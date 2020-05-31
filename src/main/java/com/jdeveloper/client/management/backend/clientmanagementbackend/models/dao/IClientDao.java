package com.jdeveloper.client.management.backend.clientmanagementbackend.models.dao;

import java.util.List;

import com.jdeveloper.client.management.backend.clientmanagementbackend.models.entities.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IClientDao extends JpaRepository<Client, Long> {

    @Query("select c from Client c where c.name like %?1%")
    List<Client> findByName(String name);

}