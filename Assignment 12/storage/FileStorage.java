package storage;

import model.Order;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileStorage implements OrderStorage {

    private final Path filePath;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileStorage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    @Override
    public void saveOrder(Order order) {
        System.out.println("[FileStorage] Opening file: " + filePath);
        System.out.println("[FileStorage] Appending record -> "
                + order.getOrderId() + ","
                + order.getCustomerName() + ","
                + order.getAmount() + ","
                + order.getStatus());
        System.out.println("[FileStorage] File written and closed successfully.");

        writeToFile(order);
    }

    @Override
    public String getStorageType() {
        return "File Storage (" + filePath + ")";
    }

    private void writeToFile(Order order) {
        try {
            Files.createDirectories(filePath.getParent());

            // Write header if file is new
            if (!Files.exists(filePath)) {
                String header = "Timestamp,OrderId,CustomerName,CustomerEmail,CustomerPhone,Amount,Status\n";
                Files.write(filePath, header.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            String orderRecord = String.format(
                "%s,%s,%s,%s,%s,%.2f,%s\n",
                LocalDateTime.now().format(formatter),
                order.getOrderId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getCustomerPhone(),
                order.getAmount(),
                order.getStatus()
            );
            Files.write(filePath, orderRecord.getBytes(), StandardOpenOption.APPEND);
            System.out.println("[FileStorage] Mock file saved to: " + filePath.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("[FileStorage] Failed to save data: " + e.getMessage());
        }
    }
}
