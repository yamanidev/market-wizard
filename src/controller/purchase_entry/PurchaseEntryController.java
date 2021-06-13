package controller.purchase_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
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
import java.sql.SQLException;
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
    final Connection c = DBUtils.getConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        dateOfPurchaseCol.setCellValueFactory(new PropertyValueFactory<>("DateOfPurchase"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("Supplier"));
        invoicesTableView.setItems(getInvoices());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        productCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        priceOfUnitCol.setCellValueFactory(new PropertyValueFactory<>("PurchasedPrice"));
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


        invoicesTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) ->{
                    System.out.println(newValue.getId());
                    try {
                        updateProducts();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
        );

    }

    private ObservableList<Invoice> getInvoices() {
        ObservableList<Invoice> list = FXCollections.observableArrayList();
        String sqlQuery = "select * from invoices";
        Statement st;
        ResultSet rs;
        try {
            st = c.createStatement();
            rs = st.executeQuery(sqlQuery);
            Invoice invoice;

            while(rs.next()){
                invoice = new Invoice(rs.getInt("invoice_id"),
                        rs.getString("supplier"),
                        rs.getString("date_of_purchase"));

                list.add(invoice);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    private ObservableList<Product> getProducts(int invoiceId) throws SQLException {
        ObservableList<Product> list = FXCollections.observableArrayList();
        String sqlQuery = "select products.product_id, products.product_name," +
                "products.purchased_price, products.expiration_date," +
                "products.sold_price, products.quantity," +
                "products.category from products\n" +
                "join invoices_products on" +
                "(products.product_id = invoices_products.product_id)\n" +
                "join invoices on" +
                "(invoices.invoice_id = invoices_products.invoice_id)\n" +
                "where invoices.invoice_id = " + invoiceId;

        Product product;
        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            while(rs.next()){
                product = new Product(rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("purchased_price"),
                        rs.getDouble("sold_price"),
                        rs.getString("expiration_date"),
                        rs.getString("category"),
                        rs.getInt("quantity"));
                list.add(product);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public void updateInvoices(){
        invoicesTableView.setItems(getInvoices());
    }

    public void updateProducts() throws SQLException {
        productsTableView.setItems(getProducts(
                invoicesTableView.getSelectionModel().getSelectedItem().getId()
        ));
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

    public void addProductOnClick(ActionEvent actionEvent) throws IOException{
        Stage window = HelperMethods.openWindow("select-product.fxml", "Select product");
        NameHolder.invoiceId = invoicesTableView.getSelectionModel().
                getSelectedItem().getId();
        window.setOnHidden((e) -> {
            try {
                updateProducts();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void editProductOnClick(ActionEvent actionEvent) {
    }

    public void deleteProductOnClick(ActionEvent actionEvent) {
    }
}
