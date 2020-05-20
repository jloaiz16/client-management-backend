package com.jdeveloper.client.management.backend.clientmanagementbackend.models.dao;

import com.jdeveloper.client.management.backend.clientmanagementbackend.models.entities.Client;

import org.springframework.data.repository.CrudRepository;

public interface IClientDao extends CrudRepository<Client, Long> {
    
}