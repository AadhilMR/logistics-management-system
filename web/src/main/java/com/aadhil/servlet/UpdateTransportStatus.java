package com.aadhil.servlet;

import java.io.IOException;

import com.aadhil.ejb.remote.CargoTransactionService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/updateStatus")
public class UpdateTransportStatus extends HttpServlet {

    @EJB
    private CargoTransactionService cargoTransactionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String trackingId = request.getParameter("id");
            String newStatus = request.getParameter("status");

            cargoTransactionService.updateStatus(trackingId, newStatus);
            response.getWriter().write("success");
        } catch (Exception ex) {
            response.getWriter().write("error");
        }
    }
}
