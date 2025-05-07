package com.pool.poolcrud.Service;

import com.pool.poolcrud.Model.Client;
import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
                () -> new RuntimeException("Пользователь в БД не найден")
        );
    }

    @Transactional(readOnly = true)
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Transactional
    public Client createClient(Client newClient) {
        Client client = new Client();
        client.setName(newClient.getName());
        client.setSurname(newClient.getSurname());
        client.setPatronymic(newClient.getPatronymic());
        client.setNumber(newClient.getNumber());
        client.setEmail(newClient.getEmail());

        clientRepository.save(client);

        return client;
    }

    @Transactional
    public Client updateClient(Long id, Client clientWhoBeUpdated) {
        Client client = getById(id);
        if (client.getId() == null) {
            System.out.println("Бассейн не найден в БД");
            return null;
        }

        client.setName(clientWhoBeUpdated.getName());
        client.setSurname(clientWhoBeUpdated.getSurname());
        client.setPatronymic(clientWhoBeUpdated.getPatronymic());
        client.setNumber(clientWhoBeUpdated.getNumber());
        client.setEmail(clientWhoBeUpdated.getEmail());

        return clientRepository.save(client);
    }
}
