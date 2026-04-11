package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.ItemService;
import java.util.List;

@Repository
public class ItemServiceRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(ItemService itemService) {
        sessionFactory.getCurrentSession().merge(itemService);
    }

    public ItemService findById(Long id) {
        return sessionFactory.getCurrentSession().get(ItemService.class, id);
    }

    public List<ItemService> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM ItemService", ItemService.class)
                .getResultList();
    }

    public void delete(ItemService itemService) {
        sessionFactory.getCurrentSession().remove(itemService);
    }
}
