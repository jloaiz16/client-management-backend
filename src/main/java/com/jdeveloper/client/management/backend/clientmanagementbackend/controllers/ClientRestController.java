package com.jdeveloper.client.management.backend.clientmanagementbackend.controllers;

import com.jdeveloper.client.management.backend.clientmanagementbackend.models.services.IClientService;
import com.jdeveloper.client.management.backend.clientmanagementbackend.models.entities.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/clients")
public class ClientRestController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/list")
    public List<Client> index() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public Client show(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@RequestBody Client client) {
        return clientService.save(client);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Client uptdate(@PathVariable Long id, @RequestBody Client client) {
        Client currentClient = clientService.findById(id);
        currentClient.setName(client.getName());
        currentClient.setLastName(client.getLastName());
        currentClient.setEmail(client.getEmail());
        return clientService.save(currentClient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }
}