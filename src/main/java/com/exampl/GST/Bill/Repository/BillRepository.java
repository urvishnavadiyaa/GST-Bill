package com.exampl.GST.Bill.Repository;

import com.exampl.GST.Bill.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT stock FROM product WHERE LOWER(product_name) = LOWER(?1)", nativeQuery = true)
    int getProductCount(String productName);

    @Query(value = "SELECT price FROM product WHERE LOWER(product_name) = LOWER(?1)", nativeQuery = true)
    int getProductAmount(String productName);

    Product findByProductName(String productName);
}
