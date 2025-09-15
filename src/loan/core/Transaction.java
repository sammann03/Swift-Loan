package loan.core;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable{
    private static final long serialVersionUID = 1L;

    private LocalDateTime timestamp;
    private String type;
    private double amount;
    private double balanceAfter;
    private String note;
    private String borrowerId;
    private String lenderId;
    private String loanId;

    public Transaction(String type, double amount, double balanceAfter, String note){
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.note = note == null ? "" : note;
        this.borrowerId = null;
        this.lenderId = null;
        this.loanId = null;
    }

    public Transaction(String type, double amount, double balanceAfter, String note, String borrowerId, String lenderId, String loanId){
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.note = note == null ? "" : note;
        this.borrowerId = borrowerId;
        this.lenderId = lenderId;
        this.loanId = loanId;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    public String getType(){
        return type;
    }

    public double getAmount(){
        return amount;
    }

    public double getBalanceAfter(){
        return balanceAfter;
    }

    public String getNote(){
        return note;
    }

    public String getBorrowerId(){
        return borrowerId;
    }

    public String getLenderId(){
        return lenderId;
    }

    public String getLoanId(){
        return loanId;
    }

    @Override
    public String toString(){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String ts = timestamp.format(fmt);
        String amt = String.format("%,.2f", amount);
        String bal = String.format("%,.2f", balanceAfter);
        String sign = amount >= 0 ? "+" : "";
        return "[" + ts + "] " + type + " " + sign + amt + "  Balance: " + bal
                + (note.isEmpty() ? "" : "  — " + note);
    }
}