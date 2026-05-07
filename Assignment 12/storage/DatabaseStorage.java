package storage;

import model.Order;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseStorage implements OrderStorage {

    private static final Path DB_FILE = Paths.get("mock_data/database.csv");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveOrder(Order order) {
        System.out.println("[Database] Connecting to orders_db...");
        System.out.println("[Database] Executing: INSERT INTO orders VALUES ("
                + order.getOrderId() + ", '"
                + order.getCustomerName() + "', "
                + order.getAmount() + ", '"
                + order.getStatus() + "')");
        System.out.println("[Database] Order saved successfully. Rows affected: 1");

        writeToDb(order);
    }

    @Override
    public String getStorageType() {
        return "Database (SQL)";
    }

    private void writeToDb(Order order) {
        try {
            Files.createDirectories(DB_FILE.getParent());

            // Write header if file is new
            if (!Files.exists(DB_FILE)) {
                String header = "Timestamp,OrderId,CustomerName,CustomerEmail,CustomerPhone,Amount,Status\n";
                Files.write(DB_FILE, header.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
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
            Files.write(DB_FILE, orderRecord.getBytes(), StandardOpenOption.APPEND);
            System.out.println("[Database] Mock DB saved to: " + DB_FILE.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("[Database] Failed to save mock data: " + e.getMessage());
        }
    }
}
