package com.epf.rentmanager.ui.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/rents/modify")
public class ReservationModifyServlet extends HttpServlet {
    @Autowired
    ReservationService reservationService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long rentID = Long.parseLong(request.getParameter("id"));
        try {
            request.setAttribute("clients", this.clientService.findAll());
            request.setAttribute("vehicles", this.vehicleService.findAll());
            request.setAttribute("rents", this.reservationService.findById(rentID));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        this.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/rents/modify.jsp")
                .forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        long rentID = Long.parseLong(request.getParameter("id"));
        try {
            Reservation reservation = new Reservation(
                    rentID,
                    clientService.findById(Integer.parseInt(request.getParameter("client"))),
                    vehicleService.findById(Integer.parseInt(request.getParameter("vehicle"))),
                    LocalDate.parse(request.getParameter("begin")),
                    LocalDate.parse(request.getParameter("end")));
            reservationService.edit(rentID, reservation);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/rentmanager/rents");
    }
}