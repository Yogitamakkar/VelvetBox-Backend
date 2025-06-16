package com.Ecommerce.demo.model;

import com.Ecommerce.demo.domain.PaymentMethod;
import com.Ecommerce.demo.domain.PaymentOrderStatus;
import com.Ecommerce.demo.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private PaymentOrderStatus paymentOrderStatus = PaymentOrderStatus.PENDING;

    private PaymentMethod paymentMethod;

    private String paymentLinkId;

    private Long amount;

    @ManyToOne
    private User user;

    @OneToMany
    private Set<Order> orders = new HashSet<>();
}
