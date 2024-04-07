package com.epf.rentmanager.dao;

import com.epf.rentmanager.exception.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository

public class VehicleDao {
	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES(?, ?, ?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
	private static final String UPDATE_VEHICLE_QUERY = "UPDATE Vehicle SET constructeur=?, modele=?, nb_places=? WHERE id=?;";
	private static final String COUNT_VEHICLES_QUERY = "SELECT COUNT(id) AS count FROM Vehicle;";

	private static VehicleDao instance = null;
	private VehicleDao() {}
	public static VehicleDao getInstance() {
		if(instance == null) {
			instance = new VehicleDao();
		}
		return instance;
	}
	public long create(Vehicle vehicle) throws DaoException {
		/**
		 * Crée un nouveau véhicule dans la base de données.
		 * @param vehicle Le véhicule à créer.
		 * @return l'identifiant du véhicule créé.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		long ID = 0;
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, vehicle.getConstructeur());
			preparedStatement.setString(2, vehicle.getModele());
			preparedStatement.setInt(3, vehicle.getNb_places());
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			while (rs.next()) {
				ID = rs.getLong("id");
			}
			preparedStatement.close();
			if (ID > 0) {
				PreparedStatement availableIdStatement = connection.prepareStatement("SELECT MIN(id) FROM Vehicle WHERE id > ?");
				availableIdStatement.setLong(1, ID);
				ResultSet availableIdResultSet = availableIdStatement.executeQuery();
				if (availableIdResultSet.next()) {
					long newID = availableIdResultSet.getLong(1);
					if (!availableIdResultSet.wasNull()) {
						ID = newID;
					}
				}
				availableIdStatement.close();
			}
			connection.close();
			return ID;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public long delete(Vehicle vehicle) throws DaoException {
		/**
		 * Supprime un véhicule de la base de données.
		 * @param vehicle Le véhicule à supprimer.
		 * @return l'identifiant du véhicule supprimé.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		long vehicleId = vehicle.getId();
		try (
				Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_VEHICLE_QUERY)
		) {
			preparedStatement.setLong(1, vehicleId);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return vehicleId;
	}

	public Vehicle findById(long id) throws DaoException {
		/**
		 * Recherche un véhicule dans la base de données par son identifiant.
		 * @param id L'identifiant du véhicule à rechercher.
		 * @return Le véhicule trouvé, ou un véhicule vide si aucun véhicule correspondant n'est trouvé.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		Vehicle vehicle = new Vehicle();
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(FIND_VEHICLE_QUERY);
			preparedStatement.setInt(1, (int) id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vehicle.setId(id);
				vehicle.setConstructeur(rs.getString("constructeur"));
				vehicle.setModele(rs.getString("modele"));
				vehicle.setNb_places(rs.getInt("nb_places"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return vehicle;
	}

	public List<Vehicle> findAll() throws DaoException {
		/**
		 * Récupère tous les véhicules présents dans la base de données.
		 * @return Une liste contenant tous les véhicules présents en base.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		try {
			Connection connection = ConnectionManager.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(FIND_VEHICLES_QUERY);
			while (rs.next()) {
				long id = (rs.getInt("id"));
				String constructeur = (rs.getString("constructeur"));
				String modele = (rs.getString("modele"));
				int nb_places = (rs.getInt("nb_places"));
				vehicles.add(new Vehicle(id, constructeur, modele, nb_places));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return vehicles;
	}

	public void update(long id, Vehicle newVehicle) throws DaoException {
		/**
		 * Met à jour les informations d'un véhicule dans la base de données.
		 * @param id L'identifiant du véhicule à mettre à jour.
		 * @param newVehicle Les nouvelles informations du véhicule.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		try (
				Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_VEHICLE_QUERY)
		) {
			preparedStatement.setString(1, newVehicle.getConstructeur());
			preparedStatement.setString(2, newVehicle.getModele());
			preparedStatement.setLong(3, newVehicle.getNb_places());
			preparedStatement.setLong(4, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public int count() throws DaoException {
		/**
		 * Compte le nombre total de véhicules dans la base de données.
		 * @return Le nombre total de véhicules.
		 * @throws DaoException en cas d'erreur lors de la connexion à la base de données ou dans l'exécution de la requête.
		 */
		try {
			int cpt = 0;
			Connection connection = ConnectionManager.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(COUNT_VEHICLES_QUERY);
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
