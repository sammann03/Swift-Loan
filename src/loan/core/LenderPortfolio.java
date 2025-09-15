package loan.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LenderPortfolio {
    private static List<Lender> lenders;

    public LenderPortfolio(){
        List<Lender> loaded = StorageManager.loadData("lenders.dat");
        if(loaded != null) lenders = loaded;
        else lenders = new ArrayList<>();
    }

    public List<Lender> getLenders(){
        return lenders;
    }

    public List<Lender> getAllLenders(){
        return this.lenders;
    }

    public void addLender(){
        Lender lender = new Lender();
        lenders.add(lender);
        StorageManager.saveData("leaders.dat",(Serializable) lenders);
        System.out.println("Lender " + lender.getName() + " added successfully!\n");
    }

    public void addLender(Lender lender){
        if(lender == null){
            System.out.println("Cannot add null lender!");
            return;
        }
        lenders.add(lender);
        StorageManager.saveData("leaders.dat",(Serializable) lenders);
        System.out.println("Lender " + lender.getName() + " added successfully!\n");
    }

    public Lender getLender(String lenderId){
        if(lenderId == null) return null;
        for(Lender l : lenders){
            if(l.getLenderId().equalsIgnoreCase(lenderId)) return l;
        }
        System.out.println("Lender with ID " + lenderId + " not found!");
        return null;
    }
    
    public static Lender findLenderById(String lenderId){
        if(lenders == null) return null;
        for(Lender l : lenders){
            if(l.getLenderId().equalsIgnoreCase(lenderId)) return l;
        }
        return null;
    }

    public void autoMatchLoan(Loan loan, double requiredAmount){
        if(lenders.isEmpty()){
            System.out.println("\nNo lenders available for auto-matching!");
            return;
        }

        double totalCapital = getTotalCapital();
        if(totalCapital < requiredAmount){
            System.out.println("\nInsufficient total lender capital to fund this loan!");
            System.out.println("Required: " + requiredAmount + " | Available: " + totalCapital);
            return;
        }

        System.out.println("\n==============================================");
        System.out.println("     🏦 Auto-Match Funding Allocation Report   ");
        System.out.println("==============================================");
        System.out.println("Loan ID: " + loan.getLoanId());
        System.out.println("Requested Amount: " + requiredAmount);
        System.out.println("Available Pool: " + totalCapital);
        System.out.println("----------------------------------------------");

        for(Lender lender : lenders){
            if(requiredAmount <= 0) break;
            double share = (lender.getCapital() / totalCapital) * requiredAmount;

            if(share > lender.getCapital()) share = lender.getCapital();
            double before = lender.getCapital();
            lender.investInLoan(loan, share);
            double after = lender.getCapital();

            System.out.printf("Lender %-15s | Allotted: %-10.2f | Balance Before: %-10.2f | After: %-10.2f%n", lender.getName(), share, before, after);
            requiredAmount -= share;
        }

        System.out.println("----------------------------------------------");
        if(requiredAmount <= 0){
            System.out.println("✅ Loan " + loan.getLoanId() + " is FULLY FUNDED via Auto-Match!");
        } else {
            System.out.println("⚠ Loan " + loan.getLoanId() + " is PARTIALLY FUNDED. Remaining: " + requiredAmount);
        }
        System.out.println("==============================================\n");

        saveLenderPortfolio();
    }

    public void printPortfolioSummary(){
        if(lenders.isEmpty()){
            System.out.println("\nNo lenders in the system yet!");
            return;
        }
        System.out.println("\n=== Lender Portfolio Summary ===");
        for(Lender l : lenders){
            l.printStatement();
        }
        System.out.println("==========================\n");
    }

    public void printAllLenders(){
        printPortfolioSummary();
    }

    public int getTotalLenders(){
        return lenders.size();
    }

    public double getTotalCapital(){
        double total = 0.0;
        for(Lender l : lenders){
            total += l.getCapital();
        }
        return total;
    }

    public void saveLenderPortfolio(){
        StorageManager.saveData("lenders.dat",(Serializable) lenders);
    }
}
