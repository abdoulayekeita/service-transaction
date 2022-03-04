package com.servicetransaction.dao;

import com.servicetransaction.entities.TestInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestInvoiceRepository extends JpaRepository<TestInvoice, Long> {
}
