package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT DISTINCT p.user FROM Purchase p WHERE p.totalPrice > :amount")
    List<User> getUserSpendingMoreThan(@Param("amount") float amount);

    @Query("FROM TourGuideUser tgu JOIN tgu.routes r JOIN r.purchases p JOIN p.review rv GROUP BY tgu HAVING AVG(rv.rating) = 1")
    List<TourGuideUser> getTourGuidesWithRating1();

}