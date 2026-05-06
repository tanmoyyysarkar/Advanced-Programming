package storage;

import model.Order;

public class DatabaseStorage implements OrderStorage {

    @Override
    public void saveOrder(Order order) {
        System.out.println("[Database] Connecting to orders_db...");
        System.out.println("[Database] Executing: INSERT INTO orders VALUES ("
                + order.getOrderId() + ", '"
                + order.getCustomerName() + "', "
                + order.getAmount() + ", '"
                + order.getStatus() + "')");
        System.out.println("[Database] Order saved successfully. Rows affected: 1");
    }

    @Override
    public String getStorageType() {
        return "Database (SQL)";
    }
}
