package loan.app;

import java.util.Scanner;
import loan.core.Borrower;
import loan.core.Portfolio;
import loan.exceptions.EligibilityException;
import loan.models.SecuredLoan;
import loan.models.UnsecuredLoan;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Portfolio portfolio = new Portfolio();
        boolean  running = true;

        while(running){
            System.out.println("\n=== SwiftLoan Micro Lending System ===");
            System.out.println("1. Add Borrower");
            System.out.println("2. Apply for Loan");
            System.out.println("3. Make Repayment");
            System.out.println("4. View Borrower Statement");
            System.out.println("5. View Portfolio Summary");
            System.out.println("6. Exit");

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

                    try {
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
                    running = false;
                    System.out.println("Exiting SwiftLoan. Goodbye!");
                    // ✅ Save before exiting
                    portfolio.savePortfolio();
                }
                default -> System.out.println("Invalid Choice, try again!");
            }
        }
        sc.close();
    }
}
