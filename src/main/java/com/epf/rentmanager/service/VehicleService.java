package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.VehicleDao;

public class VehicleService {

	private VehicleDao vehicleDao;
	public static VehicleService instance;
	
	private VehicleService() {
		this.vehicleDao = VehicleDao.getInstance();
	}
	
	public static VehicleService getInstance() {
		if (instance == null) {
			instance = new VehicleService();
		}
		
		return instance;
	}


	public long create(Vehicle vehicle) throws ServiceException {
		try {
			if (vehicle.getConstructeur().isBlank()) {
				throw new ServiceException("Il n'y a pas de constructeur");
			}
			if (vehicle.getNb_places() <= 0) {
				throw new ServiceException("Une voiture a au moins une place");
			}
			return vehicleDao.create(vehicle);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public Vehicle findById(long id) throws ServiceException {
		try {
			return vehicleDao.findById(id);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Vehicle> findAll() throws ServiceException {
		try {
			return vehicleDao.findAll();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void edit(long id, Vehicle newVehicle) throws ServiceException {
		try {
			if (newVehicle.getNb_places() <= 0) {
				throw new ServiceException("Une voiture doit avoir au moins une place");
			}
			vehicleDao.update(id, newVehicle);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public long delete(Vehicle vehicle) throws ServiceException {
		try {
			for (Reservation res : ReservationService.getInstance()
					.findResaByVehicleId(vehicle.getId())) {
				/* Remove vehicle from reservations */
				res.setVehicle(null);
				ReservationService.getInstance().modify(res.getId(), res);
			} return VehicleDao.getInstance().delete(vehicle);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public int count() throws ServiceException {
		try {
			return vehicleDao.count();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

}
