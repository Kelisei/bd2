package unlp.info.bd2.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import unlp.info.bd2.model.Service;
import unlp.info.bd2.repositories.ServiceRepository;

public class ServiceService {

    private ServiceRepository repo;

    public ServiceService(ServiceRepository repo) {
        this.repo = repo;
    }

    public Service getMostDemanded() {
        List<Service> result = repo.getMostDemandedService(PageRequest.of(0, 1));

        return result.isEmpty() ? null : result.get(0);
    }
}
