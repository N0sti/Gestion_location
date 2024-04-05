package com.epf.rentmanager.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.ClientException;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
	@Autowired
	private ClientDao clientDao;
	public ClientService(){};
	public ClientService(ClientDao clientDao){
		this.clientDao = clientDao;
	}


	public long create(Client client) throws ServiceException {
		try {
			if (client.getPrenom().isBlank()) {
				throw new ServiceException("Il n'y a pas de prénom");
			}
			if (client.getNom().isBlank()) {
				throw new ServiceException("Il n'y a pas de nom");
			}
			if (client.getPrenom().length() < 3 || client.getNom().length() < 3) {
				throw new ServiceException("Votre nom ou votre prénom est trop court (<3)");
			}
			LocalDate currentDate = LocalDate.now();
			LocalDate birthDate = client.getNaissance();
			Period diff = Period.between(birthDate, currentDate);
			int age = diff.getYears();

			if (age < 18) {
				throw new ClientException("Vous n'êtes pas majeur (+18)");
			}

			if (countSameEmail(client.getEmail()) > 0) {
				throw new ClientException("Cet email existe déjà");
			}

			client.setNom(client.getNom().toUpperCase());
			return clientDao.create(client);
		} catch (DaoException | ClientException e) {
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
			ReservationService rs = new ReservationService(new ReservationDao());
			for (Reservation rent : rs.findResaByClientId(client.getId())) {
				rs.delete(rent);
			}
			return ClientDao.getInstance().delete(client);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	public int count() throws ServiceException {
		try {
			return clientDao.count();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	public void edit(long id, Client newClient) throws ServiceException {
		try {
			clientDao.update(id, newClient);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	public int countSameEmail(String email) throws ServiceException {
		try {
			return clientDao.countSameEmail(email);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}
}
