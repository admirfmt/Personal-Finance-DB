package personal.finance.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Long id;
    private final String description;
    private final double amount;
    private final String type;
    private final LocalDateTime date;

    public Transaction(Long id, String description, double amount, String type, LocalDateTime date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public Transaction(String description, double amount, String type, LocalDateTime date) {
        this(null, description, amount, type, date);
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String saveFileString() {
        return String.format("%s|%.2f|%s|%s", description, amount, type, date.format(DATE_FORMAT));
    }
}
