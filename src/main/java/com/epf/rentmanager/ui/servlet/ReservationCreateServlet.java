package com.epf.rentmanager.ui.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {
    private final ClientService clientService = ClientService.getInstance();
    private final VehicleService vehicleService = VehicleService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("clients", this.clientService.findAll());
            request.setAttribute("vehicles", this.vehicleService.findAll());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Reservation rent = new Reservation();
        try {
            rent.setVehicle(vehicleService.findById(Integer.parseInt(request.getParameter("vehicle"))));
            rent.setClient(clientService.findById(Integer.parseInt(request.getParameter("client"))));
            rent.setFin(LocalDate.parse(request.getParameter("begin")));
            rent.setDebut(LocalDate.parse(request.getParameter("end")));
            reservationService.create(rent);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/rentmanager/rents");
    }
}