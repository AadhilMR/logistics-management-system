package com.aadhil.ejb.interceptor;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import com.aadhil.ejb.entity.Terminal;
import com.aadhil.ejb.impl.RouteServiceBean;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

public class CalculateTimeInterceptor {
    private static final double EARTH_RADIUS_NM = 3440.065;
    private static final double NAUTICAL_MILE_IN_KM = 1.85200;
    private static final double AVERAGE_SPEED_KMPH = 35;
    private static final double HOURS_IN_DAY = 24;
    private static final int MONTH_COUNT = 12;
    private static final int DAY_COUNT = 28;
    private static final int HOUR_COUNT = 24;

    @AroundInvoke
    public Object calculate(InvocationContext invocationContext) throws Exception {
        RouteServiceBean routeServiceBean = (RouteServiceBean) invocationContext.getTarget();

        List<Terminal> terminalList = routeServiceBean.getTerminalList();

        Terminal origin = terminalList.get(0);
        Terminal destination = terminalList.get(terminalList.size()-1);

        double distance = calculateDistance(origin.getLatitude(), origin.getLongitude(),
                destination.getLatitude(), destination.getLongitude());

        int durationInDays = calculateDuration(distance);

        LocalDateTime departureTime = getDepartureTime();
        LocalDateTime arrivalTime = getArrivalTime(departureTime, durationInDays);

        routeServiceBean.setDepartureTime(departureTime);
        routeServiceBean.setArrivalTime(arrivalTime);

        return invocationContext.proceed();
    }

    private double calculateDistance(double lat1, double long1, double lat2, double long2) {
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        long1 = Math.toRadians(long1);
        long2 = Math.toRadians(long2);

        double dlat = lat2 - lat1;
        double dlong = long2 - long1;

        double a = StrictMath.pow(StrictMath.sin(dlat / 2), 2)
                + StrictMath.cos(lat1) * StrictMath.cos(lat2) * StrictMath.pow(StrictMath.sin(dlong / 2), 2);

        double c = 2 * StrictMath.atan2(StrictMath.sqrt(a), StrictMath.sqrt(1 - a));

        return EARTH_RADIUS_NM * c;
    }

    private int calculateDuration(double distance) {
        double distanceInKm = distance * NAUTICAL_MILE_IN_KM;
        double durationInHours = distanceInKm / AVERAGE_SPEED_KMPH;
        double durationInDays = durationInHours / HOURS_IN_DAY;

        int actualDays = (int) StrictMath.ceil(durationInDays);
        return actualDays;
    }

    private LocalDateTime getDepartureTime() {
        SecureRandom random = new SecureRandom();

        int month = random.nextInt(MONTH_COUNT) + 1;
        int date = random.nextInt(DAY_COUNT) + 1;
        int hour = random.nextInt(HOUR_COUNT);

        int[] possibleMinutes = {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55};
        int minuteIndex = random.nextInt(possibleMinutes.length);
        int minute = possibleMinutes[minuteIndex];

        return LocalDateTime.of(LocalDateTime.now().getYear(), month, date, hour, minute);
    }

    private LocalDateTime getArrivalTime(LocalDateTime departureTime, int days) {
        SecureRandom random = new SecureRandom();

        int[] possibleMinutes = {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55};
        int minuteIndex = random.nextInt(possibleMinutes.length);

        int hours = random.nextInt(HOUR_COUNT);
        int minutes = possibleMinutes[minuteIndex];

        return departureTime.plusDays(days).plusHours(hours).plusMinutes(minutes);
    }
}
