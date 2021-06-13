package controller.purchase_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SelectProductController implements Initializable {
    public Button confirmBtn;
    public Button cancelBtn;
    public Button newProductBtn;
    public TableView<Product> productsTableView;
    public TableColumn<Product, String> productNameCol;
    public TableColumn<Product, Double> purchasedPriceCol;
    public TableColumn<Product, Double> soldPriceCol;
    public TableColumn<Product,Integer> quantityCol;
    public TableColumn<Product, String> categoryCol;
    public TableColumn<Product, String> expirationDateCol;
    public TableColumn<Product, Integer> productIdCol;

    final Connection c = DBUtils.getConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmBtn.disableProperty().bind(Bindings.isEmpty(productsTableView
                .getSelectionModel().getSelectedItems()));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        purchasedPriceCol.setCellValueFactory(new PropertyValueFactory<>("purchasedPrice"));
        soldPriceCol.setCellValueFactory(new PropertyValueFactory<>("soldPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        expirationDateCol.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        updateProducts();
    }

    public void addProductToInvoice(){
        String sqlQuery = "INSERT INTO invoices_products (invoice_id," +
                "product_id) VALUES (?, ?)";

        try(PreparedStatement pstm = c.prepareStatement(sqlQuery)) {
            pstm.setInt(1, NameHolder.invoiceId);
            pstm.setInt(2, productsTableView.getSelectionModel().
                    getSelectedItem().getId());
            pstm.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public ObservableList<Product> getProducts(){
        ObservableList<Product> list = FXCollections.observableArrayList();
        Connection c = DBUtils.getConnection();
        String sqlQuery = "select * from products";
        Statement st;
        ResultSet rs;
        try {
            st = c.createStatement();
            rs = st.executeQuery(sqlQuery);
            Product product;

            while(rs.next()){
                product = new Product(rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("purchased_price"),
                        rs.getDouble("sold_price"),
                        rs.getString("expiration_date"),
                        rs.getString("category"),
                        rs.getInt("quantity"));

                list.add(product);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public void newProductOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("add-product.fxml",
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
        addProductToInvoice();
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }


}
