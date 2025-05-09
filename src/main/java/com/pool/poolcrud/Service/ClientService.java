package com.pool.poolcrud.Service;

import com.pool.poolcrud.DTO.ClientDTO;
import com.pool.poolcrud.Model.Client;
import com.pool.poolcrud.Repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public Client getById(Long id) {
        return clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Пользователь в БД не найден")
        );
    }

    @Transactional(readOnly = true)
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Transactional
    public Client createClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setSurname(clientDTO.getSurname());
        client.setPatronymic(clientDTO.getPatronymic());
        client.setNumber(clientDTO.getNumber());
        client.setEmail(clientDTO.getEmail());

        return clientRepository.save(client);

    }

    @Transactional
    public Client updateClient(Long id, ClientDTO clientWhoBeUpdated) {
        Client client = getById(id);

        client.setName(clientWhoBeUpdated.getName());
        client.setSurname(clientWhoBeUpdated.getSurname());
        client.setPatronymic(clientWhoBeUpdated.getPatronymic());
        client.setNumber(clientWhoBeUpdated.getNumber());

        if (!client.getEmail().equals(clientWhoBeUpdated.getEmail())) {
            if (clientRepository.existsClientByEmail((clientWhoBeUpdated.getEmail()))) {
                throw new IllegalArgumentException("Данная почта уже занята");
            }
            client.setEmail(clientWhoBeUpdated.getEmail());
        }

        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
