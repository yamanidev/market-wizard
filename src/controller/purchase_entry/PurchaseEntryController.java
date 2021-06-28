package controller.purchase_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Invoice;
import model.Product;
import model.Supplier;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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

    // Top nav
    @FXML public Button dashboardBtn;
    @FXML public Button sellingBtn;
    @FXML public Button stockBtn;
    @FXML public Button suppliersBtn;
    @FXML public Button customersBtn;

    // Slider
    @FXML public Circle imageCircle ;
    @FXML public Pane openSliderPane;
    @FXML public Pane closeSliderPane;
    @FXML public ImageView openSliderImage;
    @FXML public AnchorPane slider;
    @FXML public AnchorPane x;

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
                    updateProducts();
                }
        );


        slider.setTranslateX(-255);
        openSliderPane.setVisible(true);
        closeSliderPane.setVisible(false);

        openSliderImage.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(0);
            slide.play();
            slider.setTranslateX(-255);
            slide.setOnFinished((ActionEvent e)-> {
                openSliderPane.setVisible(false);
                closeSliderPane.setVisible(true);
            });
        });

        openSliderPane.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(0);
            slide.play();
            slider.setTranslateX(-255);
            slide.setOnFinished((ActionEvent e)-> {
                openSliderPane.setVisible(false);
                closeSliderPane.setVisible(true);
            });
        });

        closeSliderPane.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(-255);
            slide.play();
            slider.setTranslateX(0);
            slide.setOnFinished((ActionEvent e)-> {
                openSliderPane.setVisible(true);
                closeSliderPane.setVisible(false);
            });
        });

        x.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);
            slide.setToX(-255);
            slide.play();
            slider.setTranslateX(0);
            slide.setOnFinished((ActionEvent e)-> {
                openSliderPane.setVisible(true);
                closeSliderPane.setVisible(false);
            });
        });

    }

    private ObservableList<Invoice> getInvoices() {
        ObservableList<Invoice> list = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * FROM invoices";

        try(Connection c = DBUtils.getConnection()) {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
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

    private ObservableList<Product> getProducts(int invoiceId){
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
        try (Connection c = DBUtils.getConnection();
             PreparedStatement pstm = c.prepareStatement(sqlQuery)){

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

    public void updateProducts(){
        if (invoicesTableView.getSelectionModel().getSelectedItem() != null)
            productsTableView.setItems(getProducts(
                invoicesTableView.getSelectionModel().getSelectedItem().getId()
        ));
        else {
            productsTableView.getItems().clear();
        }
    }

    public void addInvoiceOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("purchase_entry/add-invoice.fxml",
                "Add Invoice");
        window.setOnHidden((e) -> {
            updateInvoices();
        });
    }

    public void editInvoiceOnclick(ActionEvent actionEvent) throws IOException {
        NameHolder.invoiceId = invoicesTableView.getSelectionModel().getSelectedItem().getId();

        Stage window = HelperMethods.openWindow("purchase_entry/edit-invoice.fxml",
                "Edit Invoice");
        window.setOnHidden((e) -> {
            updateInvoices();
        });
    }

    public void deleteInvoiceOnClick(ActionEvent actionEvent) {
        String sqlQuery = "DELETE FROM invoices WHERE invoice_id = "
                + invoicesTableView.getSelectionModel().getSelectedItem().getId();
        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            st.executeUpdate(sqlQuery);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        updateInvoices();
        updateProducts();
    }

    public void addProductOnClick(ActionEvent actionEvent) throws IOException{
        Stage window = HelperMethods.openWindow("purchase_entry/add-product.fxml", "Add product");
        NameHolder.invoiceId = invoicesTableView.getSelectionModel().
                getSelectedItem().getId();
        window.setOnHidden((e) -> {
            updateProducts();
        });
    }

    public void editProductOnClick(ActionEvent actionEvent) throws IOException {
        NameHolder.productId = productsTableView.getSelectionModel().getSelectedItem().getId();
        Stage window = HelperMethods.openWindow("purchase_entry/edit-product.fxml",
                "Edit Product");
        window.setOnHidden((e) -> {
            productsTableView.getSelectionModel().clearSelection();
            updateProducts();
        });
    }

    public void deleteProductOnClick(ActionEvent actionEvent) {
        String sqlQuery = "DELETE FROM invoices_products WHERE " +
                "invoice_id = " +
                invoicesTableView.getSelectionModel().getSelectedItem().getId() +
                " AND product_id = " +
                productsTableView.getSelectionModel().getSelectedItem().getId();
        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            st.executeUpdate(sqlQuery);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        updateProducts();
    }

    public void dashboardOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/home/drakkath/IdeaProjects/MarketWizard/src/view/dashboard/dashboard.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void sellingOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/home/drakkath/IdeaProjects/MarketWizard/src/view/selling_entry/selling_entry.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void stockOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/home/drakkath/IdeaProjects/MarketWizard/src/view/stock/stock.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void customersOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/home/drakkath/IdeaProjects/MarketWizard/src/view/customers/customers.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void suppliersOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/home/drakkath/IdeaProjects/MarketWizard/src/view/suppliers/suppliers.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }
}
