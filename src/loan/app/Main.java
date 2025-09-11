package loan.app;

import java.util.Scanner;
import loan.core.Borrower;
import loan.core.Lender;
import loan.core.LenderPortfolio;
import loan.core.Portfolio;
import loan.exceptions.EligibilityException;
import loan.models.SecuredLoan;
import loan.models.UnsecuredLoan;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Portfolio portfolio = new Portfolio();
        LenderPortfolio lenderPortfolio = new LenderPortfolio();
        boolean running = true;

        while(running){
            System.out.println("\n=== SwiftLoan Micro Lending System ===");
            System.out.println("1. Add Borrower");
            System.out.println("2. Apply for Loan");
            System.out.println("3. Make Repayment");
            System.out.println("4. View Borrower Statement");
            System.out.println("5. View Portfolio Summary");
            System.out.println("6. Add Lender");
            System.out.println("7. View Lender Statement");
            System.out.println("8. View All Lenders");
            System.out.println("9. Deposit to Lender");
            System.out.println("10. Withdraw from Lender");
            System.out.println("11. Exit");

            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1 -> {
                    Borrower borrower = new Borrower();
                    portfolio.addBorrower(borrower);
                    portfolio.savePortfolio();
                }

                case 2 -> {
                    System.out.print("Enter Borrower ID: ");
                    String borrowerId = sc.nextLine();
                    Borrower borrower = portfolio.getBorrower(borrowerId);
                    if(borrower == null){
                        System.out.println("Borrower not found!");
                        break;
                    }

                    System.out.print("Enter loan type (1 = Secured, 2 = Unsecured): ");
                    int type = sc.nextInt();
                    sc.nextLine();

                    try{
                        if(type == 1){
                            borrower.applyForLoan(new SecuredLoan());
                        }
                        else if(type == 2){
                            borrower.applyForLoan(new UnsecuredLoan());
                        }
                        else{
                            System.out.println("Invalid Loan type!");
                            break;
                        }
                        portfolio.savePortfolio();
                    }
                    catch(EligibilityException e){
                        System.out.println("Loan application failed: " + e.getMessage());
                    }

                    var loan = borrower.getLoans().get(borrower.getLoans().size() - 1);

                    System.out.println("Choose an option to assign loan to the borrower: ");
                    System.out.println("1. Manually assign loans: ");
                    System.out.println("2. Auto assign loans thorugh the system");

                    int fundingChoice = sc.nextInt();
                    sc.nextLine();

                    if(fundingChoice == 1){
                        while (true){ 
                            System.out.print("Enter Lender ID (or 'done' to finish): ");
                            String lenderId = sc.nextLine();
                            if(lenderId.equalsIgnoreCase("done")) break;

                            Lender lender = lenderPortfolio.getLender(lenderId);
                            if(lender == null){
                                System.out.println("Lender not found!");
                                continue;
                            }
                            System.out.print("Enter amount to invest: ");
                            double amt = sc.nextDouble();
                            sc.nextLine();

                            lender.investInLoan(loan, amt);
                            lenderPortfolio.saveLenderPortfolio();
                        }
                    }
                    else if(fundingChoice == 2){
                        var allLenders = lenderPortfolio.getLenders();
                        if(allLenders.isEmpty()){
                            System.out.println("No lenders available for auto assignment!");
                        }
                        else{
                            double amountPerLender = loan.getAmount() / allLenders.size();
                            boolean allCapable = allLenders.stream().allMatch(l -> l.getCapital() >= amountPerLender);
                            
                            if(!allCapable){
                                System.out.println("Auto-assingment failed: Not all lenders have enough capital!");
                            }
                            else{
                                for(Lender lender : allLenders){
                                    lender.investInLoan(loan, amountPerLender);
                                }
                                lenderPortfolio.saveLenderPortfolio();
                                System.out.println("Auto-assigned loan funding across all lenders!");
                            }
                        }
                    }
                }

                case 3 -> {
                    System.out.print("Enter Borrower ID: ");
                    String borrowerId = sc.nextLine();
                    Borrower borrower = portfolio.getBorrower(borrowerId);
                    if(borrower == null){
                        System.out.println("Borrower not found!");
                        break;
                    }

                    if(borrower.getLoans().isEmpty()){
                        System.out.println("This borrower has no active loans!");
                        break;
                    }

                    System.out.println("Loans for borrower: ");
                    for(var loan : borrower.getLoans()){
                        System.out.println(" -> " + loan.getLoanId() + " | Balance: " + loan.getOutstandingBalance());
                    }

                    System.out.print("Enter loan ID to repay: ");
                    String loanId = sc.nextLine();
                    var loan = borrower.getLoans().stream().filter(l -> l.getLoanId().equals(loanId)).findFirst().orElse(null);
                    if(loan == null){
                        System.out.println("No loan was found!");
                        break;
                    }

                    System.out.print("Enter repayment amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();

                    loan.makePayment(amount);
                    portfolio.savePortfolio();
                }

                case 4 -> {
                    System.out.print("Enter Borrower ID: ");
                    String borrowerId = sc.nextLine();
                    Borrower borrower = portfolio.getBorrower(borrowerId);
                    if(borrower == null){
                        System.out.println("Borrower not found!");
                    }
                    else{
                        borrower.printStatement();
                    }
                }

                case 5 -> {
                    portfolio.printPortfolioSummary();
                }

                case 6 -> {
                    Lender lender = new Lender();
                    lenderPortfolio.addLender(lender);
                    lenderPortfolio.saveLenderPortfolio();
                }

                case 7 -> {
                    System.out.print("Enter Lender ID: ");
                    String lenderId = sc.nextLine();
                    Lender lender = lenderPortfolio.getLender(lenderId);
                    if(lender == null){
                        System.out.println("Lender not found!");
                    }
                    else{
                        lender.printStatement();
                    }
                }

                case 8 -> {
                    lenderPortfolio.printPortfolioSummary();
                }

                case 9 -> {
                    System.out.print("Enter Lender ID: ");
                    String lenderId = sc.nextLine();
                    var lender = lenderPortfolio.getLender(lenderId);
                    if(lender == null){
                        System.out.println("Lender not found!");
                        break;
                    }
                    System.out.print("Enter deposit amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();
                    lender.deposit(amount);
                    lenderPortfolio.saveLenderPortfolio();
                }

                case 10 -> {
                    System.out.print("Enter Lender ID: ");
                    String lenderId = sc.nextLine();
                    var lender = lenderPortfolio.getLender(lenderId);
                    if(lender == null){
                        System.out.println("Lender not found!");
                        break;
                    }
                    System.out.print("Enter withdrawal amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();
                    lender.withdraw(amount);
                    lenderPortfolio.saveLenderPortfolio();
                }

                case 11 -> {
                    running = false;
                    System.out.println("Exiting SwiftLoan. Goodbye!");
                    portfolio.savePortfolio();
                    lenderPortfolio.saveLenderPortfolio();
                }

                default -> System.out.println("Invalid Choice, try again!");
            }
        }
        sc.close();
    }
}
