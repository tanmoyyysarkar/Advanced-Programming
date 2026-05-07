package pricing;

import inventory.StockItem;

import java.util.Locale;

public class FixedDiscountPricingService implements ItemPricingService {

    private final double discountPercent;

    public FixedDiscountPricingService(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    @Override
    public double getUnitPrice(StockItem item) {
        double basePrice = item.getPrice();
        if (item.isDiscounted() && discountPercent > 0) {
            return basePrice - (basePrice * discountPercent / 100.0);
        }
        return basePrice;
    }

    @Override
    public double calculateTotal(StockItem item, int quantity) {
        return getUnitPrice(item) * quantity;
    }

    @Override
    public String getPricingLabel(StockItem item) {
        if (item.isDiscounted() && discountPercent > 0) {
            return String.format(Locale.US, "%.0f%% off", discountPercent);
        }
        return "No discount";
    }

    @Override
    public double getDiscountPercent() {
        return discountPercent;
    }
}
