package inventory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvStockCatalog implements StockCatalog {

    private final Path filePath;
    private final List<StockItem> items;
    private final Map<String, StockItem> itemsById;

    public CsvStockCatalog(String filePath) {
        this.filePath = Paths.get(filePath);
        this.items = new ArrayList<>();
        this.itemsById = new HashMap<>();
        load();
    }

    @Override
    public List<StockItem> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public StockItem getById(String id) {
        if (id == null) {
            return null;
        }
        return itemsById.get(id.trim().toUpperCase());
    }

    private void load() {
        if (!Files.exists(filePath)) {
            System.out.println("[StockCatalog] Stock file not found: " + filePath.toAbsolutePath());
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                if (trimmed.toLowerCase().startsWith("id,")) {
                    continue;
                }

                String[] parts = trimmed.split(",");
                if (parts.length < 4) {
                    System.out.println("[StockCatalog] Skipping invalid line: " + trimmed);
                    continue;
                }

                String id = parts[0].trim();
                String name = parts[1].trim();
                double price = parseDouble(parts[2].trim(), trimmed);
                boolean discounted = Boolean.parseBoolean(parts[3].trim());

                StockItem item = new StockItem(id, name, price, discounted);
                items.add(item);
                itemsById.put(id.toUpperCase(), item);
            }
        } catch (IOException e) {
            System.out.println("[StockCatalog] Failed to load stock file: " + e.getMessage());
        }
    }

    private double parseDouble(String value, String sourceLine) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("[StockCatalog] Invalid price '" + value + "' in line: " + sourceLine);
            return 0.0;
        }
    }
}
