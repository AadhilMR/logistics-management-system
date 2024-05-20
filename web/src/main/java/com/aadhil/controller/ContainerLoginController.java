package com.aadhil.controller;

import com.aadhil.ejb.entity.UserType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class ContainerLoginController {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin0000";
    private static final String SUPERVISOR_USERNAME = "supervisor";
    private static final String SUPERVISOR_PASSWORD = "supervisor1234";
    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWORD = "user1234";

    private ContainerLoginController() {
    }

    public static void login(HttpServletRequest request, UserType userType) throws ServletException {
        String username = "";
        String password = "";

        switch (userType) {
            case ADMIN:
                username = ADMIN_USERNAME;
                password = ADMIN_PASSWORD;
                break;
            case SUPERVISOR:
                username = SUPERVISOR_USERNAME;
                password = SUPERVISOR_PASSWORD;
                break;
            case USER:
                username = USER_USERNAME;
                password = USER_PASSWORD;
                break;
        }

        request.login(username, password);
    }
}
