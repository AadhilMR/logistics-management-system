package com.aadhil.ejb.remote;

import jakarta.ejb.Remote;

@Remote
public interface RouteService {
    Long createRoute(String name, String terminals);
}
