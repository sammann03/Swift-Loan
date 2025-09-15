package loan.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import loan.utils.IDGenerator;

public class Lender implements Serializable{
    private String lenderId;
    private String name;
    private String phoneNumber;
    private String email;
    private double capital;
    private double investedAmount = 0.0;
    private double capitalDeposited = 0.0;
    private double capitalInvested = 0.0;
    private double interestEarned = 0.0;
    private List<Transaction> transactions = new ArrayList<>();
    private List<LoanFunding> loanFundings = new ArrayList<>();
    private double riskAppetite;
    private double maxExposurePercent;

    public static class LoanFunding implements Serializable{
        private static final long serialVersionUID = 1L;
        private String loanId;
        private double amount;

        public LoanFunding(String loanId, double amount){
            this.loanId = loanId;
            this.amount = amount;
        }

        public String getLoanId(){
            return loanId;
        }

        public double getAmountFunded(){
            return amount;
        }

        public void addAmount(double delta){
            this.amount += delta;
        }
    }

    public Lender(){
        Scanner sc = new Scanner(System.in);
        this.lenderId = IDGenerator.generateLenderId();
        System.out.print("Enter lender name: ");
        this.name = sc.nextLine();

        System.out.print("Enter the email: ");
        this.email = sc.nextLine();

        System.out.print("Enter the contact number: ");
        this.phoneNumber = sc.nextLine();

        System.out.print("Enter available capital: ");
        this.capital = sc.nextDouble();
        sc.nextLine();

        this.capitalDeposited = this.capital;
        transactions.add(new Transaction("DEPOSIT", this.capital, this.capital, "Initial deposit"));
    }

    public String getLenderId(){
        return lenderId;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public double getCapital(){
        return capital;
    }

    public double getInvestedAmount(){
        return investedAmount;
    }

    public double getCapitalDeposited(){
        return capitalDeposited;
    }

    public double getCapitalInvested(){
        return capitalInvested;
    }

    public double getInterestEarned(){
        return interestEarned;
    }

    public double getRiskAppetite(){
        return riskAppetite;
    }

    public double getMaxExposurePercent(){
        return maxExposurePercent;
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }

    public List<LoanFunding> getLoanFundings(){
        return loanFundings;
    }

    public void updateCapital(double amount){
        this.capital += amount;
    }

    public void deposit(double amount){
        if(amount <= 0){
            System.out.println("Deposit amount must be positive!");
            return;
        }
        this.capital += amount;
        this.capitalDeposited += amount;
        transactions.add(new Transaction("DEPOSIT", +amount, this.capital, "Deposit"));
        System.out.println("Deposited: " + amount + ". Updated capital balance: " + capital);
    }

    public void withdraw(double amount){
        if(amount <= 0){
            System.out.println("Withdrawal amount must be positive!");
            return;
        }
        if(amount > capital){
            System.out.println("Insufficient funds! Available balance: " + capital);
            return;
        }
        this.capital -= amount;
        transactions.add(new Transaction("WITHDRAWAL", -amount, this.capital, "Withdraw"));
        System.out.println("Withdrawn amount: " + amount + ". Remaining capital: " + capital);
    }

    public void investInLoan(Loan loan, double amount){
        if(amount > capital){
            System.out.println("Insufficient funds to invest!");
            return;
        }
        this.capital -= amount;
        this.investedAmount += amount;
        this.capitalInvested += amount;
        loan.addFunding(new Loan.LenderFunding(this.lenderId, amount));
        boolean updated = false;
        for(LoanFunding lf : loanFundings){
            if(lf.getLoanId().equals(loan.getLoanId())){
                lf.addAmount(amount);
                updated = true;
                break;
            }
        }
        if(!updated){
            loanFundings.add(new LoanFunding(loan.getLoanId(), amount));
        } 
        transactions.add(new Transaction("INVESTMENT", -amount, this.capital, "Investment in Loan " + loan.getLoanId()));
        System.out.println("Lender " + name + " invested " + amount + " into loan " + loan.getLoanId());
    }

    public void addEarnings(double interest){
        this.interestEarned += interest;
        this.capital += interest;
        transactions.add(new Transaction("INTEREST", +interest, this.capital, "Interest credited"));
    }

    public void updateLoanFunding(String loanId, double delta){
        for(int i = 0; i < loanFundings.size(); i++){
            LoanFunding lf = loanFundings.get(i);
            if(lf.getLoanId().equals(loanId)){
                lf.addAmount(delta);
                if(lf.getAmountFunded() <= 0) loanFundings.remove(i);
                return;
            }
        }
    }

    public void reduceInvestment(double principal){
        this.investedAmount -= principal;
        if(this.investedAmount < 0) this.investedAmount = 0;
        this.capital += principal;
        transactions.add(new Transaction("PRINCIPAL_RETURN", +principal, this.capital, "Principal returned"));
    }

    public void printStatement(){
        System.out.println("\n--- Lender Statement ---");
        System.out.println("ID: " + lenderId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Available Capital: " + capital);
        System.out.println("Invested Amount: " + investedAmount);
        System.out.println("Total Capital Invested (lifetime): " + capitalInvested);
        System.out.println("Interest Earned: " + interestEarned);

        if(!loanFundings.isEmpty()){
            System.out.println("\nActive Loan Fundings:");
            for(LoanFunding f : loanFundings){
                System.out.println("   Loan " + f.getLoanId() + " funded: " + f.getAmountFunded());
            }
        }

        System.out.println("-------------------------\n");

        System.out.println("Recent Transactions (latest first: )");
        int start = Math.max(0, transactions.size() - 20);
        for(int i = transactions.size() - 1; i >= start; i--){
            System.out.println("   " + transactions.get(i));
        }
        System.out.println("-------------------------\n");
    }

    public void printPassbook(){
        System.out.println("\n=== FULL PASSBOOK for " + name + " (" + lenderId + ") ===");
        for(Transaction t : transactions){
            System.out.println(t.toString());
        }
        System.out.println("============================================\n");
    }
}

