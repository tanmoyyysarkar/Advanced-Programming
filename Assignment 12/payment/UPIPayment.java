package payment;

import model.Order;

// OCP: Added without modifying PaymentProcessor or any other class
public class UPIPayment implements PaymentProcessor {
    private String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean processPayment(Order order) {
        System.out.println("[UPI] Sending payment request to UPI ID: " + upiId);
        System.out.println("[UPI] Debiting $" + order.getAmount() + " from linked bank account...");
        System.out.println("[UPI] Payment successful. Ref: UPI-" + order.getOrderId());
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "UPI (" + upiId + ")";
    }
}
