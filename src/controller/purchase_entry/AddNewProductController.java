package controller.purchase_entry;


import app.utils.DBUtils;
import app.utils.HelperMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static app.utils.HelperMethods.isNumeric;

public class AddNewProductController {
    @FXML public DatePicker expirationDatePicker;
    @FXML public TextField purchasedPriceTextField;
    @FXML public TextField soldPriceTextField;
    @FXML public TextField quantityTextField;
    @FXML public TextField categoryTextField;
    @FXML public TextField productNameTextField;
    @FXML public Button confirmBtn;
    @FXML public Button cancelBtn;


    public void addProduct(String productName, double purchasedPrice,
                           double soldPrice, int quantity, String category,
                           String expirationDate){
        String sqlQuery = "INSERT INTO products (product_name, purchased_price, sold_price," +
                "quantity, category, expiration_date)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setString(1, productName);
            pstm.setDouble(2, purchasedPrice);
            pstm.setDouble(3, soldPrice);
            pstm.setInt(4, quantity);
            pstm.setString(5, category);
            pstm.setString(6, expirationDate);
            pstm.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public boolean validateFields(){
        String purchasedPrice = purchasedPriceTextField.getText();
        String soldPrice = soldPriceTextField.getText();
        String quantity = quantityTextField.getText();
        String category = categoryTextField.getText();
        String productName = productNameTextField.getText();
        return expirationDatePicker.getValue() != null &&
                purchasedPrice != null && !purchasedPrice.isEmpty() &&
                soldPrice != null && !soldPrice.isEmpty() &&
                quantity != null && !quantity.isEmpty() &&
                category != null && !category.isEmpty() &&
                productName != null && !productName.isEmpty() &&
                isNumeric(purchasedPrice) && isNumeric(quantity) &&
                isNumeric(soldPrice);
    }


    public void confirmOnClick(ActionEvent actionEvent){
        if (validateFields()){
            addProduct(productNameTextField.getText(),
                    Double.parseDouble(purchasedPriceTextField.getText()),
                    Double.parseDouble(soldPriceTextField.getText()),
                    Integer.parseInt(quantityTextField.getText()),
                    categoryTextField.getText(), expirationDatePicker.getValue().
                            format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            ((Stage) cancelBtn.getScene().getWindow()).close();
        }
        else {
            HelperMethods.invalidFieldsAlert((Stage) cancelBtn.getScene().getWindow());
        }
    }

    public void cancelOnClick(ActionEvent actionEvent) {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }
}