package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private float price;
    private float totalKm;
    private int maxNumberOfUsers;

    // Relación N a M con Stop
    @ManyToMany
    @JoinTable(
        name = "route_stop",
        joinColumns = @JoinColumn(name = "route_id"),
        inverseJoinColumns = @JoinColumn(name = "stop_id")
    )
    private List<Stop> stops = new ArrayList<>();

    // Relación N a M con DriverUser
    @ManyToMany
    @JoinTable(
        name = "route_driver",
        joinColumns = @JoinColumn(name = "route_id"),
        inverseJoinColumns = @JoinColumn(name = "driver_id")
    )
    private List<DriverUser> drivers = new ArrayList<>();

    // Relación N a M con TourGuideUser
    @ManyToMany
    @JoinTable(
        name = "route_tourguide",
        joinColumns = @JoinColumn(name = "route_id"),
        inverseJoinColumns = @JoinColumn(name = "tourguide_id")
    )
    private List<TourGuideUser> tourGuides = new ArrayList<>();

    private int maxNumberUsers;

    // --- MÉTODOS DE SINCRONIZACIÓN ---
    public void addStop(Stop stop) {
        this.stops.add(stop);
        stop.getRoutes().add(this); // Bidireccionalidad
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(float totalKm) {
        this.totalKm = totalKm;
    }

    public int getMaxNumberUsers() {
        return maxNumberUsers;
    }

    public void setMaxNumberUsers(int maxNumberUsers) {
        this.maxNumberUsers = maxNumberUsers;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<DriverUser> getDriverList() {
        return drivers;
    }

    public void setDriverList(List<DriverUser> driverList) {
        this.drivers = drivers;
    }

    public List<TourGuideUser> getTourGuideList() {
        return tourGuides;
    }

    public void setTourGuideList(List<TourGuideUser> tourGuideList) {
        this.tourGuides = tourGuideList;
    }

    
    public void addDriver(DriverUser driver) {
        this.drivers.add(driver);
    }

    public void addTourGuide(TourGuideUser guide) {
        this.tourGuides.add(guide);
    }
}
