package loan.models;

import loan.core.Repayment;
import loan.utils.IDGenerator;

import java.time.LocalDate;
import java.util.Scanner;

public class UnsecuredLoan extends Loan{
    private String purpose;
    private double emi;

    public UnsecuredLoan(){
        super();
        Scanner sc = new Scanner(System.in);
        sc.nextLine();

        System.out.print("Enter purpose of this loan (e.g. education, personal, medical): ");
        this.purpose = sc.nextLine();

        if(this.interestRate < 8.0){
            System.out.println("Unsecured loans require higher interest rates. Adjusting to minimum 8%.");
            this.interestRate = 8.0;
        }

        calculateEMI();
        generateRepaymentSchedule();
    }

    @Override
    public void calculateEMI(){
        double monthlyRate = (interestRate / 100.0) / 12.0;
        if(tenure <= 0) emi = round(amount);
        else if(monthlyRate == 0) emi = round(amount / tenure);
        else{
            double r = monthlyRate;
            int n = tenure;
            double factor = Math.pow(1 + r, n);
            double raw = (amount * r * facotr) / (factor - 1);
            emi = round(raw);
        }
        System.out.println("Calculated monthly EMI (Unsecured): " + emi);
    }

    @Override
    public void generateRepaymentSchedule(){
        repaymentSchedule.clear();
        LocalDate base = this.sanctionDate;
        double remaining = round(amount);

        for (int i = 1; i <= tenure; i++){
            LocalDate due = base.plusMonths(i);
            double payment;
            if(i == tenure) payment = round(remaining);
            else{
                payment = emi;
                remaining = round(remaining - emi);
            }
            repaymentSchedule.add(new Repayment(due, payment));
        }
        System.out.println("Generated Repayment Schedule (" + repaymentSchedule.size() + " months).");
    }

    private double round(double value){
        return Math.round(value * 100.0) / 100.0;
    }

    @Override
    public String toString(){
        return "UnsecuredLoan{" +
                "loanId='" + loanId + '\'' +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", tenure=" + tenure +
                ", emi=" + emi +
                ", purpose='" + purpose + '\'' +
                ", outstandingBalance=" + outstandingBalance +
                ", status='" + status + '\'' +
                ", sanctionDate=" + sanctionDate +
                '}';
    }
}