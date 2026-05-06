package order;

import model.Order;

// LSP: Substitutable for OrderProcessor — creates an Order with no modification
public class RegularOrder implements OrderProcessor {

    @Override
    public Order createOrder(String orderId, String customerName, String customerEmail, String customerPhone, double baseAmount) {
        System.out.println("[RegularOrder] Creating standard order. Amount: $" + baseAmount);
        return new Order(orderId, customerName, customerEmail, customerPhone, baseAmount);
    }

    @Override
    public String getOrderType() {
        return "Regular Order";
    }
}
