package controller.purchase_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddSupplierController {
    @FXML
    public Button confirmBtn;
    @FXML
    public Button cancelBtn;
    @FXML
    public TextField wilayaTextField;
    @FXML
    public TextField phoneNumberTextField;
    @FXML
    public TextField fullNameTextField;

    public void cancelOnClick(ActionEvent actionEvent) {
        Stage window = (Stage) cancelBtn.getScene().getWindow();
        window.close();
    }

    public void addSupplier(String fullName, String phoneNumber, String wilaya) throws SQLException {
        String sqlQuery = "INSERT INTO suppliers (full_name, phone_number, wilaya) VALUES (?, ?, ?)";
        try(Connection c = DBUtils.getConnectionWith("suppliers.db");
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setString(1, fullName);
            pstm.setString(2, phoneNumber);
            pstm.setString(3, wilaya);
            pstm.execute();

            System.out.println(pstm.toString());
        }
        catch (SQLException e){
            e.printStackTrace();

            System.out.println(e.getMessage());
        }
    }


    public boolean validateFields(){
        String fullName = fullNameTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String wilaya = wilayaTextField.getText();

        if (fullName == null || fullName.isEmpty() || phoneNumber == null ||
        phoneNumber.isEmpty() || wilaya == null || wilaya.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }



    public void confirmOnClick(ActionEvent actionEvent) throws SQLException {
        if (validateFields()){
            addSupplier(fullNameTextField.getText(), phoneNumberTextField.getText(),
                    wilayaTextField.getText());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/drakkath/IdeaProjects/MarketWizard/src/view/select-supplier.fxml"));
            SelectSupplierController x = (SelectSupplierController) loader.getController();

            // Closing the window afterwards
            Stage window = (Stage) cancelBtn.getScene().getWindow();
            window.close();
        }
        else{
            // Pick any injected FXML element in order to get the stage
            HelperMethods.emptyFieldsAlert((Stage) cancelBtn.getScene().getWindow());
        }
    }
}
