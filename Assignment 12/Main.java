import notification.*;
import order.*;
import payment.*;
import service.OrderService;
import storage.*;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates the full order-processing system.
 *
 * Notice that:
 *  - We only construct concrete classes HERE (in Main / composition root).
 *  - OrderService never imports any concrete class — pure DIP.
 *  - Adding a new payment/notification/storage type only requires a new class
 *    + wiring it here — no existing class changes (OCP).
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   E-Commerce Order Processing System     ║");
        System.out.println("╚══════════════════════════════════════════╝");

        // ──────────────────────────────────────────
        // Demo 1: Regular Order | Credit Card | Email + SMS | Database
        // ──────────────────────────────────────────
        OrderService service1 = new OrderService(
                new RegularOrder(),
                new CreditCardPayment(),
                Arrays.asList(new EmailNotification(), new SMSNotification()),
                new DatabaseStorage()
        );
        service1.processOrder("ORD-001", "Alice Johnson",
                "alice@example.com", "+1-555-0101", 120.00);

        // ──────────────────────────────────────────
        // Demo 2: Discounted Order (20% off) | UPI | Email + Push | File
        // ──────────────────────────────────────────
        OrderService service2 = new OrderService(
                new DiscountedOrder(20),
                new UPIPayment("alice@okaxis"),
                Arrays.asList(new EmailNotification(), new PushNotification()),
                new FileStorage("orders/orders_log.csv")
        );
        service2.processOrder("ORD-002", "Bob Smith",
                "bob@example.com", "+1-555-0202", 200.00);

        // ──────────────────────────────────────────
        // Demo 3: Priority Order | Wallet (sufficient balance) | All 3 channels | Database
        // ──────────────────────────────────────────
        List<NotificationService> allChannels = Arrays.asList(
                new EmailNotification(),
                new SMSNotification(),
                new PushNotification()
        );
        OrderService service3 = new OrderService(
                new PriorityOrder(),
                new WalletPayment(500.00),
                allChannels,
                new DatabaseStorage()
        );
        service3.processOrder("ORD-003", "Carol White",
                "carol@example.com", "+1-555-0303", 350.00);

        // ──────────────────────────────────────────
        // Demo 4: Wallet with INSUFFICIENT balance — shows graceful failure
        // ──────────────────────────────────────────
        OrderService service4 = new OrderService(
                new RegularOrder(),
                new WalletPayment(50.00),   // only $50 in wallet
                Arrays.asList(new EmailNotification()),
                new FileStorage("orders/failed_orders.csv")
        );
        service4.processOrder("ORD-004", "Dave Brown",
                "dave@example.com", "+1-555-0404", 300.00);
    }
}
