package payment;

import model.Order;

public class CreditCardPayment implements PaymentProcessor {

    @Override
    public boolean processPayment(Order order){
        System.out.println("[CreditCard] Contacting bank gateway...");
        System.out.println("[CreditCard] Charging $" + order.getAmount() + " to card on file for " + order.getCustomerName());
        System.out.println("[CreditCard] Payment authorised. Transaction ID: CC-" + order.getOrderId());
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "Credit Card";
    }
}
