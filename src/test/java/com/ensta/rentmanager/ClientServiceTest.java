package com.ensta.rentmanager;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;

import java.time.LocalDate;

public class ClientServiceTest {

    @Test
    void testCreate_ShouldThrowException_WhenFirstNameIsBlank() {
        ClientService clientService = new ClientService();
        Client client = mock(Client.class);
        when(client.getNaissance()).thenReturn(LocalDate.of(1990, 1, 1)); // Example: Client born in 1990
        when(client.getEmail()).thenReturn("test@example.com");
        when(client.getNom()).thenReturn("Doe");
        when(client.getPrenom()).thenReturn(""); // Blank first name
        try {
            clientService.create(client);
            Assertions.fail("Expected a ServiceException to be thrown");
        } catch (ServiceException e) {
            System.out.println("Création du client refusée avec succès.");
        }
    }

    @Test
    void testCreate_ShouldThrowException_WhenFristNameLessThan3() {
        ClientService clientService = new ClientService();
        Client client = mock(Client.class);
        when(client.getNaissance()).thenReturn(LocalDate.of(1990, 1, 1)); // Example: Client born in 1990
        when(client.getEmail()).thenReturn("test1@example.com");
        when(client.getNom()).thenReturn("D");
        when(client.getPrenom()).thenReturn("Jeanne");
        try {
            clientService.create(client);
            Assertions.fail("Expected a ServiceException to be thrown");
        } catch (ServiceException e) {
            System.out.println("Création du client refusée avec succès.");
        }
    }


    @Test
    void testCreate_ShouldThrowException_Under18() {
        ClientService clientService = new ClientService();
        Client client = mock(Client.class);
        when(client.getNaissance()).thenReturn(LocalDate.of(2020, 1, 1)); // Example: Client born in 2020
        when(client.getEmail()).thenReturn("test1@example.com");
        when(client.getNom()).thenReturn("D");
        when(client.getPrenom()).thenReturn("Jeanne");
        try {
            clientService.create(client);
            Assertions.fail("Expected a ServiceException to be thrown");
        } catch (ServiceException e) {
            System.out.println("Création du client refusée avec succès.");
        }
    }


}

