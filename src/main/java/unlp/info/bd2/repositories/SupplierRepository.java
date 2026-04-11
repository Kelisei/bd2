package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Supplier;
import java.util.List;

@Repository
public class SupplierRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Supplier supplier) {
        sessionFactory.getCurrentSession().merge(supplier);
    }

    public Supplier findById(Long id) {
        return sessionFactory.getCurrentSession().get(Supplier.class, id);
    }

    public List<Supplier> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Supplier", Supplier.class)
                .getResultList();
    }

    public void delete(Supplier supplier) {
        sessionFactory.getCurrentSession().remove(supplier);
    }

    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT s FROM Supplier s JOIN s.services srv JOIN srv.items i " +
                        "GROUP BY s ORDER BY COUNT(i) DESC", Supplier.class)
                .setMaxResults(n) // Límite de resultados
                .getResultList();
    }
}