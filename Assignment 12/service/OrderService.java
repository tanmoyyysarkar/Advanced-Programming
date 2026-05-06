package service;

import model.Order;
import notification.NotificationService;
import order.OrderProcessor;
import payment.PaymentProcessor;
import storage.OrderStorage;

import java.util.List;

/**
 * DIP: OrderService depends ONLY on abstractions (interfaces), never on concrete
 * classes.  All dependencies are injected through the constructor - the caller
 * decides which implementations to use.
 *
 * SRP: This class only orchestrates the order workflow.  It does NOT know how
 * to charge a card, send an email, or write to a file.
 */
public class OrderService {

    private final OrderProcessor      orderProcessor;
    private final PaymentProcessor    paymentProcessor;
    private final List<NotificationService> notificationServices;
    private final OrderStorage        orderStorage;

    // DIP: All concrete types injected from outside - OrderService never does "new XyzImpl()"
    public OrderService(OrderProcessor orderProcessor,
                        PaymentProcessor paymentProcessor,
                        List<NotificationService> notificationServices,
                        OrderStorage orderStorage) {
        this.orderProcessor       = orderProcessor;
        this.paymentProcessor     = paymentProcessor;
        this.notificationServices = notificationServices;
        this.orderStorage         = orderStorage;
    }

    /**
    * Full order lifecycle: create -> pay -> notify -> save.
     */
    public void processOrder(String orderId, String customerName,
                             String customerEmail, String customerPhone,
                             double baseAmount) {

        System.out.println("\n========================================");
        System.out.println(" PROCESSING ORDER: " + orderId);
        System.out.println(" Order Type  : " + orderProcessor.getOrderType());
        System.out.println(" Payment Via : " + paymentProcessor.getPaymentMethod());
        System.out.println("========================================\n");

        // Step 1 - Create Order
        System.out.println("--- Step 1: Creating Order ---");
        Order order = orderProcessor.createOrder(
                orderId, customerName, customerEmail, customerPhone, baseAmount);
        System.out.println("Order created: " + order);

        // Step 2 - Process Payment
        System.out.println("\n--- Step 2: Processing Payment ---");
        boolean paymentSuccess = paymentProcessor.processPayment(order);

        if (!paymentSuccess) {
            System.out.println("Payment FAILED. Order will not be placed.");
            order.setStatus("PAYMENT_FAILED");
            orderStorage.saveOrder(order);   // still persist the failed record
            return;
        }
        order.setStatus("CONFIRMED");
        System.out.println("Payment SUCCESS.");

        // Step 3 - Send Notifications
        System.out.println("\n--- Step 3: Sending Notifications ---");
        for (NotificationService ns : notificationServices) {
            ns.sendNotification(order);
            System.out.println("  - Notified via " + ns.getChannelName());
        }

        // Step 4 - Save Order
        System.out.println("\n--- Step 4: Saving Order ---");
        orderStorage.saveOrder(order);

        System.out.println("\nOrder " + orderId + " processed successfully!\n");
    }
}
