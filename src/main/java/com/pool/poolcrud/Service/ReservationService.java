package com.pool.poolcrud.Service;

import com.pool.poolcrud.Model.Client;
import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.Reservation;
import com.pool.poolcrud.Model.TimeTable;
import com.pool.poolcrud.Repository.ClientRepository;
import com.pool.poolcrud.Repository.ReservationRepository;
import com.pool.poolcrud.Repository.TimeTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// На вход идет дата записи
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientService clientService;
    private final TimeTableService timeTableService;
    private final PoolService poolService;
    private final TimeTableRepository timeTableRepository;
    private final ClientRepository clientRepository;

    public ReservationService(ReservationRepository reservationRepository, ClientService clientService, TimeTableService timeTableService, PoolService poolService, TimeTableRepository timeTableRepository, ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.clientService = clientService;
        this.timeTableService = timeTableService;
        this.poolService = poolService;
        this.timeTableRepository = timeTableRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public Reservation getById(Long id) {
        return reservationRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Запись не найдена в БД")
        );
    }

    @Transactional(readOnly = true)
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation reserve(Long clientId, Long poolId, LocalDateTime dateTime) {
        Client client = clientService.getById(clientId);
        Pool pool = poolService.getPoolById(poolId);

        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();

        TimeTable timeTable = timeTableRepository.findByPoolAndDateAndTime(pool, date, time);

        if (timeTable == null) {
            throw new IllegalStateException("Запись на не рабочее время недоступна!");
        }

        if (timeTable.getCurrentBookings() >= pool.getMaxCapacity()) {
            throw new IllegalStateException("Нет свободных место");
        }

        String orderId = createOrderId(clientId, poolId, dateTime);

        Reservation reservation = new Reservation(
                client,
                timeTable,
                LocalDateTime.now(),
                orderId
        );

        if (!existOrderId(orderId)) {
            reservationRepository.save(reservation);

            timeTable.setCurrentBookings(timeTable.getCurrentBookings() + 1);
            timeTableRepository.save(timeTable);
        } else {
            throw new IllegalArgumentException("Вы уже записаны на это время");
        }

        return reservation;
    }

    public String createOrderId(Long clientId, Long poolId, LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();

        String orderId = String.valueOf(clientId) +
                String.valueOf(poolId) +
                String.valueOf(date) + String.valueOf(time);

        return orderId;
    }

    public boolean existOrderId(String orderId) {
        return reservationRepository.existsByOrderId(orderId);
    }

    @Transactional
    public void cancel(Long clientId, String orderId) {
        Reservation reservation = reservationRepository.findByOrderId(orderId);
        Client client = clientService.getById(clientId);

        TimeTable timeTable = reservation.getTimeTable();

        if (client.getReservations().isEmpty() == false) {
            client.getReservations().remove(reservation);
            clientRepository.save(client);
        } else {
            throw new IllegalArgumentException("Запись клиента не найдена");
        }

        if (timeTable.getCurrentBookings() > 0) {
            timeTable.setCurrentBookings(timeTable.getCurrentBookings() - 1);
            timeTableRepository.save(timeTable);
        }

        reservationRepository.delete(reservation);
    }
}
