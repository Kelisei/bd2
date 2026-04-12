package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Service;
import java.util.List;
import java.util.Optional;

@Repository
public class ServiceRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public Service save(Service service) {
        return sessionFactory.getCurrentSession().merge(service);
    }

    public Service findById(Long id) {
        return sessionFactory.getCurrentSession().get(Service.class, id);
    }

    public List<Service> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Service", Service.class)
                .getResultList();
    }

    public void delete(Service service) {
        sessionFactory.getCurrentSession().remove(service);
    }

    public Service getMostDemandedService() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT s FROM Service s JOIN s.itemServiceList i " +
                        "GROUP BY s ORDER BY SUM(i.quantity) DESC", unlp.info.bd2.model.Service.class)
                .setMaxResults(1)
                .uniqueResult();
    }

    public Service update(Service service) {
        return sessionFactory.getCurrentSession().merge(service);
    }

    public Service getServiceById(Long id) {
        return sessionFactory.getCurrentSession().get(Service.class, id);
    }

    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) {
        List<Service> services = sessionFactory.getCurrentSession()
                .createQuery("FROM Service s WHERE s.name = :name AND s.supplier.id = :supplierId", Service.class)
                .setParameter("name", name)
                .setParameter("supplierId", id)
                .getResultList();
        return services.isEmpty() ? Optional.empty() : Optional.of(services.get(0));
    }
}