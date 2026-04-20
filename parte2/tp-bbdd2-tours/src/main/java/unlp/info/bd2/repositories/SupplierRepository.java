package unlp.info.bd2.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Supplier;
import java.util.Optional;

@Repository
public interface SupplierRepository extends CrudRepository<Supplier, Long> {
    Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);

    @Query("SELECT i.service.supplier FROM Purchase p JOIN p.items i GROUP BY i.service.supplier ORDER BY COUNT(p) DESC")
    Page<Supplier> getTopNSuppliersInPurchases(Pageable pageable);
}