package com.basisttha.Kreta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basisttha.Kreta.Model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, String>{
    
}
