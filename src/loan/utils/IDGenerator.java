package loan.utils;

import java.util.UUID;

public class IDGenerator{
    public static String generateBorrowerId(){
        return "B" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateLoanId(){
        return "Loan" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateLenderId(){
        return "L" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}