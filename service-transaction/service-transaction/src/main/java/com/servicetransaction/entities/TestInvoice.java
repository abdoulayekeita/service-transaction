package com.servicetransaction.entities;

import com.servicetransaction.enumeration.Status;
import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Collection;

import static javax.persistence.GenerationType.AUTO;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@ToString
public class TestInvoice {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Basic(optional = false)
    private Long id;
    private String identifier;
    private String costomerName;
    private String costomerPhone;
    private String costomerEmail;
    private String description;
    private String token;
    private Status status;
    @OneToMany
    private Collection<Transaction> transactions;
}
