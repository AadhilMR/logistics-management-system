package com.aadhil.ejb.util;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrackingIDGenerator {
    private static final String TRACKING_ID_PATTERN = "[A-Z]{3}[0-9]{4}[A-Z]{1}";
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int MAXIMUM_CHAR_COUNT = 52;
    private static final int MAXIMUM_SINGLE_NUMBER = 10;

    public String generateTrackingId() {
        StringBuilder trackingId;
        SecureRandom random = new SecureRandom();
        Pattern pattern = Pattern.compile(TRACKING_ID_PATTERN);

        do {
            trackingId = new StringBuilder();

            for (int i = 0; i < 8; i++) {
                if(i<3 || i==7) {
                    trackingId.append(ALPHABET.charAt(random.nextInt(MAXIMUM_CHAR_COUNT)));
                } else {
                    trackingId.append(random.nextInt(MAXIMUM_SINGLE_NUMBER));
                }
            }
        } while (!isValidTrackingId(trackingId.toString(), pattern));

        return trackingId.toString();
    }

    private boolean isValidTrackingId(String trackingId, Pattern pattern) {
        Matcher matcher = pattern.matcher(trackingId);
        return matcher.matches();
    }
}
