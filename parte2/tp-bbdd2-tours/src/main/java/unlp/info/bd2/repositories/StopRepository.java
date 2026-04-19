package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Stop;
import java.util.List;

@Repository
public class StopRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public Stop save(Stop stop) {
        return sessionFactory.getCurrentSession().merge(stop);
    }

    public Stop findById(Long id) {
        return sessionFactory.getCurrentSession().get(Stop.class, id);
    }

    public List<Stop> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Stop", Stop.class)
                .getResultList();
    }

    public void delete(Stop stop) {
        sessionFactory.getCurrentSession().remove(stop);
    }

    public List<Stop> getStopByNameStart(String name) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Stop WHERE name LIKE :name", Stop.class)
                .setParameter("name", name + "%")
                .getResultList();
    }
}
