package unlp.info.bd2.services;

import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;
import unlp.info.bd2.repositories.ToursRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ToursServiceImpl implements ToursService {

    private final ToursRepository repository;
    public ToursServiceImpl(ToursRepository repository) {
        this.repository = repository;
    }
    @Override
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String expedient) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber, String education) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public Optional<User> getUserById(Long id) throws ToursException {
        // TODO: implement
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        // TODO: implement
        return Optional.empty();
    }

    @Override
    public User updateUser(User user) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public void deleteUser(User user) throws ToursException {
        // TODO: implement
    }

    @Override
    public Stop createStop(String name, String description) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        // TODO: implement
        return null;
    }

    @Override
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        // TODO: implement
        return Optional.empty();
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        // TODO: implement
        return null;
    }

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        // TODO: implement
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        // TODO: implement
    }

    @Override
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public unlp.info.bd2.model.Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public unlp.info.bd2.model.Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        // TODO: implement
        return Optional.empty();
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        // TODO: implement
        return Optional.empty();
    }

    @Override
    public Optional<unlp.info.bd2.model.Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        // TODO: implement
        return Optional.empty();
    }

    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public ItemService addItemToPurchase(unlp.info.bd2.model.Service service, int quantity, Purchase purchase) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        // TODO: implement
        return Optional.empty();
    }

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        // TODO: implement
    }

    @Override
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        // TODO: implement
        return null;
    }

    @Override
    public void deleteRoute(Route route) throws ToursException {
        // TODO: implement
    }

    // --- CONSULTAS HQL ---

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        // TODO: implement
        return null;
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        // TODO: implement
        return null;
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        // TODO: implement
        return null;
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        // TODO: implement
        return 0;
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        // TODO: implement
        return null;
    }

    @Override
    public Long getMaxStopOfRoutes() {
        // TODO: implement
        return 0L;
    }

    @Override
    public List<Route> getRoutsNotSell() {
        // TODO: implement
        return null;
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        // TODO: implement
        return null;
    }

    @Override
    public unlp.info.bd2.model.Service getMostDemandedService() {
        // TODO: implement
        return null;
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        // TODO: implement
        return null;
    }
}