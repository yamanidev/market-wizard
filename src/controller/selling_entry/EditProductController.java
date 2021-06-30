package controller.selling_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EditProductController implements Initializable {
    @FXML private Label selectedProductLabel;
    @FXML private TextField quantityTextField;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedProductLabel.setText(NameHolder.productName);
        
        String sqlQuery = "SELECT quantity FROM bills_products WHERE bill_id = " + NameHolder.billId +
                " AND product_id = " + NameHolder.productId;

        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            quantityTextField.setText(String.valueOf(rs.getInt("quantity")));
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        quantityTextField.requestFocus();
    }

    private void editProduct(int billId, int productId, int quantity){
        int previousQuantity = 0;
        // Getting previously added quantity from invoices_products
        String sqlQuery = "SELECT quantity FROM bills_products " +
                "WHERE bill_id = " + NameHolder.billId + " AND product_id = " + NameHolder.productId;

        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            previousQuantity = rs.getInt("quantity");
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        int newQuantity = previousQuantity - quantity;

        sqlQuery = "UPDATE products SET quantity = ? WHERE product_id = " + productId;
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setInt(1, newQuantity);
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        sqlQuery = "UPDATE bills_products SET quantity = " + quantity +
                " WHERE bill_id = " + NameHolder.billId + " AND product_id = " + NameHolder.productId;
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    public void confirmOnClick(ActionEvent actionEvent) {
        if (quantityTextField.getText() != null && !quantityTextField.getText().isEmpty() &&
                HelperMethods.isNumeric(quantityTextField.getText())){
            editProduct(NameHolder.billId, NameHolder.productId,
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
