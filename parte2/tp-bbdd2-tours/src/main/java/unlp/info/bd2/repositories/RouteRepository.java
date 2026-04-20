package unlp.info.bd2.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.dto.RouteSummaryDTO;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;
import java.util.List;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {
    List<Route> findByPriceLessThanOrderByNameAsc(int rating);

    List<Route> findByPriceLessThan(float price); // Agregado

    List<Route> findByStopsContains(Stop stop);

    @Query("SELECT MAX(SIZE(r.stops)) FROM Route r ")
    int getMaxStopOfRoutes();

    @Query("SELECT r FROM Route r LEFT JOIN r.purchases p WHERE p IS NULL")
    List<Route> getRoutesNotSell();

    @Query("SELECT r FROM Route r JOIN r.purchases p JOIN p.review rv GROUP BY r ORDER BY AVG(rv.rating) DESC")
    Page<Route> getTop3RoutesWithMaxRating(Pageable pageable);

    @Query("SELECT new unlp.info.bd2.dto.RouteSummaryDTO(r.name, COUNT(p), AVG(r.price)) " +
            "FROM Route r LEFT JOIN Purchase p ON p.route = r " +
            "GROUP BY r.name")
    List<RouteSummaryDTO> getRoutesSummary();
}