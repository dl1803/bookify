package com.dl1803.post.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component // uitlity service class
public class DateTimeFormatter {

    //Function<Instant, String> : interface Function để khai báo 1 func với đầu vào là 1 instant đầu ra là 1 String
    Map<Long, Function<Instant, String>> strategyMap = new LinkedHashMap<>(); // cần giữ nguyên thứ tự add vào

    public DateTimeFormatter() {
        strategyMap.put(60L,this::formatInSeconds);
        strategyMap.put(3600L,this::formatInMinutes);
        strategyMap.put(86400L,this::formatInHours);
        strategyMap.put(Long.MAX_VALUE,this::formatInDates);

    }

    public String format(Instant instant){
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now()); // t được tạo đến t hiện tại

        var strategy = strategyMap.entrySet() // lấy toàn bộ cặp K-V, mỗi cặp là 1 Entry gom thành Set
                .stream() // lấy từng entry để xử lí
                // filter : giữ lại những ptu Entry thỏa điều kiện
                .filter(longFunctionEntry -> {
                    // longFunctionEntry.getKey() -> lấy key của Entry hiện tại
                    return elapseSeconds < longFunctionEntry.getKey(); // nếu t < key -> map vào value else tiếp tục stream tiếp key kế
                }).findFirst() // lấy ptu đầu tiên còn lại trong stream đã fil
                .get(); // trả về Optional<Entry> : nếu có -> Entry , nếu k thấy -> văng lỗi

        return strategy.getValue() // lấy val : Func đã ovr
                .apply(instant); // apply tức là hàm override trong interf Function, nó đưa input instant vào func yêu cầu để lấy kq String từ func đó
    }

    private String formatInSeconds(Instant instant){
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        return elapseSeconds + " seconds";
    }

    private String formatInMinutes(Instant instant){
        long elapseMinutes = ChronoUnit.MINUTES.between(instant, Instant.now());
        return elapseMinutes + " minutes";
    }

    private String formatInHours(Instant instant){
        long elapseMinutes = ChronoUnit.HOURS.between(instant, Instant.now());
        return elapseMinutes + " hours";
    }

    private String formatInDates(Instant instant){
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_DATE;
        return localDateTime.format(dateTimeFormatter);
    }

}

