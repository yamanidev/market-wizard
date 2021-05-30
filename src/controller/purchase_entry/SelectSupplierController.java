package controller.purchase_entry;

import app.utils.HelperMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectSupplierController {
    @FXML
    public Button newSupplierBtn;
    @FXML
    public Button cancelBtn;
    @FXML
    public Button confirmBtn;

    public void cancelOnClick(ActionEvent actionEvent) {
        Stage window = (Stage) cancelBtn.getScene().getWindow();
        window.close();
    }


    public void newSupplierOnClick(ActionEvent actionEvent) throws IOException {
        HelperMethods helper = new HelperMethods();
        helper.openWindow("../../view/add-supplier.fxml", "Add New Supplier");
    }
}
