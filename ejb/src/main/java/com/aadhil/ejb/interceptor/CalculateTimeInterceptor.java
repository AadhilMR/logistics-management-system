package com.aadhil.ejb.interceptor;

import java.time.LocalDateTime;
import java.util.List;

import com.aadhil.ejb.entity.Terminal;
import com.aadhil.ejb.impl.RouteServiceBean;
import com.aadhil.ejb.util.NauticalCalculator;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

public class CalculateTimeInterceptor {
    @AroundInvoke
    public Object calculate(InvocationContext invocationContext) throws Exception {
        RouteServiceBean routeServiceBean = (RouteServiceBean) invocationContext.getTarget();
        List<Terminal> terminalList = routeServiceBean.getTerminalList();

        NauticalCalculator calculator = new NauticalCalculator();

        double distance = 0.0;

        for(int i=0; i<terminalList.size()-1; i++) {
            Terminal origin = terminalList.get(i);
            Terminal destination = terminalList.get(i+1);

            distance += calculator.calculateDistance(origin.getLatitude(), origin.getLongitude(),
                    destination.getLatitude(), destination.getLongitude());
        }

        // The voyage duration is calculated in days
        // The days count is the sum of duration for voyage and terminal count
        // Assumption: the vessel stops a day in each terminal
        int durationInDays = calculator.calculateDuration(distance) + terminalList.size();

        LocalDateTime departureTime = calculator.getDepartureTime();
        LocalDateTime arrivalTime = calculator.getArrivalTime(departureTime, durationInDays);

        routeServiceBean.setDepartureTime(departureTime);
        routeServiceBean.setArrivalTime(arrivalTime);

        return invocationContext.proceed();
    }
}
