package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

import java.util.List;

@Repository
public class RouteRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Route route) {
        sessionFactory.getCurrentSession().merge(route);
    }

    public Route findById(Long id) {
        return sessionFactory.getCurrentSession().get(Route.class, id);
    }

    public List<Route> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Route", Route.class)
                .getResultList();
    }

    public void delete(Route route) {
        sessionFactory.getCurrentSession().remove(route);
    }

    public List<Route> getRoutesWithStop(Stop stop) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT r FROM Route r JOIN r.stops s WHERE s = :stop", Route.class)
                .setParameter("stop", stop)
                .getResultList();
    }

    public Long getMaxStopOfRoutes() {
        // La función SIZE() es nativa de HQL para colecciones
        Integer max = sessionFactory.getCurrentSession()
                .createQuery("SELECT MAX(SIZE(r.stops)) FROM Route r", Integer.class)
                .uniqueResult();
        return max != null ? max.longValue() : 0L;
    }

    public List<Route> getRoutsNotSell() {
        // Usamos IS EMPTY que compila nativamente para colecciones vacías
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Route r WHERE r.purchases IS EMPTY", Route.class)
                .getResultList();
    }

    public List<Route> getTop3RoutesWithMaxRating() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT r FROM Route r JOIN r.purchases p JOIN p.review rev " +
                        "GROUP BY r ORDER BY AVG(rev.rating) DESC", Route.class)
                .setMaxResults(3)
                .getResultList();
    }
}