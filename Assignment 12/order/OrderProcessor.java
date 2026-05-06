package order;

import model.Order;

// ISP: Only order-processing behaviour - small and focused
public interface OrderProcessor {
    Order createOrder(String orderId, String customerName, String customerEmail, String customerPhone, double baseAmount);
    String getOrderType();
}
