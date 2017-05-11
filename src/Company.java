import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert;
import java.util.ArrayDeque;



public class Company{

    private ArrayDeque<Order> orderqueue = new ArrayDeque<>();

    //attributes of company
    private String name;
    private double price;
    public DoubleProperty priceProperty = new SimpleDoubleProperty();
    private double profit;
    public DoubleProperty profitProperty = new SimpleDoubleProperty();
    private int quantity;
    public IntegerProperty quantityProperty = new SimpleIntegerProperty();

    public Company(String name, double initialValue){
        this.name = name;
        this.price=round(initialValue,2);
        simulate();
    }

    public int getQuantity(){
        return quantity;
    }

    public double getProfit(){
        return profit;
    }

    public double getPrice(){
        return price;
    }

    public String getName(){
        return name;
    }

    public void placeOrder(Order order){
        /*
         IF the order is a buy order, it will be added to the orderqueue
        and the custody account gets changed with the new values (quantity)
         ELSE the sell will get handled in a seperate function
        After every transaction there a new price simulated
         */
        if(order.isBuy()){
            orderqueue.add(order);
            quantity += order.getQuantity();
            quantityProperty.setValue(quantity);
        }
        else handleSell(order);
        simulate();
    }

    public void simulate(){
        // 50% of the time the price increases/decreases
        double direction = Math.random();
        if(direction > 0.5) {
            price =round(price*(1+(Math.random()/10)),2);
        }
        else {
            price = round(price - price*(Math.random()/10),2);
        }
        priceProperty.setValue(price);
    }

    public void handleSell(Order order){
        //checks if you hold enough stocks to sell them
        if(quantity < order.getQuantity()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Transaction not possible");
            alert.setContentText("You do not own enough shares of that company!");
            alert.showAndWait();
        } else {
            /*creates a counter orderSize to keep count of how many
            shares still need to be sold*/
            int orderSize = order.getQuantity();
            quantity -= order.getQuantity();
            while(!(orderSize==0)){
                Order tmp = orderqueue.getFirst();
                //checks if the first order in the queue is smaller than the ordersize
                if(tmp.getQuantity() < order.getQuantity()){
                    profit += (order.getPrice()-tmp.getPrice())*tmp.getQuantity();
                    orderSize -= order.getQuantity();
                    orderqueue.removeFirst();
                }else{
                    //changes profit etc. and changes the quantity of the existing order
                    profit += (order.getPrice()-tmp.getPrice())*order.getQuantity();
                    tmp.setQuantity(tmp.getQuantity()-order.getQuantity());
                    orderqueue.removeFirst();
                    orderqueue.addFirst(tmp);
                    orderSize = 0;
                }
            }
        }
        quantityProperty.setValue(quantity);
        profitProperty.setValue(profit);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
