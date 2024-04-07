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
		/**
		 * Crée un nouveau client dans la base de données après vérification des critères.
		 * @param client Le client à créer.
		 * @return L'identifiant du client créé.
		 * @throws ServiceException Si une erreur survient lors de la création du client.
		 */
		try {
			verification(client);
			client.setNom(client.getNom().toUpperCase());
			return clientDao.create(client);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public void verification(Client client) throws ServiceException {
		/**
		 * Vérifie si les informations du client respectent les critères de création/modification.
		 * @param client Le client à vérifier.
		 * @throws ServiceException Si une erreur survient lors de la vérification des critères.
		 */
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
		} catch (ClientException | ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}



	public Client findById(long id) throws ServiceException {
		/**
		 * Recherche un client dans la base de données par son identifiant.
		 * @param id L'identifiant du client à rechercher.
		 * @return Le client trouvé.
		 * @throws ServiceException Si une erreur survient lors de la recherche du client.
		 */
		try {
			return clientDao.findById(id);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Client> findAll() throws ServiceException {
		/**
		 * Récupère tous les clients présents dans la base de données.
		 * @return Une liste contenant tous les clients.
		 * @throws ServiceException Si une erreur survient lors de la récupération des clients.
		 */
		try {
			return clientDao.findAll();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public long delete(Client client) throws ServiceException {
		/**
		 * Supprime un client de la base de données.
		 * @param client Le client à supprimer.
		 * @return L'identifiant du client supprimé.
		 * @throws ServiceException Si une erreur survient lors de la suppression du client.
		 */
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
		/**
		 * Compte le nombre total de clients dans la base de données.
		 * @return Le nombre total de clients.
		 * @throws ServiceException Si une erreur survient lors du comptage des clients.
		 */
		try {
			return clientDao.count();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	public void edit(long id, Client newClient) throws ServiceException {
		/**
		 * Modifie les informations d'un client dans la base de données.
		 * @param id L'identifiant du client à modifier.
		 * @param newClient Les nouvelles informations du client.
		 * @throws ServiceException Si une erreur survient lors de la modification du client.
		 */
		try {
			verification(newClient);
			clientDao.update(id, newClient);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	public int countSameEmail(String email) throws ServiceException {
		/**
		 * Compte le nombre de clients ayant la même adresse e-mail dans la base de données.
		 * @param email L'adresse e-mail à rechercher.
		 * @return Le nombre de clients ayant la même adresse e-mail.
		 * @throws ServiceException Si une erreur survient lors du comptage des clients.
		 */
		try {
			return clientDao.countSameEmail(email);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}
}
