package inventory;

public class StockItem {
    private final String id;
    private final String name;
    private final double price;
    private final boolean discounted;

    public StockItem(String id, String name, double price, boolean discounted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discounted = discounted;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isDiscounted() {
        return discounted;
    }
}
