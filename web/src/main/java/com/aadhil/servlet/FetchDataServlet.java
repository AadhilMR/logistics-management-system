package com.aadhil.servlet;

import java.io.IOException;
import java.util.List;

import com.aadhil.ejb.dto.CargoDTO;
import com.aadhil.ejb.dto.CargoTransactionDTO;
import com.aadhil.ejb.dto.RouteDTO;
import com.aadhil.ejb.entity.Terminal;
import com.aadhil.ejb.remote.DataFetchService;
import com.aadhil.util.Json;
import com.google.gson.Gson;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "FetchData", value = {"/terminals", "/routes", "/cargos", "/transactions", "/transaction"})
public class FetchDataServlet extends HttpServlet {

    @EJB
    private DataFetchService dataFetchService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String urlPattern = request.getRequestURI().split("/")[2];

        Json json = new Json();
        String jsonString = "";

        if("terminals".equals(urlPattern)) {
            List<Terminal> terminalList = dataFetchService.fetchTerminals();
            jsonString = json.getJsonString(terminalList);
        } else if("routes".equals(urlPattern)) {
            List<RouteDTO> routeList = dataFetchService.fetchRoutes();
            jsonString = json.getJsonString(routeList);
        } else if("cargos".equals(urlPattern)) {
            List<CargoDTO> cargoList = dataFetchService.fetchCargoAsDTO();
            jsonString = json.getJsonString(cargoList);
        } else if("transactions".equals(urlPattern)) {
            List<CargoTransactionDTO> cargoTransactionList = dataFetchService.fetchTransactions();
            jsonString = json.getJsonString(cargoTransactionList);
        } else if("transaction".equals(urlPattern)) {
            String trackingId = request.getParameter("id");
            CargoTransactionDTO cargoTransaction = dataFetchService.fetchTransaction(trackingId);
            jsonString = new Gson().toJson(cargoTransaction);
        }

        response.getWriter().write(jsonString);
    }
}
