package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.VehicleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

	@Autowired
	private VehicleDao vehicleDao;
	public VehicleService(VehicleDao vehicleDao) {
		this.vehicleDao = vehicleDao;
	}
	public VehicleService() {}

	public long create(Vehicle vehicle) throws ServiceException {
		/**
		 * Crée un nouveau véhicule.
		 * Vérifie d'abord si la création du véhicule est possible.
		 * @param vehicle Le véhicule à créer.
		 * @return L'identifiant du véhicule créé.
		 * @throws ServiceException Si la création du véhicule n'est pas possible ou s'il y a une erreur de service.
		 */
		try {
			verification(vehicle);
			return vehicleDao.create(vehicle);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public void edit(long id, Vehicle newVehicle) throws ServiceException {
		/**
		 * Modifie les informations d'un véhicule existant.
		 * Vérifie d'abord si la modification du véhicule est possible.
		 * @param id L'identifiant du véhicule à modifier.
		 * @param newVehicle Les nouvelles informations du véhicule.
		 * @throws ServiceException Si la modification du véhicule n'est pas possible ou s'il y a une erreur de service.
		 */
		try {
			verification(newVehicle);
			vehicleDao.update(id, newVehicle);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public void verification(Vehicle vehicle) throws ServiceException {
		/**
		 * Vérifie si les informations d'un véhicule sont valides.
		 * @param vehicle Le véhicule à vérifier.
		 * @throws ServiceException Si les informations du véhicule ne sont pas valides.
		 */
		try {
			if (vehicle.getConstructeur() == null || vehicle.getConstructeur().isBlank()) {
				throw new ServiceException("Il n'y a pas de constructeur");
			}
			if (vehicle.getModele() == null || vehicle.getModele().isBlank()) {
				throw new ServiceException("Il n'y a pas de modèle");
			}
			if (vehicle.getNb_places() < 2 || vehicle.getNb_places() > 9) {
				throw new ServiceException("Une voiture doit avoir entre 2 et 9 places");
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}


	public Vehicle findById(long id) throws ServiceException {
		/**
		 * Trouve un véhicule par son identifiant.
		 * @param id L'identifiant du véhicule à rechercher.
		 * @return Le véhicule correspondant à l'identifiant spécifié.
		 * @throws ServiceException Si une erreur survient lors de l'accès aux données.
		 */
		try {
			return vehicleDao.findById(id);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Vehicle> findAll() throws ServiceException {
		/**
		 * Récupère tous les véhicules.
		 * @return Une liste contenant tous les véhicules.
		 * @throws ServiceException Si une erreur survient lors de l'accès aux données.
		 */
		try {
			return vehicleDao.findAll();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public long delete(Vehicle vehicle) throws ServiceException {
		/**
		 * Supprime un véhicule et toutes ses réservations associées.
		 * @param vehicle Le véhicule à supprimer.
		 * @return L'identifiant du véhicule supprimé.
		 * @throws ServiceException Si une erreur survient lors de l'accès aux données.
		 */
		try {
			ReservationService rs = new ReservationService(new ReservationDao());
			for (Reservation rent : rs.findResaByVehicleId(vehicle.getId())) {
				rs.delete(rent);
			} return VehicleDao.getInstance().delete(vehicle);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public int count() throws ServiceException {
		/**
		 * Compte le nombre total de véhicules.
		 * @return Le nombre total de véhicules.
		 * @throws ServiceException Si une erreur survient lors de l'accès aux données.
		 */
		try {
			return vehicleDao.count();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

}
