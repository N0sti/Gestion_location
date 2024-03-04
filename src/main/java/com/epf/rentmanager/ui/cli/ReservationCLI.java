package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.utils.IOUtils;

import java.time.LocalDate;
import java.util.List;

public class ReservationCLI {
    /*public static void listReservation() {
        try {
            for (Reservation reservation : ReservationService.getInstance().findAll()) {
                IOUtils.print(reservation.toString());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public static void createReservation() {
        IOUtils.print("Création d'une réservation");
        Reservation res = new Reservation();
        try {
            res.setClient(ClientCLI.selectClient());
            res.setVehicle(VehiculeCLI.selectVehicle());
            res.setDebut(IOUtils.readDate("Entrez une date de début de réservation : ", true));
            LocalDate end;
            do {
                end = IOUtils.readDate("Entrez une date de fin de réservation : ", true);
            } while (end.isBefore(res.getDebut()));
            res.setFin(end);
            long resId = ReservationService.getInstance().create(res);
            IOUtils.print("La réservation a été créée avec l'identifiant " + resId);
        } catch (ServiceException e) {
            e.printStackTrace();
            IOUtils.print("La réservation n'a pas pu être créée.");
        }
    }

    public static Reservation selectReservation() throws ServiceException {
        List<Reservation> reservationList = ReservationService.getInstance().findAll();
        int index;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        do {
            for (Reservation reservation : reservationList) {
                long id = reservation.getId();
                IOUtils.print(" [" + id + "] " + reservation);
                min = Integer.min(min, (int) id);
                max = Integer.max(max, (int) id);
            }
            index = IOUtils.readInt("Entrez un indice : ");
        } while (index < min || index >= max);

        return reservationList.get(index - 1);
    }

    public static void deleteReservation() {
        IOUtils.print("Supprimer une réservation");
        try {
            Reservation reservationToDelete = selectReservation(); // Retrieve the reservation
            long deleted = ReservationService.getInstance().delete(reservationToDelete); // Pass the reservation to delete method

            assert deleted == reservationToDelete.getId();

            IOUtils.print("Supprimé.");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }*/

}
