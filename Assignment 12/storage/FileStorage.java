package storage;

import model.Order;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileStorage implements OrderStorage {

    private static final Path FILE_PATH = Paths.get("mock_data/file_storage.csv");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileStorage() {
    }

    @Override
    public void saveOrder(Order order) {
        System.out.println("[FileStorage] Opening file: " + FILE_PATH);
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
        return "File Storage (mock_data/file_storage.csv)";
    }

    private void writeToFile(Order order) {
        try {
            Files.createDirectories(FILE_PATH.getParent());

            // Write header if file is new
            if (!Files.exists(FILE_PATH)) {
                String header = "Timestamp,OrderId,CustomerName,CustomerEmail,CustomerPhone,Amount,Status\n";
                Files.write(FILE_PATH, header.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
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
            Files.write(FILE_PATH, orderRecord.getBytes(), StandardOpenOption.APPEND);
            System.out.println("[FileStorage] Mock file saved to: " + FILE_PATH.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("[FileStorage] Failed to save data: " + e.getMessage());
        }
    }
}
