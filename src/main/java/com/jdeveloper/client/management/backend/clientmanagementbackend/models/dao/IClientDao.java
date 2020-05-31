package com.jdeveloper.client.management.backend.clientmanagementbackend.models.dao;

import com.jdeveloper.client.management.backend.clientmanagementbackend.models.entities.Client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IClientDao extends JpaRepository<Client, Long> {

}