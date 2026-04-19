package unlp.info.bd2.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Route;
import java.util.List;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {
    List<Route> findByPriceLessThanOrderByNameAsc(int rating);

}