package com.aadhil.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import com.aadhil.ejb.remote.CargoService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/createCargo")
public class CreateCargoServlet extends HttpServlet {

    @EJB
    private CargoService cargoService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("desc");
        String instruction = request.getParameter("ins");

        // Decode parameter
        instruction = URLDecoder.decode(instruction, StandardCharsets.UTF_8);

        try {
            cargoService.createCargo(description, instruction);
            response.getWriter().write("success");
        } catch (Exception e) {
            response.getWriter().write("error");
        }
    }
}
