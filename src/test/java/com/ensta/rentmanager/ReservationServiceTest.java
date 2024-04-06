package com.ensta.rentmanager;


import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReservationServiceTest {
    @Test
    void testCreate_ShouldThrowException_WhenRentMoreThan7Days() {
        // Arrange
        ReservationDao reservationDaoMock = mock(ReservationDao.class);
        ReservationService reservationService = new ReservationService(reservationDaoMock);
        Reservation reservation = new Reservation();
        reservation.setDebut(LocalDate.now());
        reservation.setFin(LocalDate.now().plusDays(8)); // Rent for more than 7 days
        // Act & Assert
        try {
            reservationService.create(reservation);
            Assertions.fail("Expected a ServiceException to be thrown");
        } catch (ServiceException e) {
            System.out.println("Création de la réservation refusée avec succès.");
        }
    }

    @Test
    void testDelete_ShouldDeleteRent() throws ServiceException, DaoException {
        // Arrange
        ReservationDao reservationDaoMock = mock(ReservationDao.class);
        ReservationService reservationService = new ReservationService(reservationDaoMock);
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        // Act
        when(reservationDaoMock.delete(reservation)).thenReturn(1L); // Assuming deletion is successful
        long deletedId = reservationService.delete(reservation);
        // Assert
        Assertions.assertEquals(1L, deletedId);
    }

}
