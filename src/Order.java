public class Order {
    private int quantity;
    private double price;
    private boolean buy;

    public Order(int quantity, double price, boolean buy){
        this.quantity = quantity;
        this.price = price;
        this.buy = buy;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public boolean isBuy() {
        return buy;
    }
}
