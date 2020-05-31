package com.jdeveloper.client.management.backend.clientmanagementbackend.models.services;

import java.util.List;

import com.jdeveloper.client.management.backend.clientmanagementbackend.models.entities.Client;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface IClientService {

    public List<Client> findAll();

    public Page<Client> findAll(Pageable pageable);

    public Client findById(Long id);

    public Client save(Client client);

    public void delete(Long id);
}