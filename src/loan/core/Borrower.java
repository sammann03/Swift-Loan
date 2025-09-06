package loan.core;

import loan.models.Loan;
import loan.exceptions.EligibilityException;
import loan.utils.IDGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Borrower{
    private String borrowerId;
    private String name;
    private String address;
    private String phoneNumber;
    private double monthlyIncome;
    private int creditScore;
    private LocalDate registrationDate;
    private List<Loan> loans;

    private static final double max_debt_income_ratio = 0.5;

    public Borrower(){
        Scanner sc = new Scanner(System.in);
        this.borrowerId = IDGenerator.generateBorrowerId();
        System.out.println("Creating Borrower ID: " + borrowerId);

        System.out.print("Enter the name of the borrower: ");
        this.name = sc.nextLine();

        System.out.print("Enter the address: ");
        this.address = sc.nextLine();

        System.out.print("Enter the phone number: ");
        this.phoneNumber = sc.nextLine();

        System.out.print("Enter the monthly income: ");
        this.monthlyIncome = sc.nextDouble();

        System.out.print("Enter the credit score (300-900): ");
        this.creditScore = sc.nextInt();

        this.registrationDate = LocalDate.now();
        this.loans = new ArrayList<>();
    }

    public String getBorrowerId(){
        return borrowerId;
    }

    public String getName(){
        return name;
    }

    public double getMonthlyIncome(){
        return monthlyIncome;
    }

    public int getCreditScore(){
        return creditScore;
    }

    public List<Loan> getLoans(){
        return loans;
    }


    public void applyForLoan(Loan loan) throws EligibilityException {
        if(!isEligibleForLoan(loan)){
            throw new EligibilityException("Borrower " + name + " is not eligible for loan " + loan.getLoanId());
        }
        loans.add(loan);
        System.out.println("Loan " + loan.getLoanId() + " approved for " + name);
    }

    public boolean isEligibleForLoan(Loan loan){
        double totalDebt = calculateTotalDebt() + loan.getAmount();
        double ratio = totalDebt/(monthlyIncome * 12);
        return creditScore >= 600 && ratio <= max_debt_income_ratio;
    }

    public double calculateTotalDebt(){
        return loans.stream().mapToDouble(Loan::getOutstandingBalance).sum();
    }

    public void printStatement(){
        System.out.println("\n--- Borrower Statement ---");
        System.out.println("ID: " + borrowerId);
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Phone number: " + phoneNumber);
        System.out.println("Monthly Income: " + monthlyIncome);
        System.out.println("Credit Score: " + creditScore);
        System.out.println("Registration Date: " + registrationDate);
        System.out.println("Total debt: " + calculateTotalDebt());
        System.out.println("Loans: ");
        if(loans.isEmpty()){
            System.out.println("No loans yet!");
        }
        else{
            for(Loan loan : loans){
                System.out.println(" -> " + loan);
            }
        }
        System.out.println("------------------------------\n");
    }
}