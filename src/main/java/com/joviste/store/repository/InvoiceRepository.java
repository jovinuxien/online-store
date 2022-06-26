package com.joviste.store.repository;

import com.joviste.store.domain.Invoice;
import java.util.List;
import java.util.Optional;

import com.joviste.store.domain.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Invoice entity.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    default Optional<Invoice> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Invoice> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Invoice> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct invoice from Invoice invoice left join fetch invoice.order",
        countQuery = "select count(distinct invoice) from Invoice invoice"
    )
    Page<Invoice> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct invoice from Invoice invoice left join fetch invoice.order")
    List<Invoice> findAllWithToOneRelationships();

    @Query("select invoice from Invoice invoice left join fetch invoice.order where invoice.id =:id")
    Optional<Invoice> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(value = "SELECT * FROM INVOICE LEFT JOIN PRODUCT_ORDER ON INVOICE.ORDER_ID=PRODUCT_ORDER.ID" +
        "LEFT JOIN CUSTOMER ON PRODUCT_ORDER.CUSTOMER_ID=CUSTOMER.ID" +
        "LEFT JOIN JHI_USER ON CUSTOMER.USER_ID=JHI_USER.ID" +
        "WHERE JHI_USER.LOGIN=:login",
     nativeQuery = true)
    Page<Invoice> findAllByCustomerUserLogin(@Param("login") String login, Pageable pageable);

    @Query(value = "SELECT * FROM INVOICE " +
        "LEFT JOIN PRODUCT_ORDER ON INVOICE.ORDER_ID=PRODUCT_ORDER.ID "+
        "LEFT JOIN CUSTOMER ON PRODUCT_ORDER.CUSTOMER_ID=CUSTOMER.ID " +
        "LEFT JOIN JHI_USER ON CUSTOMER.USER_ID=JHI_USER.ID " +
        "WHERE JHI_USER.LOGIN=:login AND INVOICE.ID=:id",
        nativeQuery = true)
    Optional<Invoice> findOneByCustomerUserLogin(@Param("login") String login, @Param("id") Long id);
}
