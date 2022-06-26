package com.joviste.store.repository;

import com.joviste.store.domain.Invoice;
import com.joviste.store.domain.Shipment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Shipment entity.
 */
@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    default Optional<Shipment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Shipment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Shipment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct shipment from Shipment shipment left join fetch shipment.invoice",
        countQuery = "select count(distinct shipment) from Shipment shipment"
    )
    Page<Shipment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct shipment from Shipment shipment left join fetch shipment.invoice")
    List<Shipment> findAllWithToOneRelationships();

    @Query("select shipment from Shipment shipment left join fetch shipment.invoice where shipment.id =:id")
    Optional<Shipment> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(value = "SELECT * FROM SHIPMENT " +
           "LEFT JOIN INVOICE ON SHIPMENT.INVOICE_ID=INVOICE.ID " +
           "LEFT JOIN PRODUCT_ORDER ON INVOICE.ORDER_ID=PRODUCT_ORDER.ID " +
           "LEFT JOIN CUSTOMER ON PRODUCT_ORDER.CUSTOMER_ID=CUSTOMER.ID " +
           "LEFT JOIN JHI_USER ON CUSTOMER.USER_ID=JHI_USER.ID " +
           "WHERE JHI_USER.LOGIN=:login",
           nativeQuery = true)
    Page<Shipment> findAllByCustomerUserLogin(@Param("login") String login, Pageable pageable);

    @Query(value = "SELECT * FROM SHIPMENT " +
        "LEFT JOIN INVOICE ON SHIPMENT.INVOICE_ID=INVOICE.ID " +
        "LEFT JOIN PRODUCT_ORDER ON INVOICE.ORDER_ID=PRODUCT_ORDER.ID "+
        "LEFT JOIN CUSTOMER ON PRODUCT_ORDER.CUSTOMER_ID=CUSTOMER.ID " +
        "LEFT JOIN JHI_USER ON CUSTOMER.USER_ID=JHI_USER.ID " +
        "WHERE JHI_USER.LOGIN=:login AND SHIPMENT.ID=:id",
        nativeQuery = true)
    Optional<Shipment> findOneByCustomerUserLogin(@Param("login") String login, @Param("id") Long id);
}
