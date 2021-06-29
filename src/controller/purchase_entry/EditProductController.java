package controller.purchase_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class EditProductController implements Initializable {

    @FXML public DatePicker expirationDatePicker;
    @FXML public TextField purchasedPriceTextField;
    @FXML public TextField soldPriceTextField;
    @FXML public TextField quantityTextField;
    @FXML public TextField categoryTextField;
    @FXML public TextField productNameTextField;
    @FXML public Button confirmBtn;
    @FXML public Button cancelBtn;

    private boolean validateFields(){
        return quantityTextField.getText() != null && purchasedPriceTextField.getText() != null &&
                soldPriceTextField.getText() != null && !quantityTextField.getText().trim().isEmpty() &&
                !purchasedPriceTextField.getText().trim().isEmpty() && !soldPriceTextField.getText().trim().isEmpty() &&
                !(quantityTextField.getText().charAt(0) == '0') && HelperMethods.isNumeric(quantityTextField.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String sqlQuery = "SELECT product_name, purchased_price, sold_price, expiration_date," +
                "category FROM products WHERE product_id = "
                + NameHolder.productId;
        try (Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            productNameTextField.setText(rs.getString("product_name"));
            purchasedPriceTextField.setText(
                    String.valueOf(rs.getDouble("purchased_price")));
            soldPriceTextField.setText(
                    String.valueOf(rs.getDouble("sold_price")));
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            expirationDatePicker.setValue(
                    LocalDate.parse(rs.getString("expiration_date"), df)
            );
            categoryTextField.setText(rs.getString("category"));
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        sqlQuery = "SELECT product_quantity FROM invoices_products WHERE invoice_id = " + NameHolder.invoiceId +
                " AND product_id = " + NameHolder.productId;
        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            quantityTextField.setText(String.valueOf(rs.getInt("product_quantity")));
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    public void editProduct(int productId, String productName, double purchasedPrice, double soldPrice,
                            String expirationDate, int quantity, String category){
        int previousQuantity = 0;
        // Getting previously added quantity from invoices_products
        String sqlQuery = "SELECT product_quantity FROM invoices_products " +
                "WHERE invoice_id = " + NameHolder.invoiceId + " AND product_id = " + NameHolder.productId;

        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            previousQuantity = rs.getInt("product_quantity");
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        // Calculating the quantity to be added
        int newQuantity = quantity - previousQuantity;

        sqlQuery = "UPDATE products SET product_name = ?," +
                "purchased_price = ?," +
                "sold_price = ?," +
                "expiration_date = ?," +
                "quantity = (quantity + ?)," +
                "category = ?" +
                "WHERE product_id = " + productId;
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setString(1, productName);
            pstm.setDouble(2, purchasedPrice);
            pstm.setDouble(3, soldPrice);
            pstm.setString(4, expirationDate);
            pstm.setInt(5, newQuantity);
            pstm.setString(6, category);
            pstm.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


        sqlQuery = "UPDATE invoices_products SET product_quantity = " + quantity +
        " WHERE invoice_id = " + NameHolder.invoiceId + " AND product_id = " + NameHolder.productId;
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
        if (validateFields()){
            editProduct(NameHolder.productId, productNameTextField.getText(),
                    Double.parseDouble(purchasedPriceTextField.getText()),
                    Double.parseDouble(soldPriceTextField.getText()),
                    expirationDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    Integer.parseInt(quantityTextField.getText()),
                    categoryTextField.getText());
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
