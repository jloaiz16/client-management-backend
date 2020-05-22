package com.jdeveloper.client.management.backend.clientmanagementbackend.models.services;

import java.util.List;

import com.jdeveloper.client.management.backend.clientmanagementbackend.models.entities.Client;

public interface IClientService {

    public List<Client> findAll();

    public Client findById(Long id);

    public Client save(Client client);

    public void delete(Long id);
}