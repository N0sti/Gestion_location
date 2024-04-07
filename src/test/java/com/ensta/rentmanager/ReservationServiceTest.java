package com.ensta.rentmanager;


import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReservationServiceTest {
    @Test
    void testCreate_ShouldThrowException_WhenRentMoreThan7Days() {
        ReservationDao reservationDaoMock = mock(ReservationDao.class);
        ReservationService reservationService = new ReservationService(reservationDaoMock);
        Reservation reservation = new Reservation();
        reservation.setDebut(LocalDate.now());
        reservation.setFin(LocalDate.now().plusDays(8));
        try {
            reservationService.create(reservation);
            Assertions.fail("Expected a ServiceException to be thrown");
        } catch (ServiceException e) {
            System.out.println("Création de la réservation refusée avec succès.");
        }
    }

    @Test
    void testDelete_ShouldDeleteRent() throws ServiceException, DaoException {
        ReservationDao reservationDaoMock = mock(ReservationDao.class);
        ReservationService reservationService = new ReservationService(reservationDaoMock);
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        when(reservationDaoMock.delete(reservation)).thenReturn(1L);
        long deletedId = reservationService.delete(reservation);
        Assertions.assertEquals(1L, deletedId);
    }
    @Test
    void testVerification_ShouldReturnTrue_WhenVehicleAvailable() throws ServiceException, DaoException {
        // Arrange
        ReservationDao reservationDaoMock = mock(ReservationDao.class);
        ReservationService reservationService = new ReservationService(reservationDaoMock);
        Reservation reservation = new Reservation();
        reservation.setDebut(LocalDate.now());
        reservation.setFin(LocalDate.now().plusDays(2));
        reservation.setVehicle(new Vehicle(1L));
        when(reservationDaoMock.findResaByVehicleId(1L)).thenReturn(new ArrayList<>());
        boolean result = reservationService.verification(reservation);
        assertTrue(result);
    }

    @Test
    void testEdit_ShouldThrowException_WhenRentNotPossible() {
        ReservationDao reservationDaoMock = mock(ReservationDao.class);
        ReservationService reservationService = new ReservationService(reservationDaoMock);
        Reservation reservation = new Reservation();
        reservation.setDebut(LocalDate.now());
        reservation.setFin(LocalDate.now().plusDays(8));
        assertThrows(ServiceException.class, () -> reservationService.edit(1L, reservation));
    }

}
