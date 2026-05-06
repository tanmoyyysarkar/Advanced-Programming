package notification;

import model.Order;

public class SMSNotification implements NotificationService {

    @Override
    public void sendNotification(Order order) {
        System.out.println("[SMS] Sending SMS to: " + order.getCustomerPhone());
        System.out.println("[SMS] Message: Your order #" + order.getOrderId() + " worth $" + order.getAmount() + " is confirmed. Thank you!");
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }
}
