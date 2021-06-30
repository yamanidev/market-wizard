package controller.selling_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class AddProductController {
    @FXML private Button cancelBtn;
    @FXML private Label selectedProductLabel;
    @FXML private TextField quantityTextField;

    private void updateProduct(int productId, int quantity){
        String sqlQuery = "UPDATE products SET quantity = (quantity - ?)" +
                "WHERE product_id = " + productId;
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setInt(1, quantity);
            pstm.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void addProductToBill(int billId, int productId, int productQuantity){
        String sqlQuery = "INSERT INTO bills_products (bill_id, product_id, quantity)" +
                "VALUES (?, ?, ?)";

        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)) {
            pstm.setInt(1, billId);
            pstm.setInt(2, productId);
            pstm.setInt(3, productQuantity);
            pstm.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void selectOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("selling_entry/select-product.fxml",
                "Select product");
        NameHolder.productName = "Product's name";
        window.setOnHidden((e) ->{
            if (!NameHolder.productName.equals("Product's name")){
                selectedProductLabel.setText(NameHolder.productName);
                quantityTextField.setText("0");
                quantityTextField.requestFocus();
            }
        });
    }

    public void confirmOnClick(ActionEvent actionEvent) {
        if (HelperMethods.isNumeric(quantityTextField.getText()) &&
                !selectedProductLabel.getText().equals("Product's name")){
            updateProduct(NameHolder.productId, Integer.parseInt(quantityTextField.getText()));
            addProductToBill(NameHolder.billId, NameHolder.productId,
                    Integer.parseInt(quantityTextField.getText()));
            ((Stage) cancelBtn.getScene().getWindow()).close();
        }
        else{
            HelperMethods.invalidFieldsAlert((Stage) cancelBtn.getScene().getWindow());
        }
    }

    public void cancelOnClick(ActionEvent actionEvent) {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }
}
