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

    public User save(User user) {
        return sessionFactory.getCurrentSession().merge(user);
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

    public String getUsernameById(Long id) {
        // Usamos FlushMode.COMMIT para evitar que el auto-flush guarde los cambios
        // pendientes
        // (como el nuevo username seteado en el objeto) antes de consultar el valor
        // original.
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT username FROM User WHERE id = :id", String.class)
                .setParameter("id", id)
                .setHibernateFlushMode(org.hibernate.FlushMode.COMMIT)
                .uniqueResult();
    }

    public List<User> getUserSpendingMoreThan(float mount) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT DISTINCT p.user FROM Purchase p WHERE p.totalPrice >= :mount", User.class)
                .setParameter("mount", mount)
                .getResultList();
    }

    public List<TourGuideUser> getTourGuidesWithRating1() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("select distinct g from Route r join r.tourGuides g, Purchase p "
                        + "where p.route = r and p.review.rating = 1", TourGuideUser.class)
                .getResultList();
    }

    public Optional<User> getUserByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public User update(User user) {
        return sessionFactory.getCurrentSession().merge(user);
    }

}
