package com.jdeveloper.client.management.backend.clientmanagementbackend.controllers;

import com.jdeveloper.client.management.backend.clientmanagementbackend.models.services.IClientService;
import com.jdeveloper.client.management.backend.clientmanagementbackend.models.entities.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClientRestController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/clients/list")
    public List<Client> index(@RequestParam(required = false) String name) {
        if (name == null) {
            return clientService.findAll();
        } else {
            return clientService.findByName(name);
        }
    }

    @GetMapping("/clients/list/page/{page}")
    public Page<Client> index(@PathVariable Integer page) {
        return clientService.findAll(PageRequest.of(page, 5));
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Client client = null;
        HashMap<String, Object> response = new HashMap<String, Object>();
        try {
            client = clientService.findById(id);
        } catch (DataAccessException e) {
            response.put("message", "Error doing the database query");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (client == null) {
            response.put("message", "The client doesn't exist in the database");
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Client>(client, HttpStatus.OK);
    }

    @PostMapping("/clients")
    public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result) {
        Client newClient = null;
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(err -> "The property '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            newClient = clientService.save(client);
        } catch (DataAccessException e) {
            response.put("message", "Error doing the database insert");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Client>(newClient, HttpStatus.CREATED);
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<?> uptdate(@Valid @RequestBody Client client, BindingResult result, @PathVariable Long id) {
        Client currentClient = null;
        Client updatedClient = null;
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(err -> "The property '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            currentClient = clientService.findById(id);
        } catch (DataAccessException e) {
            response.put("message", "Error doing the database query");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (client == null) {
            response.put("message", "The client doesn't exist in the database");
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        try {
            currentClient.setName(client.getName());
            currentClient.setLastName(client.getLastName());
            currentClient.setEmail(client.getEmail());
            currentClient.setCreateAt(client.getCreateAt());
            updatedClient = clientService.save(currentClient);
        } catch (DataAccessException e) {
            response.put("message", "Error doing the database update");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Client>(updatedClient, HttpStatus.OK);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        try {
            clientService.delete(id);
        } catch (DataAccessException e) {
            response.put("message", "Error doing the database delete");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "Client was deleted succesfully");
        return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
    }

    @PostMapping("/clients/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        Client client = null;
        try {
            client = clientService.findById(id);
        } catch (DataAccessException e) {
            response.put("message", "Error doing the database query");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (client == null) {
            response.put("message", "The client doesn't exist in the database");
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        if (!file.isEmpty()) {
            String fileName = UUID.randomUUID().toString().concat("_")
                    .concat(file.getOriginalFilename().replace(" ", ""));
            Path rutePath = Paths.get("uploads").resolve(fileName).toAbsolutePath();

            try {
                Files.copy(file.getInputStream(), rutePath);
            } catch (IOException e) {
                response.put("message", "Error doing the file upload");
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            try {
                client.setPhoto(fileName);
                clientService.save(client);
                response.put("client", client);
                response.put("message", "Photo " + fileName + " was uploaded succesfully");
            } catch (DataAccessException e) {
                response.put("message", "Error doing the database update");
                response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
    }
}