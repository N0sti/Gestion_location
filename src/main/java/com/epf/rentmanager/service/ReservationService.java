package com.epf.rentmanager.service;


import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    public static ReservationService instance;
    private final ReservationDao reservationDao;

    private ReservationService() {
        this.reservationDao = ReservationDao.getInstance();
    }

    public long create(Reservation reservation) throws ServiceException {
        try {
            LocalDate beginDate = reservation.getDebut();
            LocalDate endDate = reservation.getFin();
            Period diff = Period.between(beginDate, endDate);

            if (diff.getMonths() > 6 || diff.getYears() > 0) {
                throw new ServiceException("Un client ne peut pas louer une meme voiture plus de 6mois d'affiler");
            }

            return reservationDao.create(reservation);
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
    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }
}
