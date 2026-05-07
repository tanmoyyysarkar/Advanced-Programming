package notification;

import model.Order;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// OCP: Added as a new channel without modifying Email or SMS classes
public class PushNotification implements NotificationService {

    private static final Path PUSH_LOG = Paths.get("mock_data/push_notifications.log");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void sendNotification(Order order) {
        System.out.println("[Push] Sending push notification to device of: " + order.getCustomerName());
        System.out.println("[Push] Title: Order Placed!");
        System.out.println("[Push] Body: Order #" + order.getOrderId() + " confirmed. Amount: $" + String.format("%.2f", order.getAmount()));

        writeToLog(order);
    }

    @Override
    public String getChannelName() {
        return "Push Notification";
    }

    private void writeToLog(Order order) {
        try {
            Files.createDirectories(PUSH_LOG.getParent());
            String pushContent = String.format(
                "[%s] User: %s | Order: %s | Amount: %.2f | Status: %s | Title: Order Placed! | Body: Order confirmed.\n",
                LocalDateTime.now().format(formatter),
                order.getCustomerName(),
                order.getOrderId(),
                order.getAmount(),
                order.getStatus()
            );
            Files.write(PUSH_LOG, pushContent.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("[Push] Mock data saved to: " + PUSH_LOG.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("[Push] Failed to save mock data: " + e.getMessage());
        }
    }
}
