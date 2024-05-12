package com.aadhil.servlet;

import java.io.IOException;
import java.util.List;

import com.aadhil.ejb.entity.Terminal;
import com.aadhil.ejb.remote.DataFetchService;
import com.aadhil.util.Json;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "FetchData", value = {"/terminals"})
public class FetchDataServlet extends HttpServlet {

    @EJB
    private DataFetchService dataFetchService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        List<Terminal> terminalList = dataFetchService.fetchTerminals();

        Json json = new Json();
        String jsonString = json.getJsonString(terminalList);

        response.getWriter().write(jsonString);
    }
}
