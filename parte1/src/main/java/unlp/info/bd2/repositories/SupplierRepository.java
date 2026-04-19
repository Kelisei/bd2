package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Supplier;
import java.util.List;
import java.util.Optional;

@Repository
public class SupplierRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public Supplier save(Supplier supplier) {
        return sessionFactory.getCurrentSession().merge(supplier);
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
                .createQuery("SELECT s FROM Supplier s JOIN s.services srv JOIN srv.itemServiceList i " +
                        "GROUP BY s ORDER BY COUNT(i) DESC", Supplier.class)
                .setMaxResults(n) // Límite de resultados
                .getResultList();
    }

    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        List<Supplier> suppliers = sessionFactory.getCurrentSession()
                .createQuery("FROM Supplier WHERE authorizationNumber = :authNum", Supplier.class)
                .setParameter("authNum", authorizationNumber)
                .getResultList();
        return suppliers.isEmpty() ? Optional.empty() : Optional.of(suppliers.get(0));
    }
}