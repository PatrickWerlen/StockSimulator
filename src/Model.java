import java.util.ArrayList;
import java.util.List;

public class Model{

    public List<Company> companies = new ArrayList<>();


    public void newOrder(int company, int quantity, double price, boolean buy){
        companies.get(company).placeOrder(new Order(quantity, price, buy));
    }

    public void createCompanies(){
        companies.add(0, new Company("Apple", 1000*Math.random()));
        companies.add(1, new Company("Microsoft", 1000*Math.random()));
        companies.add(2, new Company("Google", 1000*Math.random()));
        companies.add(3, new Company("Tesla", 1000*Math.random()));
        companies.add(4, new Company("ExxonMobil", 1000*Math.random()));
    }


}
