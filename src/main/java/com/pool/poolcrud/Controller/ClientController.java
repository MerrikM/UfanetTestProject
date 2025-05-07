package com.pool.poolcrud.Controller;

import com.pool.poolcrud.Model.Client;
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
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.createClient(client));
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<Client> updateClient(@PathVariable("id") Long id, @RequestBody Client client) {
        Client updatedClient = clientService.updateClient(id, client);

        if (updatedClient == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(client);
    }
}
