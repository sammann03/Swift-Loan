package loan.models;

import loan.utils.IDGenerator;
import loan.core.Repayment;

import java.nio.ReadOnlyBufferException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Loan{
    protected String loanId;
    protected double amount;
    protected double interestRate;
    protected int tenure;
    protected double outstandingBalance;
    protected String status;
    protected List<Repayment> repaymentSchedule;
    protected List<Repayment> repayments;
    protected LocalDate sanctionDate;

    public Loan(){
        Scanner sc = new Scanner(System.in);

        this.loanId = IDGenerator.generateLoanId();
        System.out.println("Creating Loan ID: " + loanId);

        System.out.print("Enter loan amount: ");
        this.amount = sc.nextDouble();

        System.out.print("Enter annual interest (in %): ");
        this.interestRate = sc.nextDouble();

        System.out.print("Enter tenure (in months): ");
        this.tenure = sc.nextInt();

        this.outstandingBalance = amount;
        this.status = "Active";
        this.sanctionDate = LocalDate.now();
        this.repaymentSchedule = new ArrayList<>();
        this.repayments = new ArrayList<>();
    }

    public String getLoanId(){
        return loanId;
    }

    public double getAmount(){
        return amount;
    }

    public double getInterestRate(){
        return interestRate;
    }

    public int getTenure(){
        return tenure;
    }

    public double getOutstandingBalance(){
        return outstandingBalance;
    }

    public String getStatus(){
        return status;
    }

    public List<Repayment> getRepaymentSchedule(){
        return repaymentSchedule;
    }

    public List<Repayment> getRepayments(){
        return repayments;
    }

    public LocalDate getSanctionDate(){
        return sanctionDate;
    }

    public abstract void calculateEMI();
    public abstract void generateRepaymentSchedule();

    public void makePayment(double amountPaid){
        if(amountPaid > outstandingBalance){
            System.out.println("Payment amount exceeds oustanding balance! Adjusting to: " + outstandingBalance);
            amountPaid = outstandingBalance;
            return;
        }

        Repayment repayment = new Repayment(outstandingBalance);
        repayments.add(repayment);

        outstandingBalance -= amountPaid;
        System.out.println("Payment of " + amountPaid + " made. Remaining balance is: " + outstandingBalance);
        if(outstandingBalance <= 0){
            status = "Closed";
            System.out.println("Loan " + loanId + " is fully repaid!");
        }
    }

    public void closeLoan(){
        outstandingBalance = 0;
        status = "Closed";
        System.out.println("Loan " + loanId + " is fully repaid and loan is now closed!");
    }

    @Override
    public String toString(){
        return "Loan{" +
                "loanId='" + loanId + '\'' +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", tenure=" + tenure +
                ", outstandingBalance=" + outstandingBalance +
                ", status='" + status + '\'' +
                ", sanctionDate=" + sanctionDate +
                "}";
    }
}