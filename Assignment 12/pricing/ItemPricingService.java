package pricing;

import inventory.StockItem;

public interface ItemPricingService {
    double getUnitPrice(StockItem item);
    double calculateTotal(StockItem item, int quantity);
    String getPricingLabel(StockItem item);
    double getDiscountPercent();
}
