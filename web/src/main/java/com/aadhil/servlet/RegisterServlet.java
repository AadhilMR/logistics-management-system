package com.aadhil.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import com.aadhil.dto.AuthDTO;
import com.aadhil.ejb.remote.RegisterService;
import com.aadhil.util.Json;
import jakarta.ejb.EJB;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    @EJB
    private RegisterService registerService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter printWriter = response.getWriter();

        try {
            Json json = new Json();
            AuthDTO authDTO = json.getData(request.getReader(), AuthDTO.class);

            String username = authDTO.getUsername();
            String password = authDTO.getPassword();
            String userTypeId = authDTO.getUsertype();

            registerService.register(username, password, Integer.parseInt(userTypeId));

            printWriter.print("success");
        } catch (IOException | NumberFormatException ex) {
            printWriter.print("error");
        }
    }
}
