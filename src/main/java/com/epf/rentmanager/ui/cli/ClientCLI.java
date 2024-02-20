package com.epf.rentmanager.ui.cli;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.utils.IOUtils;

import java.util.List;

public class ClientCLI {
    public static void listClients() {
        try {
            for (Client client : ClientService.getInstance().findAll()) {
                IOUtils.print(client.toString());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public static void createClient() {
        IOUtils.print("Création d'un client");
        Client cli = new Client();
        cli.setNom(IOUtils.readString("Entrez le nom : ", true).toUpperCase());
        cli.setPrenom(IOUtils.readString("Entrez le prénom", true));
        cli.setNaissance(IOUtils.readDate("Entre la date de naissance (jj/mm/aaaa) : ", true));
        String email;
        do {
            email = IOUtils.readString("Entrez l'adresse courriel : ", true);
        } while (!email.matches("^(.+)@(\\S+)$"));
        cli.setEmail(email);
        try {
            long resId = ClientService.getInstance().create(cli);
            IOUtils.print("Le client a été créé avec l'identifiant " + resId);
        } catch (ServiceException e) {
            e.printStackTrace();
            IOUtils.print("Le client n'a pas pu être créé.");
        }
    }

    public static Client selectClient() throws ServiceException {
        IOUtils.print("Sélectionner un client");
        List<Client> clientList = ClientService.getInstance().findAll();
        int index;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        do {
            for (Client client : clientList) {
                long id = client.getId();
                IOUtils.print(" [" + id + "] " + client);
                min = Integer.min(min, (int) id);
                max = Integer.max(max, (int) id);
            }
            index = IOUtils.readInt("Entrez un indice : ");
        } while (index < min || index >= max);

        return clientList.get(index - 1);
    }

    public static void deleteClient() {
        IOUtils.print("Supprimer un client");
        try {
            long index = selectClient().getId();
            long deleted = ClientService.getInstance().delete(ClientService.getInstance().findById(index));
            assert deleted == index;

            IOUtils.print("Supprimé.");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }}
