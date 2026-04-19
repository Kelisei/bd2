package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Review;
import java.util.List;

@Repository
public class ReviewRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public Review save(Review review) {
        return sessionFactory.getCurrentSession().merge(review);
    }

    public Review findById(Long id) {
        return sessionFactory.getCurrentSession().get(Review.class, id);
    }

    public List<Review> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Review", Review.class)
                .getResultList();
    }

    public void delete(Review review) {
        sessionFactory.getCurrentSession().remove(review);
    }
}