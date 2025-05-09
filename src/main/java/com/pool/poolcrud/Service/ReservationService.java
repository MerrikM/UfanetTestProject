package com.pool.poolcrud.Service;

import com.pool.poolcrud.Model.Client;
import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.Reservation;
import com.pool.poolcrud.Model.TimeTable;
import com.pool.poolcrud.Repository.ClientRepository;
import com.pool.poolcrud.Repository.ReservationRepository;
import com.pool.poolcrud.Repository.TimeTableRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// На вход идет дата записи
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientService clientService;
    private final PoolService poolService;
    private final TimeTableRepository timeTableRepository;
    private final ClientRepository clientRepository;

    public ReservationService(ReservationRepository reservationRepository, ClientService clientService, PoolService poolService, TimeTableRepository timeTableRepository, ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.clientService = clientService;
        this.poolService = poolService;
        this.timeTableRepository = timeTableRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public Reservation getById(Long id) {
        return reservationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Запись не найдена в БД")
        );
    }

    @Transactional(readOnly = true)
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> getByFIO(String clientName, String clientSurname, String clientPatronymic) {
        return reservationRepository.findByClient_NameAndClient_SurnameAndClient_Patronymic(
                clientName,
                clientSurname,
                clientPatronymic
        );
    }

    @Transactional(readOnly = true)
    public List<Reservation> getByReservationDay(Long clientId, LocalDate date) {
        return reservationRepository.findByTimeTable_DateAndClient_Id(date, clientId);
    }

    @Transactional
    public Reservation reserve(Long clientId, Long poolId, LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Нельзя записаться на прошедшее время");
        }

        Client client = clientService.getById(clientId);
        Pool pool = poolService.getPoolById(poolId);
        LocalDate date = dateTime.toLocalDate();

        int clientReservations = reservationRepository.countByClientAndTimeTable_Date(client, date);
        if (clientReservations >= pool.getMaxVisitsPerDay()) {
            throw new IllegalStateException(String.format("Лимит посещений исчерпан, в день: ", pool.getMaxVisitsPerDay(), " посещений"));
        }

        TimeTable timeTable = timeTableRepository.findByPoolAndDateAndTime(pool, date, dateTime.toLocalTime());
        if (timeTable == null) {
            throw new IllegalStateException("Слот не найден в БД");
        }

        if (timeTable.getCurrentBookings() > timeTable.getRemainingCapacity()) {
            throw new IllegalStateException("Нет свободных мест");
        }

        if (reservationRepository.existsByClientAndTimeTable(client, timeTable)) {
            throw new IllegalArgumentException("Вы уже записаны на это время");
        }

        Reservation reservation = new Reservation(
                client,
                timeTable,
                LocalDateTime.now(),
                createOrderId(clientId, poolId, dateTime),
                pool,
                false
        );

        reservationRepository.save(reservation);
        timeTable.setCurrentBookings(timeTable.getCurrentBookings() + 1);
        timeTable.setRemainingCapacity(timeTable.getRemainingCapacity() - 1);
        timeTableRepository.save(timeTable);

        return reservation;
    }

    @Transactional
    public List<Reservation> reserveForAFewHours(Long clientId, Long poolId, LocalDate date, LocalTime start, LocalTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Время начала должно быть раньше времени окончания");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Нельзя записаться на прошедшую дату");
        }

        Client client = clientService.getById(clientId);
        Pool pool = poolService.getPoolById(poolId);

        int maxVisitsPerDay = pool.getMaxVisitsPerDay();
        long existingReservations = reservationRepository.countByClientAndTimeTable_Date(client, date);
        List<TimeTable> availableSlots = timeTableRepository.findByPoolAndDateAndTimeBetween(pool, date, start, end);

        if (existingReservations + availableSlots.size() > maxVisitsPerDay) {
            throw new IllegalStateException(String.format("Лимит посещений исчерпан, в день: ", pool.getMaxVisitsPerDay(), " посещений"));
        }

        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < availableSlots.size(); i++) {
            if (availableSlots.get(i).getCurrentBookings() > availableSlots.get(i).getRemainingCapacity()) {
                throw new IllegalStateException("Нет свободных мест");
            }

            if (reservationRepository.existsByClientAndTimeTable(client, availableSlots.get(i))) {
                throw new IllegalArgumentException("Вы уже записаны на это время");
            }

            Reservation reservation = new Reservation(
                    client,
                    availableSlots.get(i),
                    LocalDateTime.now(),
                    createOrderId(clientId, poolId, LocalDateTime.of(date, availableSlots.get(i).getTime())),
                    pool,
                    false
            );

            reservationRepository.save(reservation);
            availableSlots.get(i).setCurrentBookings(availableSlots.get(i).getCurrentBookings() + 1);
            availableSlots.get(i).setRemainingCapacity(availableSlots.get(i).getRemainingCapacity() - 1);
            timeTableRepository.save(availableSlots.get(i));

            reservations.add(reservation);
        }

        return reservations;
    }

    public String createOrderId(Long clientId, Long poolId, LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();

        String orderId = String.valueOf(clientId) +
                String.valueOf(poolId) +
                String.valueOf(date) + String.valueOf(time);

        return orderId;
    }

    @Transactional
    public void cancel(Long clientId, String orderId) {
        Reservation reservation = reservationRepository.findByOrderId(orderId);
        if (reservation == null) {
            throw new IllegalArgumentException("Запись не найдена");
        }

        Client client = clientService.getById(clientId);
        if (reservation.getClient().getId().equals(clientId) == false) {
            throw new IllegalArgumentException("Запись не принадлежит этому клиенту");
        }

        TimeTable timeTable = reservation.getTimeTable();

        client.getReservations().remove(reservation);
        clientRepository.save(client);

        timeTable.setCurrentBookings(timeTable.getCurrentBookings() - 1);
        timeTable.setRemainingCapacity(timeTable.getRemainingCapacity() + 1);
        timeTableRepository.save(timeTable);

        reservationRepository.delete(reservation);
    }

    @Transactional
    public void confirmVisit(String orderId) {
        Reservation reservation = reservationRepository.findByOrderId(orderId);

        if (reservation.isFinalized() == false) {
            reservation.setFinalized(true);
        } else {
            throw new IllegalStateException("Запись клиента уже подтверждена");
        }

        reservationRepository.delete(reservation);
    }
}
