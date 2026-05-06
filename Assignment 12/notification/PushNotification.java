package notification;

import model.Order;

// OCP: Added as a new channel without modifying Email or SMS classes
public class PushNotification implements NotificationService {

    @Override
    public void sendNotification(Order order) {
        System.out.println("[Push] Sending push notification to device of: " + order.getCustomerName());
        System.out.println("[Push] Title: Order Placed!");
        System.out.println("[Push] Body: Order #" + order.getOrderId() + " confirmed. Amount: $" + order.getAmount());
    }

    @Override
    public String getChannelName() {
        return "Push Notification";
    }
}
