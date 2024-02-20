package com.epf.rentmanager.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.ClientException;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;

public class ClientService {

	private ClientDao clientDao;
	public static ClientService instance;
	
	private ClientService() {
		this.clientDao = ClientDao.getInstance();
	}
	
	public static ClientService getInstance() {
		if (instance == null) {
			instance = new ClientService();
		}
		
		return instance;
	}


	public long create(Client client) throws ServiceException {
		try {
			if (client.getPrenom().isBlank()) {
				throw new ServiceException("Il n'y a pas de prénom");
			}
			if (client.getNom().isBlank()) {
				throw new ServiceException("Il n'y a pas de nom");
			}
			client.setNom(client.getNom().toUpperCase());
			return clientDao.create(client);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public Client findById(long id) throws ServiceException {
		try {
			return clientDao.findById(id);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Client> findAll() throws ServiceException {
		try {
			return clientDao.findAll();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public long delete(Client client) throws ServiceException {
		try {
			for (Reservation res : ReservationService.getInstance()
					.findResaByClientId(client.getId())) {
				/* Reservations from a client can be deleted if the client is removed. */
				ReservationService.getInstance().delete(res);
			}
			return ClientDao.getInstance().delete(client);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

}
