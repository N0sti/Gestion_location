package com.epf.rentmanager.ui.servlet;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("cptVehicule", VehicleService.getInstance().count());
            request.setAttribute("cptUser", ClientService.getInstance().count());
            request.setAttribute("cptRent", ReservationService.getInstance().count());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/home.jsp");
        dispatcher.forward(request, response);
    }
}
