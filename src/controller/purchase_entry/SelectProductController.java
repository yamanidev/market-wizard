package controller.purchase_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SelectProductController implements Initializable {
    @FXML private TextField searchTextField;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;
    @FXML private TableView<Product> productsTableView;
    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Double> purchasedPriceCol;
    @FXML private TableColumn<Product, Double> soldPriceCol;
    @FXML private TableColumn<Product,Integer> quantityCol;
    @FXML private TableColumn<Product, String> categoryCol;
    @FXML private TableColumn<Product, Integer> productIdCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        purchasedPriceCol.setCellValueFactory(new PropertyValueFactory<>("purchasedPrice"));
        soldPriceCol.setCellValueFactory(new PropertyValueFactory<>("soldPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        confirmBtn.disableProperty().bind(Bindings.isEmpty(productsTableView
                .getSelectionModel().getSelectedItems()));

        updateProducts();

        FilteredList<Product> productsFilteredList = new FilteredList<>(getProducts(), b -> true);
        searchTextField.textProperty().addListener((observable,oldValue,newValue) -> {
            productsFilteredList.setPredicate(product -> {
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (product.getName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else  if (product.getCategory().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else return false;
            });
        });
        SortedList<Product> productsSortedList = new SortedList<>(productsFilteredList);
        productsSortedList.comparatorProperty().bind(productsTableView.comparatorProperty());
        productsTableView.setItems(productsSortedList);
    }

    private ObservableList<Product> getProducts(){
        ObservableList<Product> list = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * FROM products";

        try (Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            Product product;

            while(rs.next()){
                product = new Product(rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("purchased_price"),
                        rs.getDouble("sold_price"),
                        rs.getString("expiration_date"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getInt("quantity") * rs.getDouble("purchased_price"));

                list.add(product);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public void newProductOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("purchase_entry/add-new-product.fxml",
                "Add New Product");
        window.setOnHidden((e) -> {
            updateProducts();
        });
    }

    private void updateProducts() {
        productsTableView.setItems(getProducts());
    }

    public void cancelOnClick(ActionEvent actionEvent) {
        Stage window = (Stage) cancelBtn.getScene().getWindow();
        window.close();
    }

    public void confirmOnClick(ActionEvent actionEvent) {
        NameHolder.productName = productsTableView.getSelectionModel().getSelectedItem().getName();
        NameHolder.productId = productsTableView.getSelectionModel().getSelectedItem().getId();
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }


}
