package controller.purchase_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Invoice;
import model.Product;
import model.Supplier;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PurchaseEntryController implements Initializable {
    @FXML public Button addInvoiceBtn;
    @FXML public Button editInvoiceBtn;
    @FXML public Button deleteInvoiceBtn;
    @FXML public Button addProductBtn;
    @FXML public Button editProductBtn;
    @FXML public Button deleteProductBtn;

    @FXML public TableView<Invoice> invoicesTableView;
    @FXML public TableColumn<Invoice, Integer> invoiceIdCol;
    @FXML public TableColumn<Invoice, String> dateOfPurchaseCol;
    @FXML public TableColumn<Invoice, String> supplierCol;

    @FXML public TableView<Product> productsTableView;
    @FXML public TableColumn<Product, Integer> productIdCol;
    @FXML public TableColumn<Product, String> productCol;
    @FXML public TableColumn<Product, Double> priceOfUnitCol;
    @FXML public TableColumn<Product, Integer> quantityCol;
    @FXML public TableColumn<Product, String> categoryCol;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        dateOfPurchaseCol.setCellValueFactory(new PropertyValueFactory<>("DateOfPurchase"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("Supplier"));
        invoicesTableView.setItems(getInvoices());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        productCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        priceOfUnitCol.setCellValueFactory(new PropertyValueFactory<>("PurhcasedPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));

        editInvoiceBtn.disableProperty().bind(Bindings.isEmpty(invoicesTableView
                .getSelectionModel().getSelectedItems()));
        deleteInvoiceBtn.disableProperty().bind(Bindings.isEmpty(invoicesTableView
                .getSelectionModel().getSelectedItems()));

        addProductBtn.disableProperty().bind(Bindings.isEmpty(invoicesTableView
                .getSelectionModel().getSelectedItems()));
        editProductBtn.disableProperty().bind(Bindings.isEmpty(productsTableView
                .getSelectionModel().getSelectedItems()));
        deleteProductBtn.disableProperty().bind(Bindings.isEmpty(productsTableView
                .getSelectionModel().getSelectedItems()));
    }

    private ObservableList<Invoice> getInvoices() {
        ObservableList<Invoice> list = FXCollections.observableArrayList();
        Connection c = DBUtils.getConnection();
        String sqlQuery = "select * from invoices";
        Statement st;
        ResultSet rs;
        try {
            st = c.createStatement();
            rs = st.executeQuery(sqlQuery);
            Invoice invoice;

            while(rs.next()){
                invoice = new Invoice(rs.getInt("id"),
                        rs.getString("supplier"),
                        rs.getString("date_of_purchase"));

                list.add(invoice);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public void updateInvoices(){
        invoicesTableView.setItems(getInvoices());
    }

    public void addInvoiceOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("add-invoice.fxml",
                "Add Invoice");
        window.setOnHidden((e) -> {
            updateInvoices();
        });
    }

    public void editInvoiceOnclick(ActionEvent actionEvent) {
    }

    public void deleteInvoiceOnClick(ActionEvent actionEvent) {
    }

    public void addProductOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("select-product.fxml", "Select product");

    }

    public void editProductOnClick(ActionEvent actionEvent) {
    }

    public void deleteProductOnClick(ActionEvent actionEvent) {
    }
}
