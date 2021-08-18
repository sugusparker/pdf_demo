package com.fiserv.pdf_demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.CrudRepositoryExtensionsKt;


public interface TransactionRepository extends CrudRepository<Transaction, String> {
}
