package loan.core;

import java.util.ArrayList;
import java.util.List;

public class Portfolio{
    private List<Borrower> borrowers;
    
    public Portfolio(){
        this.borrowers = new ArrayList<>();
    }

    public void addBorrower(){
        Borrower borrower = new Borrower();
        borrowers.add(borrower);
        System.out.println("Borrower " + borrower.getName() + " added successfully!\n");
    }

    public void addBorrower(Borrower borrower){
        if(borrower == null){
            System.out.println("Cannot add null borrower!");
            return;
        }
        borrowers.add(borrower);
        System.out.println("Borrower " + borrower.getName() + " added successfully!\n");
    }

    public Borrower getBorrower(String borrowerId){
        if(borrowerId == null) return null;
        for(Borrower b : borrowers){
            if(b.getBorrowerId().equalsIgnoreCase(borrowerId)){
                return b;
            }
        }
        System.out.println("Borrower with ID " + borrowerId + " not found!");
        return null;
    }

    public Borrower findBorrowerById(String borrowerId){
        return getBorrower(borrowerId);
    }

    public void printPortfolioSummary(){
        if(borrowers.isEmpty()){
            System.out.println("\nNo borrowers in the system yet!");
            return;
        }
        System.out.println("\n=== Portfolio Summary ===");
        for(Borrower b : borrowers){
            b.printStatement();
        }
        System.out.println("==========================\n");
    }

    public void printAllBorrowers(){
        printPortfolioSummary();
    }

    public int getTotalBorrowers(){
        return borrowers.size();
    }
}