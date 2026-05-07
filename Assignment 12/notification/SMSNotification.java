package notification;

import model.Order;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SMSNotification implements NotificationService {

    private static final Path SMS_LOG = Paths.get("mock_data/sms.log");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void sendNotification(Order order) {
        System.out.println("[SMS] Sending SMS to: " + order.getCustomerPhone());
        System.out.println("[SMS] Message: Your order #" + order.getOrderId() + " worth $" + String.format("%.2f", order.getAmount()) + " is confirmed. Thank you!");

        writeToLog(order);
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }

    private void writeToLog(Order order) {
        try {
            Files.createDirectories(SMS_LOG.getParent());
            String smsContent = String.format(
                "[%s] Phone: %s | Order: %s | Amount: %.2f | Status: %s | Message: Your order is confirmed. Thank you!\n",
                LocalDateTime.now().format(formatter),
                order.getCustomerPhone(),
                order.getOrderId(),
                order.getAmount(),
                order.getStatus()
            );
            Files.write(SMS_LOG, smsContent.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("[SMS] Mock data saved to: " + SMS_LOG.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("[SMS] Failed to save mock data: " + e.getMessage());
        }
    }
}
