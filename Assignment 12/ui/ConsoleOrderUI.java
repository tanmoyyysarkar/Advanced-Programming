package ui;

import inventory.StockCatalog;
import inventory.StockItem;
import notification.EmailNotification;
import notification.NotificationService;
import notification.PushNotification;
import notification.SMSNotification;
import order.DiscountedOrder;
import order.OrderProcessor;
import order.PriorityOrder;
import order.RegularOrder;
import payment.CreditCardPayment;
import payment.PaymentProcessor;
import payment.UPIPayment;
import payment.WalletPayment;
import pricing.ItemPricingService;
import service.OrderService;
import storage.DatabaseStorage;
import storage.FileStorage;
import storage.OrderStorage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public class ConsoleOrderUI {
    private final StockCatalog stockCatalog;
    private final ItemPricingService pricingService;
    private final Scanner scanner;

    public ConsoleOrderUI(StockCatalog stockCatalog, ItemPricingService pricingService, Scanner scanner) {
        this.stockCatalog = stockCatalog;
        this.pricingService = pricingService;
        this.scanner = scanner;
    }

    public void start() {
        printHeader();

        if (stockCatalog.getAllItems().isEmpty()) {
            System.out.println("No stock items available. Please check stocks.csv.");
            return;
        }

        boolean keepRunning = true;
        while (keepRunning) {
            processSingleOrder();
            keepRunning = readYesNo("\nPlace another order? (y/n): ");
        }

        System.out.println("\nThanks for using the order system.");
    }

    private void printHeader() {
        System.out.println("+==========================================+");
        System.out.println("|   E-Commerce Order Processing System     |");
        System.out.println("+==========================================+");
        if (pricingService.getDiscountPercent() > 0) {
            System.out.println("Discounted items get " + formatPercent(pricingService.getDiscountPercent()) + "% off.");
        }
    }

    private void processSingleOrder() {
        displayItems();
        StockItem item = promptForItem();
        int quantity = readIntRange("Enter quantity: ", 1, 100);

        double unitPrice = pricingService.getUnitPrice(item);
        double total = pricingService.calculateTotal(item, quantity);

        System.out.println("\nItem selected: " + item.getName());
        System.out.println("Unit price: $" + formatMoney(unitPrice));
        System.out.println("Quantity: " + quantity);
        System.out.println("Line total: $" + formatMoney(total));

        String customerName = readNonEmpty("\nCustomer name: ");
        String customerEmail = readNonEmpty("Customer email: ");
        String customerPhone = readNonEmpty("Customer phone: ");

        OrderProcessor orderProcessor = selectOrderProcessor();
        PaymentProcessor paymentProcessor = selectPaymentProcessor();
        List<NotificationService> notificationServices = selectNotificationChannels();
        OrderStorage orderStorage = selectStorage();

        OrderService service = new OrderService(orderProcessor, paymentProcessor, notificationServices, orderStorage);
        String orderId = generateOrderId();

        service.processOrder(orderId, customerName, customerEmail, customerPhone, total);
    }

    private void displayItems() {
        System.out.println("\nAvailable items:");
        System.out.printf(Locale.US, "%-6s %-24s %-10s %-12s %-10s%n",
                "ID", "Item", "Base", "Discount", "Final");

        for (StockItem item : stockCatalog.getAllItems()) {
            String base = formatMoney(item.getPrice());
            String discountLabel = pricingService.getPricingLabel(item);
            String finalPrice = formatMoney(pricingService.getUnitPrice(item));

            System.out.printf(Locale.US, "%-6s %-24s $%-9s %-12s $%-9s%n",
                    item.getId(), item.getName(), base, discountLabel, finalPrice);
        }
    }

    private StockItem promptForItem() {
        while (true) {
            String id = readNonEmpty("\nEnter item ID: ");
            StockItem item = stockCatalog.getById(id);
            if (item != null) {
                return item;
            }
            System.out.println("Invalid item ID. Please try again.");
        }
    }

    private OrderProcessor selectOrderProcessor() {
        System.out.println("\nOrder type:");
        System.out.println("1) Regular Order");
        System.out.println("2) Discounted Order (fixed 15% off)");
        System.out.println("3) Priority Order (+$15 surcharge)");

        int choice = readIntRange("Choose order type: ", 1, 3);
        if (choice == 1) {
            return new RegularOrder();
        }
        if (choice == 2) {
            return new DiscountedOrder(15);  // Fixed 15% discount
        }
        return new PriorityOrder();
    }

    private PaymentProcessor selectPaymentProcessor() {
        System.out.println("\nPayment method:");
        System.out.println("1) Credit Card");
        System.out.println("2) UPI");
        System.out.println("3) Wallet");

        int choice = readIntRange("Choose payment method: ", 1, 3);
        if (choice == 1) {
            return new CreditCardPayment();
        }
        if (choice == 2) {
            String upiId = readNonEmpty("Enter UPI ID: ");
            return new UPIPayment(upiId);
        }

        double balance = readDoubleMin("Enter wallet balance: ", 0);
        return new WalletPayment(balance);
    }

    private List<NotificationService> selectNotificationChannels() {
        System.out.println("\nNotification channels (comma-separated):");
        System.out.println("1) Email");
        System.out.println("2) SMS");
        System.out.println("3) Push");

        while (true) {
            String input = readNonEmpty("Choose channels: ");
            Set<Integer> selections = parseSelections(input, 3);

            if (selections.isEmpty()) {
                System.out.println("Please select at least one channel.");
                continue;
            }

            List<NotificationService> services = new ArrayList<>();
            if (selections.contains(1)) {
                services.add(new EmailNotification());
            }
            if (selections.contains(2)) {
                services.add(new SMSNotification());
            }
            if (selections.contains(3)) {
                services.add(new PushNotification());
            }
            return services;
        }
    }

    private OrderStorage selectStorage() {
        System.out.println("\nStorage type:");
        System.out.println("1) Database");
        System.out.println("2) File");

        int choice = readIntRange("Choose storage type: ", 1, 2);
        if (choice == 1) {
            return new DatabaseStorage();
        }

        return new FileStorage();
    }

    private Set<Integer> parseSelections(String input, int maxOption) {
        Set<Integer> selections = new LinkedHashSet<>();
        String[] parts = input.split(",");

        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            try {
                int value = Integer.parseInt(trimmed);
                if (value >= 1 && value <= maxOption) {
                    selections.add(value);
                }
            } catch (NumberFormatException ignored) {
                // ignore invalid entries
            }
        }
        return selections;
    }

    private String generateOrderId() {
        long suffix = System.currentTimeMillis() % 1000000;
        return "ORD-" + String.format(Locale.US, "%06d", suffix);
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    private int readIntRange(String prompt, int min, int max) {
        while (true) {
            String input = readNonEmpty(prompt);
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private double readDoubleMin(String prompt, double min) {
        while (true) {
            String input = readNonEmpty(prompt);
            try {
                double value = Double.parseDouble(input);
                if (value < min) {
                    System.out.println("Please enter a value of at least " + min + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid value.");
            }
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            String input = readNonEmpty(prompt).toLowerCase(Locale.US);
            if (input.equals("y") || input.equals("yes")) {
                return true;
            }
            if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter y or n.");
        }
    }

    private String formatMoney(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    private String formatPercent(double value) {
        return String.format(Locale.US, "%.0f", value);
    }
}
