package com.aadhil.servlet;

import java.io.IOException;

import com.aadhil.ejb.remote.RouteService;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/createRoute")
public class CreateRouteServlet extends HttpServlet {

    @EJB
    private RouteService routeService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String route = request.getParameter("route");
        String terminals = request.getParameter("terminals");

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        try {
            Long daysForVoyage = routeService.createRoute(route, terminals);

            objectBuilder.add("response", "success");
            objectBuilder.add("days", daysForVoyage);
        } catch (Exception ex) {
            objectBuilder.add("response", "error");
        } finally {
            JsonObject jsonObject = objectBuilder.build();
            response.getWriter().write(jsonObject.toString());
        }
    }
}
