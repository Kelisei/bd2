package unlp.info.bd2.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.repositories.SupplierRepository;

public class SupplierService {

    private SupplierRepository sp;

    public SupplierService(SupplierRepository sp) {
        this.sp = sp;
    }

    // getTopNSuppliersInPurchases(int n)
    public List<Supplier> getTopNSuppliers(int n) {
        PageRequest topN = PageRequest.of(0, n);
        Page<Supplier> page = sp.getTopNSuppliersInPurchases(topN);
        return page.getContent();
    }
}
