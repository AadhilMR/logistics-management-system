package com.aadhil.servlet;

import java.io.IOException;

import com.aadhil.controller.ContainerLoginController;
import com.aadhil.ejb.entity.User;
import com.aadhil.ejb.entity.UserType;
import com.aadhil.ejb.remote.LoginService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @EJB
    private LoginService loginService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            User user = loginService.login(username, password);

            if(user!=null) {
                ContainerLoginController.login(request, user.getUserType());
                response.sendRedirect("admin/dashboard.jsp");
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
