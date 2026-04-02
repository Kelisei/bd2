package unlp.info.bd2.model;

import java.util.List;

import jakarta.persistence.*; // Si usas una versión antigua de Spring Boot, podría ser javax.persistence.*

@Entity
@Table(name = "services")
public class Service {

    // e) Clave primaria con estrategia automática
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // f) Atributos según requerimientos y g) Restricción de unicidad
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = true) // Es opcional ponerlo, por defecto es true
    private String description;

    @Column(nullable = false)
    private Double price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "supplier_id") // Nombre de la columna FK en la tabla
    private Supplier supplier;
    @OneToMany(mappedBy = "service")
    private List<ItemService> itemServiceList = new java.util.ArrayList<>();

    protected Service() {
    }

    public Service(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Double getPrice() {
        return price;
    }

    public List<ItemService> getItemServiceList() {
        return itemServiceList;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}