package unlp.info.bd2.dto;

public class RouteSummaryDTO {
    private String name;
    private Long purchaseCount;
    private Double averagePrice;

    public RouteSummaryDTO(String name, Long purchaseCount, Double averagePrice) {
        this.name = name;
        this.purchaseCount = purchaseCount;
        this.averagePrice = averagePrice;
    }

    public String getName() {
        return name;
    }

    public Long getPurchaseCount() {
        return purchaseCount;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }
}