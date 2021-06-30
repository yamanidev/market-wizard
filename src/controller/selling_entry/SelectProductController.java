package controller.selling_entry;

import app.utils.DBUtils;
import app.utils.NameHolder;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SelectProductController implements Initializable {

    @FXML private TextField searchTextField;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;
    @FXML private TableView<Product> productsTableView;
    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Double> priceCol;
    @FXML private TableColumn<Product,Integer> quantityCol;
    @FXML private TableColumn<Product, String> categoryCol;
    @FXML private TableColumn<Product, Integer> productIdCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("soldPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        confirmBtn.disableProperty().bind(Bindings.isEmpty(productsTableView
                .getSelectionModel().getSelectedItems()));

        updateProducts();
    }

    private ObservableList<Product> getProducts(){
        ObservableList<Product> list = FXCollections.observableArrayList();
        String sqlQuery = "SELECT product_id, product_name, sold_price, quantity, category " +
                "FROM products";

        try (Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            Product product;

            while(rs.next()){
                product = new Product(rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("sold_price"),
                        rs.getString("category"),
                        rs.getInt("quantity"));

                list.add(product);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    private void updateProducts() {
        productsTableView.setItems(getProducts());
    }

    public void confirmOnClick(ActionEvent actionEvent) {
        NameHolder.productName = productsTableView.getSelectionModel().getSelectedItem().getName();
        NameHolder.productId = productsTableView.getSelectionModel().getSelectedItem().getId();
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    public void cancelOnClick(ActionEvent actionEvent) {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

}
