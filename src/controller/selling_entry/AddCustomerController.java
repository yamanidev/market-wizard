package controller.selling_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCustomerController {
    @FXML private TextField nameTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField emailTextField;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;

    private void addCustomer(String name, String phoneNumber, String email){
        String sqlQuery = "INSERT INTO customers (customer_name, customer_phone_number, customer_email)" +
                "VALUES (?, ?, ?)";
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setString(1, name);
            pstm.setString(2, phoneNumber);
            pstm.setString(3, email);
            pstm.execute();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


    private boolean validateFields(){
        return nameTextField.getText() != null && phoneNumberTextField.getText() != null &&
                emailTextField.getText() != null && !nameTextField.getText().isEmpty() &&
                !phoneNumberTextField.getText().isEmpty() && !emailTextField.getText().isEmpty() &&
                HelperMethods.isAlpha(nameTextField.getText()) && HelperMethods.isNumeric(phoneNumberTextField.getText());
    }

    public void confirmOnClick(ActionEvent actionEvent) {
        if (validateFields()){
            addCustomer(nameTextField.getText(), phoneNumberTextField.getText(), emailTextField.getText());
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
