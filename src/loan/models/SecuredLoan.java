package loan.models;

import java.time.LocalDate;
import java.util.Scanner;
import loan.core.Loan;
import loan.core.Repayment;

public class SecuredLoan extends Loan{
    private String collateralDescription;
    private double collateralValue;
    private double emi;

    public SecuredLoan(){
        super();
        Scanner sc = new Scanner(System.in);
        sc.nextLine();

        System.out.print("Enter Collateral description (e.g. house, vehicle, jewellery): ");
        this.collateralDescription = sc.nextLine();

        System.out.print("Enter calculated collateral value: ");
        this.collateralValue = sc.nextDouble();

        if(collateralValue >= 0.75 * this.amount){
            System.out.println("High value colateral detected - applying 0.5% interest concession");
            this.interestRate = Math.max(0.0, this.interestRate - 0.5);
        }

        calculateEMI();
        generateRepaymentSchedule();
    }

    @Override
    public void calculateEMI(){
        double monthlyRate = (interestRate / 100.0) / 12.0;
        if(tenure <= 0) emi = round(amount);
        else if(monthlyRate == 0) emi = round(amount/tenure);
        else{
            double r = monthlyRate;
            int n = tenure;
            double factor = Math.pow(1 + r, n);
            double raw = (amount * r * factor) / (factor - 1);
            emi = round(raw);
        }
        System.out.println("Calculated monthly income: " + emi);
    }

    @Override
    public void generateRepaymentSchedule(){
        repaymentSchedule.clear();
        LocalDate base = this.sanctionDate;
        double remaining = round(amount);

        for(int i = 0; i <= tenure; i++){
            LocalDate due = base.plusMonths(i);
            double payment;
            if(i == tenure){
                payment = round(remaining);
            }
            else{
                payment = emi;
                remaining = round(remaining - emi);
            }
            repaymentSchedule.add(new Repayment(due, payment));
        }

        System.out.println("Generated Repayment Schedule: (" + repaymentSchedule.size() + " months).");
    }

    private double round(double value){
        return Math.round(value * 100.0) / 100.0;
    }

    @Override
    public String toString(){
        return "SecuredLoan{" +
                "loanId='" + loanId + '\'' +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", tenure=" + tenure +
                ", emi=" + emi +
                ", collateral='" + collateralDescription + '\'' +
                ", collateralValue=" + collateralValue +
                ", outstandingBalance=" + outstandingBalance +
                ", status='" + status + '\'' +
                ", sanctionDate=" + sanctionDate +
                '}';
    }
}