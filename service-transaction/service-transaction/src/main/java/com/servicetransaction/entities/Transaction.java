package com.servicetransaction.entities;

import com.servicetransaction.enumeration.TypeTransaction;
import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @Column(unique = true)
    private String identifier;
    private Double amount;
    private Double amountWithoutFees;
    private Double feesPercentage;
    private String name;
    private String email;
    private String phone;
    private TypeTransaction typeTransaction;
    @ManyToOne
    private Invoice invoice;

}
