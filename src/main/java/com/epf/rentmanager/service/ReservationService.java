package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
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
        /**
         * Crée une nouvelle réservation.
         * Vérifie d'abord si la réservation est possible en utilisant la méthode {@link #verification}.
         * @param reservation La réservation à créer.
         * @return L'identifiant de la réservation créée.
         * @throws ServiceException Si la réservation n'est pas possible ou s'il y a une erreur de service.
         */
        try {
            if (!verification(reservation)) {
                throw new ServiceException("La réservation n'est pas possible.");
            }
            return reservationDao.create(reservation);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
    public boolean verification(Reservation reservation) throws ServiceException {
        /**
         * Vérifie si la création ou la modification d'une réservation est possible.
         * @param reservation La réservation à vérifier.
         * @return true si la réservation est possible, sinon false.
         * @throws ServiceException Si la réservation n'est pas possible en raison de contraintes métier.
         */
        try {
            LocalDate beginDate = reservation.getDebut();
            LocalDate endDate = reservation.getFin();
            Period diff = Period.between(beginDate, endDate);
            if (diff.getDays() > 7 || diff.getMonths() > 0 || diff.getYears() > 0){
                throw new ServiceException("Un client ne peut pas louer une même voiture plus de 7 jours d'affilée.");
            }
            boolean available = isVehicleAvailableForPeriod(reservation.getVehicle().getId(), beginDate, endDate);
            if (!available) {
                throw new ServiceException("La voiture est déjà réservée pendant cette période.");
            }
            return true;
        } catch (ServiceException e) {
            throw e;
        }
    }
    private boolean isVehicleAvailableForPeriod(long vehicleId, LocalDate startDate, LocalDate endDate) throws ServiceException {
        /**
         * Vérifie si un véhicule est disponible pour une période donnée.
         * @param vehicleId L'identifiant du véhicule à vérifier.
         * @param startDate La date de début de la période.
         * @param endDate La date de fin de la période.
         * @return true si le véhicule est disponible, sinon false.
         * @throws ServiceException Si une erreur survient lors de l'accès aux données.
         */
        try {
            List<Reservation> reservations = reservationDao.findResaByVehicleId(vehicleId);
            boolean dispo = true;
            for (Reservation resa : reservations) {
                if (startDate.equals(resa.getFin())) {
                    dispo= false;
                }
            }
            return dispo;
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    public long delete(Reservation reservation) throws ServiceException {
        /**
         * Supprime une réservation.
         * @param reservation La réservation à supprimer.
         * @return L'identifiant de la réservation supprimée.
         * @throws ServiceException Si une erreur survient lors de l'accès aux données.
         */
        try {
            return reservationDao.delete(reservation);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
    public void edit(long id, Reservation newRent) throws ServiceException {
        /**
         * Modifie une réservation existante.
         * Vérifie d'abord si la réservation modifiée est possible en utilisant la méthode {@link #verification}.
         * @param id L'identifiant de la réservation à modifier.
         * @param newRent Les nouvelles données de la réservation.
         * @throws ServiceException Si la réservation n'est pas possible ou s'il y a une erreur de service.
         */
        try {
            if (!verification(newRent)) {
                throw new ServiceException("La réservation n'est pas possible.");
            }
            reservationDao.update(id, newRent);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    public List<Reservation> findResaByClientId(long id) throws ServiceException {
        /**
         * Trouve toutes les réservations associées à un client donné.
         * @param id L'identifiant du client.
         * @return Une liste de réservations associées au client.
         * @throws ServiceException Si une erreur survient lors de l'accès aux données.
         */
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
        /**
         * Trouve toutes les réservations associées à un véhicule donné.
         * @param id L'identifiant du véhicule.
         * @return Une liste de réservations associées au véhicule.
         * @throws ServiceException Si une erreur survient lors de l'accès aux données.
         */
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
        /**
         * Récupère toutes les réservations.
         * @return Une liste contenant toutes les réservations.
         * @throws ServiceException Si une erreur survient lors de l'accès aux données.
         */
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
        /**
         * Trouve une réservation par son identifiant.
         * @param id L'identifiant de la réservation à rechercher.
         * @return La réservation correspondant à l'identifiant spécifié.
         * @throws ServiceException Si une erreur survient lors de l'accès aux données.
         */
        try {
            return reservationDao.findById(id);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
    public int count() throws ServiceException {
        /**
         * Compte le nombre total de réservations.
         * @return Le nombre total de réservations.
         * @throws ServiceException Si une erreur survient lors de l'accès aux données.
         */
        try {
            ReservationDao reservationDao = new ReservationDao();
            return reservationDao.count();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

}
