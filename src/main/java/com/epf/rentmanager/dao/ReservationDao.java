package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao {

	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
	private static final String FIND_RESERVATIONS_QUERY_BYID = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation WHERE id=?;";
	private static final String UPDATE_RESERVATION_QUERY = "UPDATE Reservation SET client_id=?, vehicle_id=?, debut=?, fin=? WHERE id=?;";
	private static final String COUNT_RESERVATION_QUERY = "SELECT COUNT(id) AS count FROM Reservation;";

	private ClientDao clientDao;
	private VehicleDao vehicleDao;

	public ReservationDao() {
		clientDao = ClientDao.getInstance();
		vehicleDao = VehicleDao.getInstance();
	}

	public ClientDao getClientDao() {
		return clientDao;
	}
	public VehicleDao getVehicleDao() {
		return vehicleDao;
	}
	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	public void setVehicleDao(VehicleDao vehicleDao) {
		this.vehicleDao = vehicleDao;
	}



	public long create(Reservation reservation) throws DaoException {
		/**
		 * Crée une nouvelle réservation dans la base de données.
		 * @param reservation La réservation à créer.
		 * @return l'identifiant de la réservation créée.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		long ID = 0;
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(CREATE_RESERVATION_QUERY, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, reservation.getClient().getId());
			preparedStatement.setLong(2, reservation.getVehicle().getId());
			preparedStatement.setDate(3, Date.valueOf(reservation.getDebut()));
			preparedStatement.setDate(4, Date.valueOf(reservation.getFin()));
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			while (rs.next()) {
				ID = rs.getLong("id");
			}
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return ID;
	}

	public long delete(Reservation rent) throws DaoException {
		/**
		 * Supprime une réservation de la base de données.
		 * @param rent La réservation à supprimer.
		 * @return l'identifiant de la réservation supprimée.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		long reservationId = rent.getId();
		try (
				Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_RESERVATION_QUERY)
		) {
			preparedStatement.setLong(1, reservationId);
			preparedStatement.execute();
			int count = count();
			if (count == 0) {
				Statement updateStatement = connection.createStatement();
				updateStatement.executeUpdate("ALTER TABLE Reservation ALTER COLUMN id RESTART WITH 1");
				updateStatement.close();
			} else {
				PreparedStatement updateStatement = connection.prepareStatement("UPDATE Reservation SET id = id - 1 WHERE id > ?");
				updateStatement.setLong(1, reservationId);
				updateStatement.executeUpdate();
				updateStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return reservationId;
	}



	public List<Reservation> findResaByClientId(long clientId) throws DaoException {
		/**
		 * Récupère les réservations d'un client à partir de son identifiant.
		 * @param clientId L'identifiant du client.
		 * @return Une liste contenant les réservations du client.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		List<Reservation> reservation = new ArrayList<Reservation>();
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);
			preparedStatement.setInt(1, (int) clientId);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				long id = rs.getInt("id");
				long vehicleId = rs.getInt("vehicle_id");
				Client client = new Client(clientId);
				Vehicle vehicle = new Vehicle(vehicleId);
				LocalDate debut = rs.getDate("debut").toLocalDate();
				LocalDate fin = rs.getDate("fin").toLocalDate();
				reservation.add(new Reservation(id, client, vehicle, debut, fin));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return reservation;
	}


	public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {
		/**
		 * Récupère les réservations d'un véhicule à partir de son identifiant.
		 * @param vehicleId L'identifiant du véhicule.
		 * @return Une liste contenant les réservations du véhicule.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		List<Reservation> reservation = new ArrayList<Reservation>();
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY);
			preparedStatement.setInt(1, (int) vehicleId);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				long id = (rs.getInt("id"));
				long clientId = (rs.getInt("client_id"));
				Client client = new Client(clientId);
				Vehicle vehicle = new Vehicle(vehicleId);
				LocalDate debut = (rs.getDate("debut").toLocalDate());
				LocalDate fin = (rs.getDate("fin").toLocalDate());
				reservation.add(new Reservation(id, client, vehicle, debut, fin));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return reservation;
	}

	public List<Reservation> findAll() throws DaoException {
		/**
		 * Récupère toutes les réservations présentes dans la base de données.
		 * @return Une liste contenant toutes les réservations présentes en base.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		List<Reservation> reservation = new ArrayList<Reservation>();
		try {
			Connection connection = ConnectionManager.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(FIND_RESERVATIONS_QUERY);
			while (rs.next()) {
				long id = (rs.getInt("id"));
				long clientId = (rs.getInt("client_id"));
				long vehicleId = (rs.getInt("vehicle_id"));
				Client client = new Client(clientId);
				Vehicle vehicle = new Vehicle(vehicleId);
				LocalDate debut = (rs.getDate("debut").toLocalDate());
				LocalDate fin = (rs.getDate("fin").toLocalDate());
				reservation.add(new Reservation(id, client, vehicle, debut, fin));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return reservation;
	}
	public void update(long id, Reservation newData) throws DaoException {
		/**
		 * Met à jour les informations d'une réservation dans la base de données.
		 * @param id L'identifiant de la réservation à mettre à jour.
		 * @param newData Les nouvelles informations de la réservation.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RESERVATION_QUERY);
			preparedStatement.setLong(1, newData.getClient().getId());
			preparedStatement.setLong(2, newData.getVehicle().getId());
			preparedStatement.setDate(3, Date.valueOf(newData.getDebut()));
			preparedStatement.setDate(4, Date.valueOf(newData.getFin()));
			preparedStatement.setLong(5, id);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}
	public Reservation findById(long id) throws DaoException {
		/**
		 * Recherche une réservation dans la base de données par son identifiant.
		 * @param id L'identifiant de la réservation à rechercher.
		 * @return La réservation trouvée, ou null si aucune réservation correspondante n'est trouvée.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		Reservation reservation = null;
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(FIND_RESERVATIONS_QUERY_BYID);
			preparedStatement.setLong(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				reservation = new Reservation(id, clientDao.findById(rs.getLong("client_id")),
						vehicleDao.findById(rs.getLong("vehicle_id")),
						rs.getDate("debut").toLocalDate(), rs.getDate("fin").toLocalDate());
			}
			preparedStatement.close();
			connection.close();
		} catch (SQLException | DaoException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return reservation;

	}
	public int count() throws DaoException {
		/**
		 * Compte le nombre total de réservations dans la base de données.
		 * @return Le nombre total de réservations.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		try {
			int cpt = 0;
			Connection connection = ConnectionManager.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(COUNT_RESERVATION_QUERY);
			if (rs.next()) {
				cpt = rs.getInt("count");
			}
			statement.close();
			connection.close();
			return cpt;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}
}
