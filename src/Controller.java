public class Controller {
    private Model model;

    public Controller(Model model){
        this.model = model;
    }

    public void newOrder(int company, int quantity, double price, boolean buy){
        model.newOrder(company, quantity, price, buy);
    }
}
