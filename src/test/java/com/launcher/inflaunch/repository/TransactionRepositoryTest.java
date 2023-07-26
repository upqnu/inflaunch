package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Transaction;
import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.enum_status.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionRepositoryTest {

    PrintWriter pw = new PrintWriter(System.out);

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void init(){
        User mentor1 = userRepository.findByUsername("mentor1");

        Transaction transaction1 = Transaction.builder()
                .payment(70000000L)
                .paymentStatus(PaymentStatus.COMPLETED)
                .user(mentor1)
                .build();

        Transaction transaction2 = Transaction.builder()
                .payment(140000000L)
                .paymentStatus(PaymentStatus.COMPLETED)
                .user(mentor1)
                .build();

        Transaction transaction3 = Transaction.builder()
                .payment(50000000L)
                .paymentStatus(PaymentStatus.COMPLETED)
                .user(mentor1)
                .build();

        transaction1 = transactionRepository.save(transaction1);
        transaction2 = transactionRepository.save(transaction2);
        transaction3 = transactionRepository.save(transaction3);

        transactionRepository.findAll().forEach(pw::println);

        pw.close();
    }
}