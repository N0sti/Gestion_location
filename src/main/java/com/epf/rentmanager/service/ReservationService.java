package com.epf.rentmanager.service;


import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    public ReservationService() {}
    @Autowired
    private ReservationDao reservationDao;
    public ReservationService(ReservationDao ReservationDao){
        this.reservationDao = ReservationDao;
    }


    public long create(Reservation reservation) throws ServiceException {
        try {
            LocalDate beginDate = reservation.getDebut();
            LocalDate endDate = reservation.getFin();
            Period diff = Period.between(beginDate, endDate);

            if (diff.getDays() > 7 || diff.getMonths() > 0 || diff.getYears() > 0){
                throw new ServiceException("Un client ne peut pas louer une meme voiture plus de 7jours d'affiler");
            }

            // Vérification de la contrainte
            boolean Available = isVehicleAvailableForPeriod(reservation.getVehicle().getId(), beginDate, endDate);
            System.out.println("Available "+ Available);
            if (Available==false) {
                throw new ServiceException("La voiture est déjà réservée pendant cette période.");
            }
            return reservationDao.create(reservation);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    private boolean isVehicleAvailableForPeriod(long vehicleId, LocalDate startDate, LocalDate endDate) throws ServiceException {
        try {
            List<Reservation> reservations = reservationDao.findResaByVehicleId(vehicleId);
            boolean dispo = true;
            for (Reservation resa : reservations) {
                // Vérifier s'il y a une intersection entre les périodes de réservation
                System.out.print("startDate==resa.getFin()");
                System.out.println(startDate.equals(resa.getFin()));
                if (startDate.equals(resa.getFin())) {
                    dispo= false; // Il y a un chevauchement, la voiture n'est pas disponible
                }
                System.out.println("start date: "+ startDate + " resa.getFin(): " + resa.getFin() + dispo);
            }

            return dispo; // Aucun chevauchement, la voiture est disponible
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }


    public long delete(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.delete(reservation);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
    public void edit(long id, Reservation newRent) throws ServiceException {
        try {
            LocalDate beginDate = newRent.getDebut();
            LocalDate endDate = newRent.getFin();
            Period diff = Period.between(beginDate, endDate);
            if (diff.getDays() > 7 || diff.getMonths() > 0 || diff.getYears() > 0){
                throw new ServiceException("Un client ne peut pas louer une meme voiture plus de 7jours d'affiler");
            }
            boolean Available = isVehicleAvailableForPeriod(newRent.getVehicle().getId(), beginDate, endDate);
            System.out.println("Available "+ Available);
            if (Available==false) {
                throw new ServiceException("La voiture est déjà réservée pendant cette période.");
            }
            reservationDao.update(id, newRent);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    public List<Reservation> findResaByClientId(long id) throws ServiceException {
        try {
            List<Reservation> reservation = new ArrayList<Reservation>();
            reservation = reservationDao.findResaByClientId(id);
            for (Reservation r : reservation) {
                r.setVehicle(reservationDao.getVehicleDao().findById(r.getVehicle().getId()));
                r.setClient(reservationDao.getClientDao().findById(r.getClient().getId()));
            }
            return reservation;
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException();
        }
    }

    public List<Reservation> findResaByVehicleId(long id) throws ServiceException {
        try {
            List<Reservation> reservation = new ArrayList<Reservation>();
            reservation = reservationDao.findResaByVehicleId(id);
            for (Reservation r : reservation) {
                r.setVehicle(reservationDao.getVehicleDao().findById(r.getVehicle().getId()));
                r.setClient(reservationDao.getClientDao().findById(r.getClient().getId()));
            }
            return reservation;
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException();
        }
    }

    public void modify(long id, Reservation newData) throws ServiceException {
        try {
            reservationDao.update(id, newData);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try {
            List<Reservation> reservation = new ArrayList<Reservation>();
            reservation = reservationDao.findAll();
            for (Reservation r : reservation) {
                r.setVehicle(reservationDao.getVehicleDao().findById(r.getVehicle().getId()));
                r.setClient(reservationDao.getClientDao().findById(r.getClient().getId()));
            }
            return reservation;
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException();
        }
    }

    public Reservation findById(long id) throws ServiceException {
        try {
            return reservationDao.findById(id);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
    public int count() throws ServiceException {
        try {
            ReservationDao reservationDao = new ReservationDao();
            return reservationDao.count();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

}
