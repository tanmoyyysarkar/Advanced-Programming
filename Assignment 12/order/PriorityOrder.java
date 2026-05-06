package order;

import model.Order;

// LSP: Behaves like OrderProcessor — adds a surcharge but still returns a valid Order
public class PriorityOrder implements OrderProcessor {

    private static final double PRIORITY_SURCHARGE = 15.00;

    @Override
    public Order createOrder(String orderId, String customerName, String customerEmail, String customerPhone, double baseAmount) {
        double priorityAmount = baseAmount + PRIORITY_SURCHARGE;
        System.out.println("[PriorityOrder] Adding priority surcharge: $" + PRIORITY_SURCHARGE);
        System.out.println("[PriorityOrder] Base: $" + baseAmount + "  →  Final: $" + priorityAmount);
        System.out.println("[PriorityOrder] Order flagged for express handling.");
        return new Order(orderId, customerName, customerEmail, customerPhone, priorityAmount);
    }

    @Override
    public String getOrderType() {
        return "Priority Order (+$" + PRIORITY_SURCHARGE + " surcharge)";
    }
}
