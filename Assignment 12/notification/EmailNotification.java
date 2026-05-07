package notification;

import model.Order;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailNotification implements NotificationService {

    private static final Path EMAIL_LOG = Paths.get("mock_data/emails.log");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void sendNotification(Order order) {
        System.out.println("[Email] Sending order confirmation to: " + order.getCustomerEmail());
        System.out.println("[Email] Subject: Order #" + order.getOrderId() + " Confirmed!");
        System.out.println("[Email] Body: Hi " + order.getCustomerName() + ", your order of $" + String.format("%.2f", order.getAmount()) + " has been placed.");

        writeToLog(order);
    }

    @Override
    public String getChannelName() {
        return "Email";
    }

    private void writeToLog(Order order) {
        try {
            Files.createDirectories(EMAIL_LOG.getParent());
            String emailContent = String.format(
                "[%s] To: %s | Order: %s | Amount: %.2f | Status: %s | Message: Order confirmed.\n",
                LocalDateTime.now().format(formatter),
                order.getCustomerEmail(),
                order.getOrderId(),
                order.getAmount(),
                order.getStatus()
            );
            Files.write(EMAIL_LOG, emailContent.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("[Email] Mock data saved to: " + EMAIL_LOG.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("[Email] Failed to save mock data: " + e.getMessage());
        }
    }
}
