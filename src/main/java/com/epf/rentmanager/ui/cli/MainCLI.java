package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.utils.IOUtils;

public class MainCLI {
    private static boolean fin = false;

    public static void main(String[] args) {
        System.out.println("Bonjour");
        while (!fin) {
            afficherMenu();
        }
    }

    public static void afficherMenu() {
        IOUtils.print("Que voulez-vous faire ?");
        IOUtils.print("""
                    [1] Lister des enregistrements
                    [2] Créer des enregistrements
                    [3] Supprimer des enregistrements
                    [4] Quitter le programme""");

        int choice = IOUtils.readInt("Entrez votre choix : ");
        switch (choice) {
            case 1 -> displayListOptions();
            case 2 -> displayCreateOptions();
            case 3 -> displayDeleteOptions();
            case 4 -> {
                IOUtils.print("Au revoir !");
                fin = true;
            }
            default -> IOUtils.print("Option invalide.");
        }
    }

    public static void displayListOptions() {
        IOUtils.print("""
                    [1] Lister les clients
                    [2] Lister les véhicules
                    [3] Lister les réservations
                    [4] Quitter le programme""");

        int choice = IOUtils.readInt("Entrez votre choix : ");
        switch (choice) {
            case 1 -> ClientCLI.listClients();
            case 2 -> ClientCLI.listClients();
            case 3 -> ClientCLI.listClients();
            case 4 -> {
                IOUtils.print("Au revoir !");
                fin = true;
            }
            default -> IOUtils.print("Option invalide.");
        }
    }

    public static void displayCreateOptions() {
        IOUtils.print("""
                    [1] Créer un client
                    [2] Créer un véhicule
                    [3] Créer une réservation
                    [4] Quitter le programme""");

        int choice = IOUtils.readInt("Entrez votre choix : ");
        switch (choice) {
            case 1 -> ClientCLI.createClient();
            case 2 -> ClientCLI.createClient();
            case 3 -> ClientCLI.createClient();
            case 4 -> {
                IOUtils.print("Au revoir !");
                fin = true;
            }
            default -> IOUtils.print("Option invalide.");
        }
    }

    public static void displayDeleteOptions() {
        IOUtils.print("""
                    [1] Supprimer un client
                    [2] Supprimer un véhicule
                    [3] Supprimer une réservation
                    [4] Quitter le programme""");

        int choice = IOUtils.readInt("Entrez votre choix : ");
        switch (choice) {
            case 1 -> ClientCLI.deleteClient();
            case 2 -> ClientCLI.deleteClient();
            case 3 -> ClientCLI.deleteClient();
            case 4 -> {
                IOUtils.print("Au revoir !");
                fin = true;
            }
            default -> IOUtils.print("Option invalide.");
        }
    }


}
