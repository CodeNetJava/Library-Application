package com.sau.library.repositoty;

import com.sau.library.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepositoty extends JpaRepository<Payment,Integer> {
}
