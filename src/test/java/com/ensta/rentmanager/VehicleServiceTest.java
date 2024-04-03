package com.ensta.rentmanager;

import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class VehicleServiceTest {
    @Test
    void testCreate_ShouldThrowException_WhenConstructeurIsBlank() {
        // Arrange
        VehicleDao vehicleDaoMock = mock(VehicleDao.class);
        VehicleService vehicleService = new VehicleService(vehicleDaoMock);
        Vehicle vehicle = new Vehicle();
        vehicle.setConstructeur(""); // Blank constructor
        // Act & Assert
        try {
            vehicleService.create(vehicle);
            Assertions.fail("Expected a ServiceException to be thrown");
        } catch (ServiceException e) {
            Assertions.assertEquals("Il n'y a pas de constructeur", e.getMessage());
            System.out.println("Création du véhicule refusée avec succès.");
        }
    }

}
