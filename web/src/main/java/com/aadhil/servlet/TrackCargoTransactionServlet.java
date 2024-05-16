package com.aadhil.servlet;

import java.io.IOException;

import com.aadhil.ejb.dto.CargoTrackerDTO;
import com.aadhil.ejb.remote.CargoTransactionService;
import com.aadhil.util.Json;
import com.google.gson.Gson;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/track")
public class TrackCargoTransactionServlet extends HttpServlet {

    @EJB
    private CargoTransactionService cargoTransactionService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trackingId = request.getParameter("id");

        CargoTrackerDTO cargoTrackerDTO = cargoTransactionService.trackTransaction(trackingId);

        Gson gson = new Gson();
        String jsonString = gson.toJson(cargoTrackerDTO);
        response.getWriter().write(jsonString);
    }
}
