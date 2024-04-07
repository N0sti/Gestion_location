package com.ensta.rentmanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.service.VehicleService;
import org.junit.Before;
import org.junit.Test;

import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;

public class VehicleServiceTest {

    private VehicleService vehicleService;
    private VehicleDao vehicleDao;

    @Before
    public void setUp() {
        vehicleDao = mock(VehicleDao.class);
        vehicleService = new VehicleService(vehicleDao);
    }

    @Test
    public void testCreateVehicle() throws ServiceException, DaoException {
        Vehicle vehicle = new Vehicle();
        vehicle.setConstructeur("Renault");
        vehicle.setModele("Clio");
        vehicle.setNb_places(5);
        when(vehicleDao.create(vehicle)).thenReturn(1L);
        long id = vehicleService.create(vehicle);
        assertEquals(1L, id);
    }

    @Test(expected = ServiceException.class)
    public void testCreateVehicleWithBlankConstructeur() throws ServiceException, DaoException {
        Vehicle vehicle = new Vehicle();
        vehicle.setModele("Clio");
        vehicle.setNb_places(5);
        vehicleService.create(vehicle);
    }

    @Test(expected = ServiceException.class)
    public void testCreateVehicleWithBlankModele() throws ServiceException, DaoException {
        Vehicle vehicle = new Vehicle();
        vehicle.setConstructeur("Renault");
        vehicle.setNb_places(5);
        vehicleService.create(vehicle);
    }

    @Test(expected = ServiceException.class)
    public void testCreateVehicleWithInvalidNbPlaces() throws ServiceException, DaoException {
        Vehicle vehicle = new Vehicle();
        vehicle.setConstructeur("Renault");
        vehicle.setModele("Clio");
        vehicle.setNb_places(0);
        vehicleService.create(vehicle);
    }

    @Test(expected = ServiceException.class)
    public void testCreateVehicleWithOutOfRangeNbPlaces() throws ServiceException, DaoException {
        Vehicle vehicle = new Vehicle();
        vehicle.setConstructeur("Renault");
        vehicle.setModele("Clio");
        vehicle.setNb_places(10);
        vehicleService.create(vehicle);
    }

    @Test
    public void testFindAllVehicles() throws ServiceException, DaoException {
        List<Vehicle> vehicles = new ArrayList<>();
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setConstructeur("Renault");
        vehicle1.setModele("Clio");
        vehicle1.setNb_places(5);
        vehicles.add(vehicle1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setConstructeur("Peugeot");
        vehicle2.setModele("208");
        vehicle2.setNb_places(5);
        vehicles.add(vehicle2);

        Vehicle vehicle3 = new Vehicle();
        vehicle3.setConstructeur("Volkswagen");
        vehicle3.setModele("Golf");
        vehicle3.setNb_places(5);
        vehicles.add(vehicle3);

        when(vehicleDao.findAll()).thenReturn(vehicles);

        List<Vehicle> foundVehicles = vehicleService.findAll();
        assertNotNull(foundVehicles);
        assertEquals(3, foundVehicles.size());
    }

    // Ajoutez d'autres tests selon vos besoins

}
