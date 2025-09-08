package loan.core;

import java.time.LocalDate;
import java.util.Scanner;

public class Repayment{
    private String repaymentId;
    private LocalDate date;
    private double amount;
    private String status;

    public Repayment(double outstandingBalance){
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter repayment ID (or press Enter for auto-generation): ");
        String inputId = sc.nextLine();
        this.repaymentId = inputId.isEmpty()
                ? "R -> " + System.currentTimeMillis()
                : inputId;

        System.out.print("Enter repayment amount (Outstanding: " + outstandingBalance + "): ");
        this.amount = sc.nextDouble();
        sc.nextLine();

        this.date = LocalDate.now();

        System.out.print("Enter repayment status (ON TIME/ LATE/ PARTIAL): ");
        this.status = sc.nextLine().trim().toUpperCase();
    }

    public Repayment(LocalDate dueDate, double amount){
        this.repaymentId = "R -> " + System.currentTimeMillis();
        this.date = dueDate;
        this.amount = amount;
        this.status = "Pending";
    }

    public String getRepaymentId(){
        return repaymentId;
    }

    public LocalDate getDate(){
        return date;
    }

    public double getAmount(){
        return amount;
    }

    public String getStatus(){
        return status;
    }

    @Override
    public String toString(){
        return "Repayment{" +
                "repaymentId='" + repaymentId + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}