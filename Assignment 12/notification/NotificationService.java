package notification;

import model.Order;

// ISP: Dedicated interface for notifications only — nothing unrelated
// OCP: New notification channels can be added without modifying existing ones
public interface NotificationService {
    void sendNotification(Order order);
    String getChannelName();
}
