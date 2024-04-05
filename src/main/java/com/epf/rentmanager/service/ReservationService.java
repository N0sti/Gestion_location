package com.epf.rentmanager.service;


import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
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
    public void edit(long id, Reservation newRent) throws ServiceException {
        try {
            LocalDate beginDate = newRent.getDebut();
            LocalDate endDate = newRent.getFin();
            Period diff = Period.between(beginDate, endDate);
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
