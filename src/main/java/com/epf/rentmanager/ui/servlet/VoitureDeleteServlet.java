package com.epf.rentmanager.ui.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cars/delete")
public class VoitureDeleteServlet extends HttpServlet {
    private final VehicleService vehicleService = VehicleService.getInstance();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        long vehicleId = Long.parseLong(request.getParameter("id"));
        try {
            Vehicle vehicule = this.vehicleService.findById(vehicleId);
            vehicleService.delete(vehicule);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/rentmanager/cars");
    }
}
