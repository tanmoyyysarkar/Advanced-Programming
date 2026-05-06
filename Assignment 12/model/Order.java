package model;

public class Order {
    private String orderId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private double amount;
    private String status;

    public Order(String orderId, String customerName, String customerEmail, String customerPhone, double amount) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.amount = amount;
        this.status = "PENDING";
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Order[id=%s, customer=%s, amount=%.2f, status=%s]",
                orderId, customerName, amount, status);
    }
}
