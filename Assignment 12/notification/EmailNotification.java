package notification;

import model.Order;

public class EmailNotification implements NotificationService {

    @Override
    public void sendNotification(Order order) {
        System.out.println("[Email] Sending order confirmation to: " + order.getCustomerEmail());
        System.out.println("[Email] Subject: Order #" + order.getOrderId() + " Confirmed!");
        System.out.println("[Email] Body: Hi " + order.getCustomerName() + ", your order of $" + order.getAmount() + " has been placed.");
    }

    @Override
    public String getChannelName() {
        return "Email";
    }
}
