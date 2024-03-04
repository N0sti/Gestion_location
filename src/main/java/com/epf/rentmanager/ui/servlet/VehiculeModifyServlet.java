package com.epf.rentmanager.ui.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cars/modify")
public class VehiculeModifyServlet extends HttpServlet {

    @Autowired
    VehicleService vehicleService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long vehicleId = Long.parseLong(request.getParameter("id"));
        try {
            request.setAttribute("vehicle", this.vehicleService.findById(vehicleId));
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        this.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/vehicles/modify.jsp")
                .forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        long vehicleId = Long.parseLong(request.getParameter("id"));
        String placesParameter = request.getParameter("seats");
        short places = 0;
        if (placesParameter != null && !placesParameter.isEmpty()) {
            places = Short.parseShort(placesParameter);
        }
        Vehicle vehicle = new Vehicle(vehicleId, request.getParameter("manufacturer"),
                request.getParameter("modele"), places);
        try {
            vehicleService.edit(vehicleId, vehicle);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/rentmanager/cars");
    }

}

