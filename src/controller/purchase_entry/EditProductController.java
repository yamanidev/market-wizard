package controller.purchase_entry;

import app.utils.DBUtils;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String sqlQuery = "SELECT * from products where product_id = "
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
            quantityTextField.setText(String.valueOf(rs.getInt("quantity")));
            categoryTextField.setText(rs.getString("category"));
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void editProduct(String productName, double purchasedPrice, double soldPrice,
                            String expirationDate, int quantity, String category){

        String sqlQuery = "UPDATE products SET product_name = ?," +
                "purchased_price = ?," +
                "sold_price = ?," +
                "expiration_date = ?," +
                "quantity = ?," +
                "category = ?" +
                "WHERE product_id = " + NameHolder.productId;
        System.out.println(NameHolder.productId + " was given to sqlQuery");
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setString(1, productName);
            pstm.setDouble(2, purchasedPrice);
            pstm.setDouble(3, soldPrice);
            pstm.setString(4, expirationDate);
            pstm.setInt(5, quantity);
            pstm.setString(6, category);
            pstm.execute();
            c.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void confirmOnClick(ActionEvent actionEvent) {
        editProduct(productNameTextField.getText(),
                Double.parseDouble(purchasedPriceTextField.getText()),
                Double.parseDouble(soldPriceTextField.getText()),
                expirationDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                Integer.parseInt(quantityTextField.getText()),
                categoryTextField.getText());

        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    public void cancelOnClick(ActionEvent actionEvent) {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }
}
