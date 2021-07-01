package controller.suppliers;

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

public class EditSupplierController implements Initializable {
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField registryTextField;

    @FXML
    private TextField nifTextField;

    @FXML
    private TextField nisTextField;

    @FXML
    private Button confirmBtn;

    @FXML
    private Button cancelBtn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String sqlQuery = "SELECT supplier_name, phone_number, address, registry," +
                "nis, nif FROM suppliers WHERE supplier_id = "
                + NameHolder.supplierId;
        try (Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            nameTextField.setText(rs.getString("supplier_name"));
            phoneNumberTextField.setText(rs.getString("phone_number"));
            addressTextField.setText(rs.getString("address"));
            registryTextField.setText(rs.getString("registry"));
            nisTextField.setText(rs.getString("nis"));
            nifTextField.setText(rs.getString("nif"));
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    private boolean validateFields(){
        return nameTextField.getText() != null && phoneNumberTextField.getText() != null &&
                addressTextField.getText() != null && !registryTextField.getText().trim().isEmpty()
                && !nifTextField.getText().trim().isEmpty()&& !nisTextField.getText().trim().isEmpty()
                ;
    }

    public void editSupplier(int supplierId, String supplierName, String phoneNumber, String address,
                            String registry, String nis, String nif){

        String sqlQuery = "UPDATE suppliers SET supplier_name = ?," +
                "phone_number = ?," +
                "address = ?," +
                "registry = ?," +
                "nis = ?," +
                "nif = ? " +
                "WHERE supplier_id = " + supplierId;
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setString(1, supplierName);
            pstm.setString(2, phoneNumber);
            pstm.setString(3, address);
            pstm.setString(4, registry);
            pstm.setString(5, nis);
            pstm.setString(6, nif);
            pstm.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void confirmOnClick(ActionEvent event) {
        if (validateFields()){
            editSupplier(NameHolder.supplierId, nameTextField.getText(),
                    phoneNumberTextField.getText(),
                    addressTextField.getText(),
                    registryTextField.getText(),
                    nifTextField.getText(),
                    nisTextField.getText());
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
