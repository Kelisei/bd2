package unlp.info.bd2.services;

import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;
import unlp.info.bd2.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ToursServiceImpl implements ToursService {

    private final UserRepository userRepository;
    private final StopRepository stopRepository;
    private final RouteRepository routeRepository;
    private final SupplierRepository supplierRepository;
    private final ServiceRepository serviceRepository;
    private final PurchaseRepository purchaseRepository;
    private final ItemServiceRepository itemServiceRepository;
    private final ReviewRepository reviewRepository;

    public ToursServiceImpl(UserRepository userRepository, StopRepository stopRepository,
            RouteRepository routeRepository, SupplierRepository supplierRepository,
            ServiceRepository serviceRepository, PurchaseRepository purchaseRepository,
            ItemServiceRepository itemServiceRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.stopRepository = stopRepository;
        this.routeRepository = routeRepository;
        this.supplierRepository = supplierRepository;
        this.serviceRepository = serviceRepository;
        this.purchaseRepository = purchaseRepository;
        this.itemServiceRepository = itemServiceRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public User createUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber) throws ToursException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ToursException("Constraint Violation");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(fullName);
        user.setEmail(email);
        user.setBirthdate(birthdate);
        user.setPhoneNumber(phoneNumber);
        user.setActive(true);
        return userRepository.save(user);
    }

    @Override
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber, String expedient) throws ToursException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ToursException("Constraint Violation");
        }
        DriverUser driver = new DriverUser();
        driver.setUsername(username);
        driver.setPassword(password);
        driver.setName(fullName);
        driver.setEmail(email);
        driver.setBirthdate(birthdate);
        driver.setPhoneNumber(phoneNumber);
        driver.setExpedient(expedient);
        driver.setActive(true);
        return userRepository.save(driver);
    }

    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email,
            Date birthdate, String phoneNumber, String education) throws ToursException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ToursException("Constraint Violation");
        }
        TourGuideUser guide = new TourGuideUser();
        guide.setUsername(username);
        guide.setPassword(password);
        guide.setName(fullName);
        guide.setEmail(email);
        guide.setBirthdate(birthdate);
        guide.setPhoneNumber(phoneNumber);
        guide.setEducation(education);
        guide.setActive(true);
        return userRepository.save(guide);
    }

    @Override
    public Optional<User> getUserById(Long id) throws ToursException {
        return userRepository.findById(id); // CORREGIDO: findById ya retorna Optional
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateUser(User user) throws ToursException {
        Optional<User> originalUser = userRepository.findById(user.getId());
        if (originalUser.isPresent()) {
            user.setUsername(originalUser.get().getUsername());
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) throws ToursException {
        if (!user.isActive()) {
            throw new ToursException("El usuario se encuentra desactivado");
        }
        if (user instanceof TourGuideUser) {
            TourGuideUser guide = (TourGuideUser) user;
            if (guide.getRoutes() != null && !guide.getRoutes().isEmpty()) {
                throw new ToursException("El usuario no puede ser desactivado");
            }
        }
        if (user.getPurchaseList() != null && !user.getPurchaseList().isEmpty()) {
            user.setActive(false);
            userRepository.save(user);
        } else {
            userRepository.delete(user);
        }
    }

    @Override
    public Stop createStop(String name, String description) throws ToursException {
        Stop stop = new Stop();
        stop.setName(name);
        stop.setDescription(description);
        return stopRepository.save(stop);
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        return stopRepository.findByNameStartingWith(name); // CORREGIDO: Usando Query Method standard
    }

    @Override
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops)
            throws ToursException {
        Route route = new Route();
        route.setName(name);
        route.setPrice(price);
        route.setTotalKm(totalKm);
        route.setMaxNumberOfUsers(maxNumberOfUsers);
        route.setStops(stops);
        return routeRepository.save(route);
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id); // CORREGIDO: Doble Optional quitado
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        return routeRepository.findByPriceLessThan(price);
    }

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ToursException("Usuario no encontrado"));

        if (!(user instanceof DriverUser)) {
            throw new ToursException("El usuario no es un Chofer (DriverUser)");
        }

        Route route = routeRepository.findById(idRoute) // CORREGIDO
                .orElseThrow(() -> new ToursException("Ruta no encontrada"));

        DriverUser driver = (DriverUser) user;
        route.getDriverList().add(driver);
        driver.getRoutes().add(route);
        routeRepository.save(route); // CORREGIDO: Es save(), no update()
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ToursException("Usuario no encontrado"));

        if (!(user instanceof TourGuideUser)) {
            throw new ToursException("El usuario no es un Guía (TourGuideUser)");
        }

        Route route = routeRepository.findById(idRoute) // CORREGIDO
                .orElseThrow(() -> new ToursException("Ruta no encontrada"));

        TourGuideUser guide = (TourGuideUser) user;
        route.getTourGuideList().add(guide);
        guide.getRoutes().add(route);
        routeRepository.save(route); // CORREGIDO
    }

    @Override
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        if (supplierRepository.findByAuthorizationNumber(authorizationNumber).isPresent()) {
            throw new ToursException("Constraint Violation");
        }
        Supplier supplier = new Supplier();
        supplier.setBusinessName(businessName);
        supplier.setAuthorizationNumber(authorizationNumber);
        return supplierRepository.save(supplier);
    }

    @Override
    public unlp.info.bd2.model.Service addServiceToSupplier(String name, float price, String description,
            Supplier supplier) throws ToursException {
        unlp.info.bd2.model.Service service = new unlp.info.bd2.model.Service();
        service.setName(name);
        service.setPrice(Double.valueOf(price));
        service.setDescription(description);
        service.setSupplier(supplier);
        unlp.info.bd2.model.Service saved = serviceRepository.save(service);
        supplier.getServices().add(saved);
        return saved;
    }

    @Override
    public unlp.info.bd2.model.Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        unlp.info.bd2.model.Service service = serviceRepository.findById(id) // CORREGIDO
                .orElseThrow(() -> new ToursException("No existe el producto"));

        service.setPrice(Double.valueOf(newPrice));
        return serviceRepository.save(service); // CORREGIDO
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id); // CORREGIDO
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return supplierRepository.findByAuthorizationNumber(authorizationNumber);
    }

    @Override
    public Optional<unlp.info.bd2.model.Service> getServiceByNameAndSupplierId(String name, Long id)
            throws ToursException {
        return serviceRepository.findByNameAndSupplierId(name, id);
    }

    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        return createPurchase(code, new Date(), route, user);
    }

    @Override
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        if (purchaseRepository.findByCode(code).isPresent()) {
            throw new ToursException("Constraint Violation");
        }
        if (route.getPurchases().size() >= route.getMaxNumberOfUsers()) {
            throw new ToursException("No puede realizarse la compra");
        }
        Purchase purchase = new Purchase();
        purchase.setCode(code);
        purchase.setDate(date);
        purchase.setRoute(route);
        purchase.setUser(user);
        purchase.setTotalPrice(route.getPrice());

        Purchase saved = purchaseRepository.save(purchase);
        user.getPurchaseList().add(saved);
        route.getPurchases().add(saved);
        return saved;
    }

    @Override
    public ItemService addItemToPurchase(unlp.info.bd2.model.Service service, int quantity, Purchase purchase)
            throws ToursException {
        ItemService item = new ItemService();
        item.setService(service);
        item.setQuantity(quantity);
        item.setPurchase(purchase);

        ItemService saved = itemServiceRepository.save(item);
        purchase.getItemServiceList().add(saved);
        service.getItemServiceList().add(saved);
        purchase.setTotalPrice(purchase.getTotalPrice() + (float) (service.getPrice() * quantity));
        purchaseRepository.save(purchase);
        return saved;
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        return purchaseRepository.findByCode(code);
    }

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        purchaseRepository.delete(purchase);
    }

    @Override
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        if (purchase.getReview() != null) {
            throw new ToursException("Constraint Violation");
        }
        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        purchase.setReview(review);
        return review;
    }

    @Override
    public void deleteRoute(Route route) throws ToursException {
        if (route.getPurchases() != null && !route.getPurchases().isEmpty()) {
            throw new ToursException("No puede eliminarse una ruta con compras asociadas");
        }
        routeRepository.delete(route);
    }

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return purchaseRepository.findByUserUsername(username);
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        return userRepository.getUserSpendingMoreThan(mount);
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return supplierRepository.getTopNSuppliersInPurchases(PageRequest.of(0, n)).getContent(); // CORREGIDO:
                                                                                                  // Paginación
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return purchaseRepository.countByDateBetween(start, end);
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        return routeRepository.findByStopsContains(stop);
    }

    @Override
    public Long getMaxStopOfRoutes() {
        return (long) routeRepository.getMaxStopOfRoutes(); // CORREGIDO: Casteo de int a Long
    }

    @Override
    public List<Route> getRoutsNotSell() {
        return routeRepository.getRoutesNotSell();
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        return routeRepository.getTop3RoutesWithMaxRating(PageRequest.of(0, 3)).getContent(); // CORREGIDO: Paginación
    }

    @Override
    public unlp.info.bd2.model.Service getMostDemandedService() {
        List<unlp.info.bd2.model.Service> result = serviceRepository.getMostDemandedService(PageRequest.of(0, 1));
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return userRepository.getTourGuidesWithRating1();
    }

    @Override
    public List<Route> getRoutesNotSell() {
        return routeRepository.getRoutesNotSell();
    }
}