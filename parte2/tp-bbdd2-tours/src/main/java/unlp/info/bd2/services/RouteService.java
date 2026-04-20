package unlp.info.bd2.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import unlp.info.bd2.model.Route;
import unlp.info.bd2.repositories.RouteRepository;

public class RouteService {
    private RouteRepository rr;

    public RouteService(RouteRepository rr) {
        this.rr = rr;
    }

    // getTop3RoutesWithMaxRating()
    List<Route> getTop3RoutesWithMaxRating() {
        PageRequest topN = PageRequest.of(0, 3);
        Page<Route> page = rr.getTop3RoutesWithMaxRating(topN);
        return page.getContent();
    }
}
