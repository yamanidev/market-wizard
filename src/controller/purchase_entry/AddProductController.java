package controller.purchase_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddProductController {

    @FXML private Label selectedProductLabel;
    @FXML private TextField quantityTextField;
    @FXML private TextField purchasedPriceTextField;
    @FXML private TextField soldPriceTextField;
    @FXML private DatePicker expirationDatePicker;
    @FXML private Button selectBtn;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;

    private boolean validateFields(){
        return quantityTextField.getText() != null && purchasedPriceTextField.getText() != null &&
                soldPriceTextField.getText() != null && !quantityTextField.getText().trim().isEmpty() &&
                !purchasedPriceTextField.getText().trim().isEmpty() && !soldPriceTextField.getText().trim().isEmpty()
                && !(quantityTextField.getText().charAt(0) == '0');
    }

    private void updateProduct(int productId, int quantity, double purchasedPrice, double soldPrice){
        String sqlQuery = "UPDATE products SET quantity = (quantity + ?)," +
                "purchased_price = ?," +
                "sold_price = ? " +
                "WHERE product_id = " + productId;
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setInt(1, quantity);
            pstm.setDouble(2, purchasedPrice);
            pstm.setDouble(3, soldPrice);
            pstm.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void addProductToInvoice(int invoiceId, int productId, int productQuantity){
        String sqlQuery = "INSERT INTO invoices_products (invoice_id," +
                "product_id, product_quantity) VALUES (?, ?, ?)";

        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)) {
            pstm.setInt(1, invoiceId);
            pstm.setInt(2, productId);
            pstm.setInt(3, productQuantity);
            pstm.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @FXML void selectOnClick(ActionEvent event) throws IOException {
        Stage window = HelperMethods.openWindow("purchase_entry/select-product.fxml",
                "Select product");
        window.setOnHidden((e) ->{
            selectedProductLabel.setText(NameHolder.productName);

            String sqlQuery = "SELECT * FROM products WHERE product_id = " + NameHolder.productId;
            try(Connection c = DBUtils.getConnection()) {
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery(sqlQuery);
                quantityTextField.setText("0");
                purchasedPriceTextField.setText(String.valueOf(rs.getDouble("purchased_price")));
                soldPriceTextField.setText(String.valueOf(rs.getDouble("sold_price")));
                DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                expirationDatePicker.setValue(
                        LocalDate.parse(rs.getString("expiration_date"), df));
            }
            catch (SQLException ex){
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }
        });
    }

    @FXML void confirmOnClick(ActionEvent event) {
        if (validateFields()){
            updateProduct(NameHolder.productId, Integer.parseInt(quantityTextField.getText()),
                    Double.parseDouble(purchasedPriceTextField.getText()),
                    Double.parseDouble(soldPriceTextField.getText()));
            addProductToInvoice(NameHolder.invoiceId, NameHolder.productId,
                    Integer.parseInt(quantityTextField.getText()));
            ((Stage) cancelBtn.getScene().getWindow()).close();
        }
        else{
            HelperMethods.invalidFieldsAlert((Stage) cancelBtn.getScene().getWindow());
        }
    }

    @FXML void cancelOnClick(ActionEvent event) {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

}
