package com.epf.rentmanager.ui.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users/delete")
public class ClientDeleteServlet extends HttpServlet {
    private final ClientService clientService = ClientService.getInstance();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long clientId = Long.parseLong(request.getParameter("id"));
        try {
            Client client = this.clientService.findById(clientId);
            clientService.delete(client);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/rentmanager/users");
    }
}
