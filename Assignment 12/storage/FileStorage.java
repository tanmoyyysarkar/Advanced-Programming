package storage;

import model.Order;

public class FileStorage implements OrderStorage {

    private String filePath;

    public FileStorage(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveOrder(Order order) {
        System.out.println("[FileStorage] Opening file: " + filePath);
        System.out.println("[FileStorage] Appending record → "
                + order.getOrderId() + ","
                + order.getCustomerName() + ","
                + order.getAmount() + ","
                + order.getStatus());
        System.out.println("[FileStorage] File written and closed successfully.");
    }

    @Override
    public String getStorageType() {
        return "File Storage (" + filePath + ")";
    }
}
