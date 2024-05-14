package com.aadhil.servlet;

import java.io.IOException;
import java.util.List;

import com.aadhil.ejb.dto.CargoDTO;
import com.aadhil.ejb.dto.RouteDTO;
import com.aadhil.ejb.entity.Terminal;
import com.aadhil.ejb.remote.DataFetchService;
import com.aadhil.util.Json;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "FetchData", value = {"/terminals", "/routes", "/cargos"})
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
        }

        response.getWriter().write(jsonString);
    }
}
