package com.devteria.post.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DateTimeFormatterCustom {

    Map<Long, Function<Instant, String>> strategyMap = new LinkedHashMap<>();

    public DateTimeFormatterCustom() {
        strategyMap.put(60L, this::formatSeconds);
        strategyMap.put(3600L, this::formatMinutes);
        strategyMap.put(86400L, this::formatHour);
        strategyMap.put(Long.MAX_VALUE, this::formatSeconds);
    }

    private String formatSeconds(Instant instant) {
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());

        return elapseSeconds + " seconds";
    }

    private String formatMinutes(Instant instant) {
        long elapseSeconds = ChronoUnit.MINUTES.between(instant, Instant.now());

        return elapseSeconds + " minutes";
    }

    private String formatHour(Instant instant) {
        long elapseSeconds = ChronoUnit.HOURS.between(instant, Instant.now());

        return elapseSeconds + " hours";
    }

    public String formatInDate(Instant instant) {

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime to the desired string format
        return localDateTime.format(formatter);

    }

    public String format(Instant instant) {

        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        var strategyFilter =
                strategyMap.entrySet().stream().filter(val -> elapseSeconds < val.getKey()).findFirst();

        if (strategyFilter.isPresent()) {
            var strategy = strategyFilter.get();
            return strategy.getValue().apply(instant);
        }

        return "";

    }

}
