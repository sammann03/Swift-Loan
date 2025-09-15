package loan.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import loan.utils.IDGenerator;

public abstract class Loan implements Serializable{
    private static final long serialVersionUID = 1L;

    protected String loanId;
    protected double amount;
    protected double interestRate;
    protected int tenure;
    protected double outstandingBalance;
    protected String status;
    protected List<Repayment> repaymentSchedule;
    protected List<Repayment> repayments;
    protected LocalDate sanctionDate;
    protected List<LenderFunding> lenderFundings;
    protected double riskLevel;

    public Loan(){
        Scanner sc = new Scanner(System.in);

        this.loanId = IDGenerator.generateLoanId();
        System.out.println("Creating Loan ID: " + loanId);

        System.out.print("Enter loan amount: ");
        String loanAmount = sc.nextLine().replace(",", "");
        this.amount = Double.parseDouble(loanAmount);

        System.out.print("Enter annual interest (in %): ");
        this.interestRate = sc.nextDouble();
        sc.nextLine();

        System.out.print("Enter tenure (in months): ");
        this.tenure = sc.nextInt();
        sc.nextLine();

        this.outstandingBalance = amount;
        this.status = "Active";
        this.sanctionDate = LocalDate.now();
        this.repaymentSchedule = new ArrayList<>();
        this.repayments = new ArrayList<>();
        this.lenderFundings = new ArrayList<>();
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

    public double getRiskLevel(){
        return riskLevel;
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

    public List<LenderFunding> getLenderFundings(){
        return lenderFundings;
    }

    public static class LenderFunding implements Serializable{
        private String loanId;
        public String lenderId;
        public double amount;
        public LenderFunding(String lenderId, double amount){
            this.lenderId = lenderId;
            this.amount = amount;
        }

        public String getLoanId(){
            return loanId;
        }

        public String getLenderId(){
            return lenderId;
        }

        public double getAmountFunded(){
            return amount;
        }

        public void addAmount(double delta){
            this.amount += delta;
        }

        @Override
        public String toString(){
            return "{loanId=" + loanId + ", lenderId=" + lenderId + ", amount=" + amount + "}";
        }
    }

    public void addFunding(LenderFunding funding){
        if(funding == null) return;
        for(LenderFunding lf : lenderFundings){
            if(lf.getLenderId().equals(funding.getLenderId())){
                lf.addAmount(funding.getAmountFunded());
                return;
            }
        }
        lenderFundings.add(funding);
    }

    public abstract void calculateEMI();
    public abstract void generateRepaymentSchedule();

    public void makePayment(double amountPaid){
        if(amountPaid > outstandingBalance){
            System.out.println("Payment amount exceeds oustanding balance! Adjusting to: " + outstandingBalance);
            amountPaid = outstandingBalance;
        }

        double monthlyRate = (interestRate / 100) / 12;
        double interestComponent = outstandingBalance * monthlyRate;
        double principalComponent = amountPaid - interestComponent;
        if(principalComponent < 0) principalComponent = 0;

        if(amountPaid < interestComponent){
            System.out.println("Payment is too small! It doesn't even cover monthly interest.");
            return;
        }

        Repayment repayment = new Repayment(outstandingBalance);
        repayment.setPrincipalComponent(principalComponent); 
        repayment.setInterestComponent(interestComponent);
        repayments.add(repayment);
        outstandingBalance -= principalComponent;

        double totalFunded = lenderFundings.stream().mapToDouble(LenderFunding::getAmountFunded).sum();
        for(LenderFunding lf : lenderFundings){
            Lender lender = LenderPortfolio.findLenderById(lf.lenderId);
            if(lender != null && totalFunded > 0){
                double proportion = lf.amount / totalFunded;
                double principalShare = principalComponent * proportion;
                double interestShare = interestComponent * proportion;

                lender.addEarnings(interestShare);
                lender.reduceInvestment(principalShare);
                lf.amount -= principalShare;
                lender.updateLoanFunding(loanId, -principalShare);
            }
        }

        System.out.println("Payment of " + amountPaid + " made [Principal: " + principalComponent + ", Interest: " + interestComponent + "]. Remaining balance is: " + outstandingBalance);

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
        StringBuilder sb = new StringBuilder();
        sb.append("Loan{loanId='").append(loanId)
        .append("', amount=").append(amount)
        .append(", interestRate=").append(interestRate)
        .append(", tenure=").append(tenure)
        .append(", outstandingBalance=").append(outstandingBalance)
        .append(", status='").append(status)
        .append("', sanctionDate=").append(sanctionDate)
        .append(", lenderFundings=[");
        for(LenderFunding lf : lenderFundings){
            sb.append("{lenderId=").append(lf.lenderId)
            .append(", amount=").append(lf.amount).append("}, ");
        }
        sb.append("]}");
        return sb.toString();
    } 
}