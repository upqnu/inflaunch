package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Cart;
import com.launcher.inflaunch.domain.Payments;
import com.launcher.inflaunch.enum_status.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PaymentsRepositoryTest {

    PrintWriter pw = new PrintWriter(System.out);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Test
    void init(){
        Cart cart1 = cartRepository.findById(1L).orElse(null);
        Cart cart2 = cartRepository.findById(2L).orElse(null);
        Cart cart3 = cartRepository.findById(3L).orElse(null);
        Cart cart4 = cartRepository.findById(4L).orElse(null);
        Cart cart5 = cartRepository.findById(5L).orElse(null);

        Payments payments1 = Payments.builder()
                .cart(cart1)
                .paymentStatus(PaymentStatus.FAILED)
                .build();
        Payments payments2 = Payments.builder()
                .cart(cart2)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        Payments payments3 = Payments.builder()
                .cart(cart3)
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();
        Payments payments4 = Payments.builder()
                .cart(cart4)
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();
        Payments payments5 = Payments.builder()
                .cart(cart5)
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();

        payments1 = paymentsRepository.save(payments1);
        payments2 = paymentsRepository.save(payments2);
        payments3 = paymentsRepository.save(payments3);
        payments4 = paymentsRepository.save(payments4);
        payments5 = paymentsRepository.save(payments5);

        paymentsRepository.findAll().forEach(pw::println);

        pw.close();
    }
}