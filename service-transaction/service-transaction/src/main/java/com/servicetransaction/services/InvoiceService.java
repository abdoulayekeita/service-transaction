package com.servicetransaction.services;

import com.servicetransaction.dto.InvoiceDto;
import com.servicetransaction.entities.Invoice;

public interface InvoiceService {
    Invoice save(InvoiceDto invoiceDto);

}
