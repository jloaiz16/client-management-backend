package com.jdeveloper.client.management.backend.clientmanagementbackend.models.services;

import com.jdeveloper.client.management.backend.clientmanagementbackend.models.dao.IClientDao;
import com.jdeveloper.client.management.backend.clientmanagementbackend.models.entities.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IClientServiceImpl implements IClientService{
    
    @Autowired
    private IClientDao clientDao;

    @Override
    public List<Client> findAll() {
        return (List<Client>) clientDao.findAll();
    }
}