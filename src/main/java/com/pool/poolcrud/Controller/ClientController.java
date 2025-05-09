package com.pool.poolcrud.Controller;

import com.pool.poolcrud.DTO.ClientDTO;
import com.pool.poolcrud.Model.Client;
import com.pool.poolcrud.Service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v0/pool/client/")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<?> getClient(@PathVariable("id") Long id) {
        try {
            Client client = clientService.getById(id);
            return ResponseEntity.ok(client);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clientList = clientService.getAll();
        return ResponseEntity.ok(clientList);
    }

    @PostMapping("/add")
    public ResponseEntity<Client> createClient(@RequestBody ClientDTO client) {
        return ResponseEntity.ok(clientService.createClient(client));
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateClient(@PathVariable("id") Long id, @RequestBody ClientDTO client) {
        try {
            Client updatedClient = clientService.updateClient(id, client);

            if (updatedClient == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}
