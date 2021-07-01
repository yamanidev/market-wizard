package controller.suppliers;

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

public class AddSupplierController {
    @FXML private TextField nameTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField registryTextField;
    @FXML private TextField nifTextField;
    @FXML private TextField nisTextField;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;

    public void cancelOnClick(ActionEvent actionEvent){
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    public void addSupplier(String name, String phoneNumber, String address,
                            String registry, String nif, String nis) {
        registry = !registry.isEmpty() ? registry : "None";
        nif = !nif.isEmpty() ? nif : "None";
        nis = !nis.isEmpty() ? nis : "None";

        System.out.println("registry = " + registry);
        System.out.println("nif = " + nif);
        System.out.println("nis = " + nis);
        String sqlQuery = "INSERT INTO suppliers (supplier_name, phone_number, address, registry, nif, nis)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setString(1, name);
            pstm.setString(2, phoneNumber);
            pstm.setString(3, address);
            pstm.setString(4, registry);
            pstm.setString(5, nif);
            pstm.setString(6, nis);
            pstm.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    public boolean validateFields(){
        return nameTextField.getText() != null && phoneNumberTextField.getText() != null &&
                addressTextField.getText() != null && !nameTextField.getText().trim().isEmpty() &&
                !phoneNumberTextField.getText().trim().isEmpty() && !addressTextField.getText().trim().isEmpty() &&
                HelperMethods.isAlpha(nameTextField.getText()) && HelperMethods.isNumeric(phoneNumberTextField.getText());
    }

    public void confirmOnClick(ActionEvent actionEvent){
        if (validateFields()){
            addSupplier(nameTextField.getText(), phoneNumberTextField.getText(),
                    addressTextField.getText(), registryTextField.getText(),
                    nifTextField.getText(), nisTextField.getText());
            ((Stage) cancelBtn.getScene().getWindow()).close();
        }
        else{
            HelperMethods.invalidFieldsAlert((Stage) cancelBtn.getScene().getWindow());
        }
    }
}
