package inventory;

import java.util.List;

public interface StockCatalog {
    List<StockItem> getAllItems();
    StockItem getById(String id);
}
