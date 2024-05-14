package com.aadhil.servlet;

import com.aadhil.ejb.remote.RouteService;
import jakarta.ejb.EJBException;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.io.PrintWriter;

class CreateRouteServletTest {
    @Mock
    private RouteService routeService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private JsonObject jsonObject;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private CreateRouteServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void doGetSuccess() throws Exception {
        // Test data
        String route = "Route 1";
        String terminals = "Sotuhampton, Amsterdam, Hamburg, Dubai, Jeddah";
        long daysForVoyage = 5L;

        when(request.getParameter("route")).thenReturn(route);
        when(request.getParameter("terminals")).thenReturn(terminals);
        when(routeService.createRoute(route, terminals)).thenReturn(daysForVoyage);
        when(jsonObject.toString()).thenReturn("{\"response\":\"success\",\"days\":5}");
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doGet(request, response);

        verify(routeService).createRoute(route, terminals);
        verify(response).setContentType("application/json");
        verify(response.getWriter()).write("{\"response\":\"success\",\"days\":5}");
        verify(printWriter).write("{\"response\":\"success\",\"days\":5}");
    }

    @Test
    void doGetError() throws Exception {
        // Test data
        String route = "Route 1";
        String terminals = "Sotuhampton, Amsterdam, Hamburg, Dubai, Jeddah";

        when(request.getParameter("route")).thenReturn(route);
        when(request.getParameter("terminals")).thenReturn(terminals);
        doThrow(new EJBException()).when(routeService).createRoute(route, terminals);
        when(response.getWriter()).thenReturn(printWriter);

        servlet.doGet(request, response);

        verify(routeService).createRoute(route, terminals);
        verify(response).setContentType("application/json");
        verify(response.getWriter()).write("{\"response\":\"error\"}");
    }
}