package controller.purchase_entry;

import app.utils.HelperMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class PurchaseEntryController {

    @FXML
    public Button addInvoiceBtn;
    @FXML
    public Button editInvoiceBtn;
    @FXML
    public Button deleteInvoiceBtn;
    @FXML
    public Button addProductBtn;
    @FXML
    public Button editProductBtn;
    @FXML
    public Button deleteProductBtn;


    public void addInvoiceOnClick(ActionEvent actionEvent) throws IOException {
        HelperMethods helper = new HelperMethods();
        helper.openWindow("../../view/add-invoice.fxml", "Add Invoice");
    }

    public void editInvoiceOnclick(ActionEvent actionEvent) {
    }

    public void deleteInvoiceOnClick(ActionEvent actionEvent) {
    }

    public void addProductOnClick(ActionEvent actionEvent) {
    }

    public void editProductOnClick(ActionEvent actionEvent) {
    }

    public void deleteProductOnClick(ActionEvent actionEvent) {
    }
}
