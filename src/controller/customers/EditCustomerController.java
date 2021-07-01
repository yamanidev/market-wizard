package controller.customers;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EditCustomerController implements Initializable {
    @FXML private TextField nameTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField emailTextField;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String sqlQuery = "SELECT customer_name, customer_phone_number, customer_email " +
                "FROM customers WHERE customer_id = " + NameHolder.customerId;
        try (Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            nameTextField.setText(rs.getString("customer_name"));
            phoneNumberTextField.setText(rs.getString("customer_phone_number"));
            emailTextField.setText(rs.getString("customer_email"));
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private boolean validateFields(){
        return nameTextField.getText() != null && phoneNumberTextField.getText() != null &&
                emailTextField.getText() != null && !nameTextField.getText().isEmpty() &&
                !phoneNumberTextField.getText().isEmpty() && !emailTextField.getText().isEmpty() &&
                HelperMethods.isAlpha(nameTextField.getText()) && HelperMethods.isNumeric(phoneNumberTextField.getText()) &&
                HelperMethods.isEmail(emailTextField.getText());
    }

    private void editCustomer(int id, String name, String phoneNumber, String email){
        String sqlQuery = "UPDATE customers SET customer_name = ?, " +
                "customer_phone_number = ?, customer_email = ?" +
                "WHERE customer_id  = " + id;
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setString(1, name);
            pstm.setString(2, phoneNumber);
            pstm.setString(3, email);
            pstm.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void confirmOnClick(ActionEvent actionEvent) {
        if (validateFields()){
            editCustomer(NameHolder.customerId, nameTextField.getText(), phoneNumberTextField.getText(),
                    emailTextField.getText());
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
