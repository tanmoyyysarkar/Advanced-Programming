package payment;

import model.Order;

// OCP + ISP: Small, focused interface — only payment responsibility.
// New payment methods can be added without touching existing code.

public interface PaymentProcessor {
    boolean processPayment(Order order);
    String getPaymentMethod();
}
