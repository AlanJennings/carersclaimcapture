package uk.gov.dwp.carersallowance;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class TransactionIdService {
    private static int counter = 1;

    public String getTransactionId() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMM");
        String transactionId = dateFormatter.format(new Date()) + String.format("%7d", counter++);  // e.g. 16110012523
        return transactionId;
    }
}