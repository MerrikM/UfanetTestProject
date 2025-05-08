package com.pool.poolcrud.Controller;

import com.pool.poolcrud.Model.Client;
import com.pool.poolcrud.Model.Pool;
import com.pool.poolcrud.Model.Reservation;
import com.pool.poolcrud.Service.ClientService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/pool/client/")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<Client> getClient(@PathVariable("id") Long id) {
        Client client = clientService.getById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clientList = clientService.getAll();
        return ResponseEntity.ok(clientList);
    }

    @PostMapping("/add")
    public ResponseEntity<Client> createClient(@RequestBody ClientCreateRequest client) {

        Client newClient = new Client();
        newClient.setName(client.getName());
        newClient.setSurname(client.getSurname());
        newClient.setPatronymic(client.getPatronymic());
        newClient.setNumber(client.getNumber());
        newClient.setEmail(client.getEmail());

        Client createdClient = clientService.createClient(newClient);

        return ResponseEntity.ok(createdClient);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<Client> updateClient(@PathVariable("id") Long id, @RequestBody Client client) {
        Client updatedClient = clientService.updateClient(id, client);

        if (updatedClient == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(client);
    }

    public static class ClientCreateRequest {
        private String name;
        private String surname;
        private String patronymic;
        private String number;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getPatronymic() {
            return patronymic;
        }

        public void setPatronymic(String patronymic) {
            this.patronymic = patronymic;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
