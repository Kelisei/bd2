package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(User user) {
        sessionFactory.getCurrentSession().merge(user);
    }

    public User findById(Long id) {
        return sessionFactory.getCurrentSession().get(User.class, id);
    }

    public List<User> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM User", User.class)
                .getResultList();
    }

    public void delete(User user) {
        sessionFactory.getCurrentSession().remove(user);
    }

    public List<User> getUserSpendingMoreThan(float mount) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT DISTINCT p.user FROM Purchase p LEFT JOIN p.items i " +
                        "GROUP BY p.user, p.id, p.route.price " +
                        "HAVING (p.route.price + COALESCE(SUM(i.service.price * i.quantity), 0)) >= :mount", User.class)
                .setParameter("mount", (double) mount) // HQL a veces requiere casteo a double para operaciones math
                .getResultList();
    }

    public List<TourGuideUser> getTourGuidesWithRating1() {
        // Usamos DISTINCT porque un guía puede tener varias compras con rating 1
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT DISTINCT g FROM Route r JOIN r.guides g JOIN r.purchases p JOIN p.review rev " +
                        "WHERE rev.rating = 1", TourGuideUser.class)
                .getResultList();
    }

    public Optional<User> getUserByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public void Update(User user) {
        sessionFactory.getCurrentSession().merge(user);
    }

}