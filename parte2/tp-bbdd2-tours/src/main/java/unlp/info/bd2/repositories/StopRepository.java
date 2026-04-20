package unlp.info.bd2.repositories;

import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Stop;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

@Repository
public interface StopRepository extends CrudRepository<Stop, Long> {
    List<Stop> findByNameStartingWith(String name);
}