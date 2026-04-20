package unlp.info.bd2.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.User;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    List<Purchase> findByUser(User user);

    boolean existsByRoute(Route route);

    Optional<Purchase> findByCode(String code);

    List<Purchase> findByUserUsername(String username);

    int countByDateBetween(Date from, Date to);
}