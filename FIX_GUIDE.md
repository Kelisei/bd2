# Guide to Fix the Tours Project

This guide provides a step-by-step walkthrough to resolve compilation errors, JPA mapping issues, and business logic bugs in the project.

## Step 1: Update Dependencies (`pom.xml`)
1. Add the **H2 Database** dependency (useful for testing without a local MySQL).
2. Ensure **MySQL Connector** is present.

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

## Step 2: Fix Model Annotations and Mapping
### User.java
1. Rename the table to `users` (avoid reserved word conflict).
2. Add `@Id` and `@GeneratedValue` to the `id` field.
3. Add `@OneToMany(mappedBy = "user")` to `purchaseList` and initialize it as an `ArrayList`.

### Route.java
1. Standardize the field name to `maxNumberOfUsers`.
2. Add `@OneToMany(mappedBy = "route")` to a new `purchases` list.
3. Fix the `@ManyToMany` with `tourGuides` and `drivers` to ensure correct `JoinTable` definitions.

### DriverUser & TourGuideUser
1. Add `@ManyToMany(mappedBy = "...")` to the `routes` list.
2. Initialize the `routes` list to avoid `NullPointerException`.
3. Correct any typos (e.g., `setRouts` -> `setRoutes`).

### Purchase.java
1. Ensure `@OneToOne(mappedBy = "purchase", cascade = CascadeType.ALL)` is set for the `review` field.
2. Ensure `@OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)` is set for `items`.

## Step 3: Update Repositories
In all Repository classes (UserRepository, RouteRepository, etc.):
1. Change the `save` method signature from `void` to return the saved entity.
2. Use `sessionFactory.getCurrentSession().merge(entity)` and return its result. This ensures you are working with managed entities.

### Fix HQL Queries
1. **ServiceRepository**: In `getMostDemandedService`, change `s.items` to `s.itemServiceList`.
2. **SupplierRepository**: In `getTopNSuppliersInPurchases`, change `srv.items` to `srv.itemServiceList`.
3. **UserRepository**: 
    - In `getTourGuidesWithRating1`, change `r.guides` to `r.tourGuides`.
    - In `getUserSpendingMoreThan`, use `SUM(p.totalPrice)` instead of complex joins.

## Step 4: Implement Business Logic in `ToursServiceImpl`
### User Creation
- Check if `username` already exists; if so, throw `ToursException("Constraint Violation")`.
- Set the `name` field using the `fullName` parameter.

### Purchase Logic
- **Validation**: Throw `ToursException` if the route is full (`route.getPurchases().size() >= route.getMaxNumberOfUsers()`).
- **Pricing**: Initialize `purchase.totalPrice` with `route.price`.
- **Relationship Sync**: When creating a purchase or adding an item, manually add the object to the parent's list (e.g., `user.getPurchaseList().add(purchase)`).

### Service Price Updates
- Update the price and call `repository.update()`.

### User Immutability
- In `updateUser`, ensure the `username` cannot be changed by looking up the original value by ID before saving.

## Step 5: Configure Hibernate
In `HibernateConfiguration.java`:
1. Use the modern driver: `com.mysql.cj.jdbc.Driver`.
2. For testing, you can temporarily switch the URL to `jdbc:h2:mem:bd2_tours;MODE=MySQL` and the dialect to `org.hibernate.dialect.H2Dialect`.

## Step 6: Data Integrity
In `DBInitializer.java`:
- Ensure all `Service` names are unique within the same supplier if the model has a unique constraint on name.

---
### Verification
Run the following command to verify the fixes:
```bash
mvn clean install
```
All 22 tests should pass.
