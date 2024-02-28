package com.epf.rentmanager.ui.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users/details")
public class ClientDetailServlet extends HttpServlet {
    private final ClientService clientService = ClientService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long clientId = Long.parseLong(request.getParameter("id"));
        try {
            request.setAttribute("client", this.clientService.findById(clientId));
            request.setAttribute("allReservations", this.reservationService.findResaByClientId(clientId));
            //request.setAttribute("allVehicles", this.reservationService.findAllVehiclesPerClientId(clientId));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/details.jsp").forward(request, response);
    }
}
