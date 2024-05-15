package com.aadhil.servlet;

import java.io.IOException;
import java.util.HashMap;

import com.aadhil.ejb.remote.CargoTransactionService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TransactionServlet", urlPatterns = "/transaction")
public class CreateCargoTransactionServlet extends HttpServlet {

    @EJB
    private CargoTransactionService transactionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String responseText = "";
        try {
            String cargoId = request.getParameter("cargo");
            int originId = Integer.parseInt(request.getParameter("origin"));
            int destinationId = Integer.parseInt(request.getParameter("dest"));

            HashMap<String, String> responseData = transactionService.createTransaction(cargoId, originId, destinationId);
            String trackingId = responseData.get("trackingId");
            String routeName = responseData.get("route");

            responseText = "{\"response\":\"success\", \"trackingId\":\"" + trackingId + "\", \"route\":\""+ routeName +"\"}";
        } catch (Exception ex) {
            responseText = "{\"response\":\"error\"}";
        } finally {
            response.getWriter().write(responseText);
        }
    }
}
