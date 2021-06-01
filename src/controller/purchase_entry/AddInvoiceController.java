package controller.purchase_entry;

import app.utils.HelperMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Supplier;
import model.SupplierNameHolder;

import java.io.IOException;

public class AddInvoiceController {

    @FXML public Button selectBtn;
    @FXML public DatePicker dateOfPurchaseDatePicker;
    @FXML public Button confirmBtn;
    @FXML public Button cancelBtn;
    @FXML public Label selectedSupplierLabel;



    public void selectOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("select-supplier.fxml", "Something");
        window.setOnHidden((e) ->{
            selectedSupplierLabel.setText(SupplierNameHolder.name);
        });
    }


    public void cancelOnClick(ActionEvent actionEvent) {
        Stage window = (Stage) cancelBtn.getScene().getWindow();
        window.close();
    }

    public void confirmOnClick(ActionEvent actionEvent) {
    }
}
