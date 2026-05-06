package payment;

import model.Order;

public interface PaymentProcessor {
    boolean processPayment(Order order);
    String getPaymentMethod();
}
