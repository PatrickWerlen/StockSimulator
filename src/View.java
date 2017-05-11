import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Observable;
import java.util.Observer;
import java.util.function.UnaryOperator;

public class View extends Stage{
    private Model model;
    private Controller controller;

    public View(Model model, Controller controller){
        this.model = model;
        this.controller = controller;

        this.setScene(new Scene(new BorderPane(null, null, createOrderPlacement(), createCustodyAccount(), createOverview())));
        this.setTitle("StockManager");
        this.sizeToScene();
        this.show();
    }

    private GridPane createOrderPlacement(){
        GridPane gridPane = new GridPane();

        TextField quantity = new TextField();
        quantity.setTextFormatter(createIntFormatter());


        Label price = new Label();
        price.setPadding(new Insets(5));
        ChoiceBox companies = createChoiceBox();
        companies.getSelectionModel().select(0 );
        companies.valueProperty().addListener((observable, oldValue, newValue) ->
        price.setText(String.valueOf(model.companies.get(companies.getSelectionModel().selectedIndexProperty().getValue()).getPrice())));


        ToggleGroup operation = new ToggleGroup();
        RadioButton buy = new RadioButton("buy");
        buy.setToggleGroup(operation);
        RadioButton sell = new RadioButton("sell");
        sell.setToggleGroup(operation);


        Button enter = new Button("Enter");
        enter.disableProperty().bind(
                Bindings.isEmpty(quantity.textProperty())
                .or(companies.valueProperty().isNull())
                .or(operation.selectedToggleProperty().isNull())
        );
        enter.setOnAction(event -> {
            controller.newOrder(
                    companies.getSelectionModel().selectedIndexProperty().getValue(),
                    Integer.valueOf(quantity.getText()),
                    model.companies.get(companies.getSelectionModel().selectedIndexProperty().getValue()).getPrice(),
                    buy.isSelected());
            quantity.clear();
            price.setText(String.valueOf(model.companies.get(companies.getSelectionModel().selectedIndexProperty().getValue()).getPrice()));
            buy.setSelected(false);
            sell.setSelected(false);
        });

        gridPane.addRow(1,new Label("Company:\t"), companies);
        gridPane.addRow(2, new Label("Quantity:\t"), quantity);
        gridPane.addRow(3, new Label("Price:\t"), price);
        gridPane.addRow(4, buy, sell, enter);

        gridPane.setPadding(new Insets(15,25,0,0));

        return gridPane;
    }

    private TextFormatter<String> createIntFormatter(){
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        return textFormatter;
    }

    private GridPane createOverview(){
        GridPane gridPane = new GridPane();
        int rowIndex = 0;
        for(Company company : model.companies){
            Label companyName = new Label(company.getName());
            companyName.setPadding(new Insets(5,25,0,0));
            Label companyPrice = new Label();
            companyPrice.setPadding(new Insets(5,0,0,0));
            companyPrice.textProperty().bind(company.priceProperty.asString());

            gridPane.addRow(rowIndex++,companyName, companyPrice);
        }
        gridPane.setPadding(new Insets(10,0,0,25));
        return gridPane;
    }

    private HBox createCustodyAccount() {
        HBox custodyAccount = new HBox();
        Label title0 = new Label("Company:");
        Label title1 = new Label("Shares in Account:");
        Label title2 = new Label("Profit in Total:");
        VBox title = new VBox(title0, title1,title2);
        title.setAlignment(Pos.BOTTOM_LEFT);
        title.setPadding(new Insets(25));
        custodyAccount.getChildren().add(title);

        for (Company company : model.companies) {
            Label name = new Label(company.getName());
            Label stock = new Label("Number of shares:\t" + company.getQuantity());
            stock.textProperty().bind(company.quantityProperty.asString());
            Label profit = new Label("realized profit:\t" + company.getProfit());
            profit.textProperty().bind(company.profitProperty.asString("%.0f"));
            VBox vbox = new VBox(name,stock, profit);
            vbox.setAlignment(Pos.BOTTOM_LEFT);
            vbox.setPadding(new Insets(25));
            custodyAccount.getChildren().add(vbox);
        }
        return custodyAccount;
    }

    private ChoiceBox createChoiceBox(){
        ChoiceBox companies = new ChoiceBox();
        for(Company company : model.companies) {
            companies.getItems().add(company.getName());
        }
        return companies;
    }
}
