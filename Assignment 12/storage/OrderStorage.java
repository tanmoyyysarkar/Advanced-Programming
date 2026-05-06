package storage;

import model.Order;

// ISP: Storage-only concern. Not mixed with payment or notification.
// OCP: New storage backends (Cloud, Redis, etc.) can be added freely.
public interface OrderStorage {
    void saveOrder(Order order);
    String getStorageType();
}
