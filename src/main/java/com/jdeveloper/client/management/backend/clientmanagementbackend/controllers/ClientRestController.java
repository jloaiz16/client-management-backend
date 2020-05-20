package com.jdeveloper.client.management.backend.clientmanagementbackend.controllers;

import com.jdeveloper.client.management.backend.clientmanagementbackend.models.services.IClientService;
import com.jdeveloper.client.management.backend.clientmanagementbackend.models.entities.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}