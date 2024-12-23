package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(String transactionID, String accountID, double transactionAmount,
                          LocalDateTime transactionDate, String location, Channel channel) {

    private  static final int DATE_ARG = 4;
    private  static final int FIELDS_COUNT = 6;
    private  static final int TRANSACTION = 3;
    private  static final int CHANNEL = 5;

    public static Transaction of(String line) {
        if (line == null || line.isBlank() || line.isEmpty()) {
            throw new IllegalArgumentException("string is null");
        }
        String[] fields = line.split(",");
        if (fields.length != FIELDS_COUNT) {
            throw new IllegalArgumentException("args are not 6");
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new Transaction(fields[0], fields[1], Double.parseDouble(fields[2])
                , LocalDateTime.parse(fields[TRANSACTION], format), fields[DATE_ARG]
                , Channel.valueOf(fields[CHANNEL].toUpperCase()));
    }
}
