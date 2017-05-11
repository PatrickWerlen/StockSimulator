import javafx.application.Application;
import javafx.stage.Stage;

public class StockManager extends Application {
    private Model model = new Model();
    private Controller controller = new Controller(model);

    @Override
    public void init(){
        model.createCompanies();
    }

    @Override
    public void start(Stage primaryStage) {
        View view = new View(model, controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
