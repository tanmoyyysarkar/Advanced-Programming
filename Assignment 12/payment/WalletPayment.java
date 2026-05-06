package payment;

import model.Order;

// OCP: New payment method added - zero changes to existing code
public class WalletPayment implements PaymentProcessor {

    private double walletBalance;

    public WalletPayment(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    @Override
    public boolean processPayment(Order order) {
        if (walletBalance < order.getAmount()) {
            System.out.println("[Wallet] Insufficient balance! Required: $" + order.getAmount() + ", Available: $" + walletBalance);
            return false;
        }

        walletBalance -= order.getAmount();

        System.out.println("[Wallet] Deducted $" + order.getAmount() + " from wallet. Remaining balance: $" + walletBalance);
        System.out.println("[Wallet] Payment successful. Ref: WAL-" + order.getOrderId());
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "Wallet";
    }
}
