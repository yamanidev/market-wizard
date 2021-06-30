package controller.selling_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SellingEntryController implements Initializable {
    @FXML private Circle imageCircle;
    @FXML private Pane openSliderPane;
    @FXML private Pane closeSliderPane;
    @FXML private ImageView openSliderImage;
    @FXML private AnchorPane slider;
    @FXML private AnchorPane x;

    @FXML private Button endTransactionBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;

    @FXML private Label totalPriceLabel;
    @FXML private Label currentCashierLabel;
    @FXML private Label selectedCustomerLabel;

    @FXML private TableView<Product> productsTableView;
    @FXML private TableColumn<Product, Integer> idCol;
    @FXML private TableColumn<Product, String> productCol;
    @FXML private TableColumn<Product, String> categoryCol;
    @FXML private TableColumn<Product, Double> unitPriceCol;
    @FXML private TableColumn<Product, Integer> quantityCol;
    @FXML private TableColumn<Product, Double> totalCol;

    public void initialize(URL location, ResourceBundle resources) {
        slider.setTranslateX(-255);
        openSliderPane.setVisible(true);
        closeSliderPane.setVisible(false);
        endTransactionBtn.setDisable(true);

        openSliderImage.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-255);

            slide.setOnFinished((ActionEvent e)-> {
                openSliderPane.setVisible(false);
                closeSliderPane.setVisible(true);
            });
        });

        openSliderPane.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-255);

            slide.setOnFinished((ActionEvent e)-> {
                openSliderPane.setVisible(false);
                closeSliderPane.setVisible(true);
            });
        });


        closeSliderPane.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-255);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                openSliderPane.setVisible(true);
                closeSliderPane.setVisible(false);
            });
        });

        x.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-255);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                openSliderPane.setVisible(true);
                closeSliderPane.setVisible(false);
            });
        });

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("soldPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

        deleteBtn.disableProperty().bind(Bindings.isEmpty(productsTableView
                .getSelectionModel().getSelectedItems()));
        editBtn.disableProperty().bind(Bindings.isEmpty(productsTableView
                .getSelectionModel().getSelectedItems()));

        try(Connection c = DBUtils.getConnection();
            Statement st = c.createStatement()){
            String sqlQuery = "INSERT INTO bills (cashier, customer_id) VALUES ('None', 0)";
            st.executeUpdate(sqlQuery);

            sqlQuery = "SELECT bill_id FROM bills ORDER BY bill_id DESC LIMIT 1";
            ResultSet rs = st.executeQuery(sqlQuery);
            NameHolder.billId = rs.getInt("bill_id");
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    private ObservableList<Product> getProducts(int billId){
        ObservableList<Product> list = FXCollections.observableArrayList();

        String sqlQuery = "SELECT\n" +
                "products.product_id, products.product_name, bills_products.quantity, products.sold_price, products.category\n" +
                "FROM products \n" +
                "JOIN bills_products ON (products.product_id = bills_products.product_id)\n" +
                "JOIN bills ON (bills.bill_id = bills_products.bill_id)\n" +
                "WHERE bills.bill_id = " + billId;

        Product product;
        try (Connection c = DBUtils.getConnection()){

            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            while(rs.next()){
                product = new Product(rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("sold_price"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getInt("quantity") * rs.getDouble("sold_price"));
                list.add(product);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return list;
    }

    private double getTotal(){
        double total = 0;
        for (Product product : productsTableView.getItems()){
            total += product.getTotal();
        }
        return total;
    }

    private void updateProducts(){
        productsTableView.setItems(getProducts(NameHolder.billId));
        totalPriceLabel.setText(String.format("%.2f", getTotal()).replace(",", "."));
    }

    public void dashboardOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/home/drakkath/IdeaProjects/MarketWizard/src/view/dashboard/dashboard.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void sellingEntryOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/selling_entry/selling-entry.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void stockOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/stock/stock.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void customersOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/home/drakkath/IdeaProjects/MarketWizard/src/view/customers/customers.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void suppliersOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/home/drakkath/IdeaProjects/MarketWizard/src/view/suppliers/suppliers.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void purchaseEntryOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/purchase_entry/purchase-entry.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }


    public void addOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("selling_entry/add-product.fxml", "Add product");
        window.setOnHidden((e) -> {
            updateProducts();
            endTransactionBtn.disableProperty().bind(Bindings.isEmpty(productsTableView.getItems()));
        });
    }

    public void editOnClick(ActionEvent actionEvent) {
    }

    public void deleteOnClick(ActionEvent actionEvent) {
    }

    public void endTransactionOnClick(ActionEvent actionEvent) {
    }

    public void selectCustomerOnClick(ActionEvent actionEvent) throws IOException {
       Stage window = HelperMethods.openWindow("selling_entry/select-customer.fxml",
               "Select Customer");
        window.setOnHidden((e) ->{
            selectedCustomerLabel.setText(NameHolder.customerName);
        });
    }
}
