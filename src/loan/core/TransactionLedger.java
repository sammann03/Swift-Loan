package loan.core;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionLedger implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final String FILE_NAME = "transactions.dat";

    private static TransactionLedger instance;
    private List<Transaction> transactions;

    private TransactionLedger(){
        this.transactions = new ArrayList<>();
        loadLedger();
    }

    public static TransactionLedger getInstance(){
        if(instance == null) instance = new TransactionLedger();
        return instance;
    }

    public void addTransaction(Transaction tx){
        transactions.add(tx);
        saveLedger();
    }

    public List<Transaction> getAllTransactions(){
        return Collections.unmodifiableList(transactions);
    }

    public List<Transaction> getByBorrower(String borrowerId){
        return transactions.stream().filter(t -> borrowerId.equals(t.getBorrowerId())).collect(Collectors.toList());
    }

    public List<Transaction> getByLender(String lenderId) {
        return transactions.stream().filter(t -> lenderId.equals(t.getLenderId())).collect(Collectors.toList());
    }

    public List<Transaction> getByLoan(String loanId) {
        return transactions.stream().filter(t -> loanId.equals(t.getLoanId())).collect(Collectors.toList());
    }

    public void printAllTransactions(){
        if(transactions.isEmpty()){
            System.out.println("No transactions recorded!");
            return;
        }
        System.out.println("\n=== Global Transaction Ledger ===");
        for(Transaction tx : transactions){
            System.out.println(tx);
        }
    }

    public void saveLedger(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
            oos.writeObject(this);
        }
        catch(IOException e){
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    public static TransactionLedger loadLedger(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))){
            return (TransactionLedger) ois.readObject();
        }
        catch(FileNotFoundException e){
            return new TransactionLedger();
        }
        catch(IOException | ClassNotFoundException e){
            System.out.println("Error loading transactions: " + e.getMessage());
            return new TransactionLedger();
        }
    }
}
