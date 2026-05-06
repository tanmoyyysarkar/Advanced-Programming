package order;

import model.Order;

// LSP: Returns a valid Order just like RegularOrder - caller needs no special handling
public class DiscountedOrder implements OrderProcessor {

    private double discountPercent;

    public DiscountedOrder(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    @Override
    public Order createOrder(String orderId, String customerName, String customerEmail, String customerPhone, double baseAmount) {
        double discountedAmount = baseAmount - (baseAmount * discountPercent / 100);
        System.out.println("[DiscountedOrder] Applying " + discountPercent + "% discount.");
        System.out.println("[DiscountedOrder] Original: $" + baseAmount + "  ->  Final: $" + discountedAmount);
        return new Order(orderId, customerName, customerEmail, customerPhone, discountedAmount);
    }

    @Override
    public String getOrderType() {
        return "Discounted Order (" + discountPercent + "% off)";
    }
}
