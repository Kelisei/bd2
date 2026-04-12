package unlp.info.bd2.services;

import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;
import unlp.info.bd2.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (userRepository.getUserByUsername(username).isPresent()) {
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
        if (userRepository.getUserByUsername(username).isPresent()) {
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
        return (DriverUser) userRepository.save(driver);
    }

    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email,
            Date birthdate, String phoneNumber, String education) throws ToursException {
        if (userRepository.getUserByUsername(username).isPresent()) {
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
        return (TourGuideUser) userRepository.save(guide);
    }

    @Override
    public Optional<User> getUserById(Long id) throws ToursException {
        return Optional.ofNullable(userRepository.findById(id));
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public User updateUser(User user) throws ToursException {
        // El test espera que el username sea inmutable via updateUser
        String originalUsername = userRepository.getUsernameById(user.getId());
        if (originalUsername != null) {
            user.setUsername(originalUsername);
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
        return stopRepository.getStopByNameStart(name);
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
        return Optional.ofNullable(routeRepository.findById(id));
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        return routeRepository.getRoutesBelowPrice(price);
    }

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        User user = userRepository.getUserByUsername(username)
                .orElseThrow(() -> new ToursException("Usuario no encontrado"));

        if (!(user instanceof DriverUser)) {
            throw new ToursException("El usuario no es un Chofer (DriverUser)");
        }

        Route route = Optional.ofNullable(routeRepository.findById(idRoute))
                .orElseThrow(() -> new ToursException("Ruta no encontrada"));

        DriverUser driver = (DriverUser) user;
        route.getDriverList().add(driver);
        driver.getRoutes().add(route);
        routeRepository.update(route);
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        User user = userRepository.getUserByUsername(username)
                .orElseThrow(() -> new ToursException("Usuario no encontrado"));

        if (!(user instanceof TourGuideUser)) {
            throw new ToursException("El usuario no es un Guía (TourGuideUser)");
        }

        Route route = Optional.ofNullable(routeRepository.findById(idRoute))
                .orElseThrow(() -> new ToursException("Ruta no encontrada"));

        TourGuideUser guide = (TourGuideUser) user;
        route.getTourGuideList().add(guide);
        guide.getRoutes().add(route);
        routeRepository.update(route);
    }

    @Override
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        if (supplierRepository.getSupplierByAuthorizationNumber(authorizationNumber).isPresent()) {
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
        unlp.info.bd2.model.Service service = Optional.ofNullable(serviceRepository.findById(id))
                .orElseThrow(() -> new ToursException("No existe el producto"));

        service.setPrice(Double.valueOf(newPrice));
        return serviceRepository.update(service);
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        return Optional.ofNullable(supplierRepository.findById(id));
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return supplierRepository.getSupplierByAuthorizationNumber(authorizationNumber);
    }

    @Override
    public Optional<unlp.info.bd2.model.Service> getServiceByNameAndSupplierId(String name, Long id)
            throws ToursException {
        return serviceRepository.getServiceByNameAndSupplierId(name, id);
    }

    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        return createPurchase(code, new Date(), route, user);
    }

    @Override
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        if (purchaseRepository.getPurchaseByCode(code).isPresent()) {
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
        purchase.setTotalPrice(purchase.getTotalPrice() + (float)(service.getPrice() * quantity));
        purchaseRepository.save(purchase);
        return saved;
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        return purchaseRepository.getPurchaseByCode(code);
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
        // Rely on CascadeType.ALL from Purchase
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
        return purchaseRepository.getAllPurchasesOfUsername(username);
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        return userRepository.getUserSpendingMoreThan(mount);
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return supplierRepository.getTopNSuppliersInPurchases(n);
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return purchaseRepository.getCountOfPurchasesBetweenDates(start, end);
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        return routeRepository.getRoutesWithStop(stop);
    }

    @Override
    public Long getMaxStopOfRoutes() {
        return routeRepository.getMaxStopOfRoutes();
    }

    @Override
    public List<Route> getRoutsNotSell() {
        return routeRepository.getRoutsNotSell();
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        return routeRepository.getTop3RoutesWithMaxRating();
    }

    @Override
    public unlp.info.bd2.model.Service getMostDemandedService() {
        return serviceRepository.getMostDemandedService();
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return userRepository.getTourGuidesWithRating1();
    }
}