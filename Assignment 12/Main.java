import inventory.CsvStockCatalog;
import pricing.FixedDiscountPricingService;
import ui.ConsoleOrderUI;

import java.util.Scanner;

/**
 * Entry point for the interactive order-processing system.
 */
public class Main {

    public static void main(String[] args) {
        ConsoleOrderUI ui = new ConsoleOrderUI(
                new CsvStockCatalog("stocks.csv"),
                new FixedDiscountPricingService(10),
                new Scanner(System.in)
        );

        ui.start();
    }
}
