package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Purchase;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class PurchaseRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public Purchase save(Purchase purchase) {
        return sessionFactory.getCurrentSession().merge(purchase);
    }

    public Purchase findById(Long id) {
        return sessionFactory.getCurrentSession().get(Purchase.class, id);
    }

    public List<Purchase> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Purchase", Purchase.class)
                .getResultList();
    }

    public void delete(Purchase purchase) {
        sessionFactory.getCurrentSession().remove(purchase);
    }

    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(p) FROM Purchase p WHERE p.date BETWEEN :start AND :end", Long.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .uniqueResult();
    }

    public Optional<Purchase> getPurchaseByCode(String code) {
        List<Purchase> purchases = sessionFactory.getCurrentSession()
                .createQuery("FROM Purchase WHERE code = :code", Purchase.class)
                .setParameter("code", code)
                .getResultList();
        return purchases.isEmpty() ? Optional.empty() : Optional.of(purchases.get(0));
    }

    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Purchase WHERE user.username = :username", Purchase.class)
                .setParameter("username", username)
                .getResultList();
    }
}