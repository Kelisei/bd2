package unlp.info.bd2.repositories;

import org.springframework.data.repository.CrudRepository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.ItemService;
import java.util.List;

@Repository
public interface ItemServiceRepository extends CrudRepository<ItemService, Long> {

}
