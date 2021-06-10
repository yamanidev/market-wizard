package controller.purchase_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class AddInvoiceController {

    @FXML public Button selectBtn;
    @FXML public DatePicker dateOfPurchaseDatePicker;
    @FXML public Button confirmBtn;
    @FXML public Button cancelBtn;
    @FXML public Label selectedSupplierLabel;
    final Connection c = DBUtils.getConnection();

    public void addInvoice(String supplier, String dateOfPurchase){
        String sqlQuery = "INSERT INTO invoices (supplier, date_of_purchase)" +
                "VALUES (?, ?)";
        try(PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setString(1, supplier);
            pstm.setString(2, dateOfPurchase);
            pstm.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public boolean validateFields(){
        if (dateOfPurchaseDatePicker.getValue() == null ||
                selectedSupplierLabel.getText().equals("Label")){
            return false;
        }
        else{
            return true;
        }
    }

    public void selectOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("select-supplier.fxml",
                "Something");
        window.setOnHidden((e) ->{
            selectedSupplierLabel.setText(NameHolder.supplierName);
        });
    }

    public void cancelOnClick(ActionEvent actionEvent) {
        Stage window = (Stage) cancelBtn.getScene().getWindow();
        window.close();
    }

    public void confirmOnClick(ActionEvent actionEvent) {
        if (validateFields()){
            addInvoice(selectedSupplierLabel.getText(),
                    dateOfPurchaseDatePicker.getValue().
                            format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            Stage window = (Stage) cancelBtn.getScene().getWindow();
            window.close();
        }
        else{
            HelperMethods.emptyFieldsAlert((Stage) cancelBtn.getScene().getWindow());
        }
    }
}
