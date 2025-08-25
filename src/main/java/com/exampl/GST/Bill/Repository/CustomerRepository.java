package com.exampl.GST.Bill.Repository;

import com.exampl.GST.Bill.Model.Customer;
import com.exampl.GST.Bill.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "SELECT * FROM customer WHERE local_date = :today", nativeQuery = true)
    List<Customer> findByLocalDate(@Param("today") LocalDate today);

}
