package controller.purchase_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class EditInvoiceController implements Initializable {

    @FXML public Label selectedSupplierLabel;
    @FXML public Button selectBtn;
    @FXML public DatePicker dateOfPurchaseDatePicker;
    @FXML public Button confirmBtn;
    @FXML public Button cancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String sqlQuery = "SELECT * from invoices where invoice_id = "
                + NameHolder.invoiceId;
        try(Connection c = DBUtils.getConnection()){
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery(sqlQuery);
            selectedSupplierLabel.setText(rs.getString("supplier"));
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dateOfPurchaseDatePicker.setValue(
                    LocalDate.parse(rs.getString("date_of_purchase"), df));
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void editInvoice(String supplier, String dateOfPurchase){
        String sqlQuery = "UPDATE invoices SET supplier = ?," +
                "date_of_purchase = ?" +
                "WHERE invoice_id = " + NameHolder.invoiceId;
        try(Connection c = DBUtils.getConnection();
        PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setString(1, supplier);
            pstm.setString(2, dateOfPurchase);
            pstm.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void selectOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("purchase_entry/select-supplier.fxml",
                "Select Supplier");
        window.setOnHidden((e) ->{
            selectedSupplierLabel.setText(NameHolder.supplierName);
        });
    }

    public void cancelOnClick(ActionEvent actionEvent) {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    public void confirmOnClick(ActionEvent actionEvent) {
        editInvoice(selectedSupplierLabel.getText(),
                dateOfPurchaseDatePicker.getValue().
                        format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

}
