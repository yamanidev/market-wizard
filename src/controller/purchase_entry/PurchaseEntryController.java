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
import javafx.scene.control.Label;
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
    @FXML private Button endTransationBtn;
    @FXML private Button addInvoiceBtn;
    @FXML private Button editInvoiceBtn;
    @FXML private Button deleteInvoiceBtn;
    @FXML private Button addProductBtn;
    @FXML private Button editProductBtn;
    @FXML private Button deleteProductBtn;
    @FXML private Label totalPriceLabel;

    @FXML private TableView<Invoice> invoicesTableView;
    @FXML private TableColumn<Invoice, Integer> invoiceIdCol;
    @FXML private TableColumn<Invoice, String> dateOfPurchaseCol;
    @FXML private TableColumn<Invoice, String> supplierCol;

    @FXML private TableView<Product> productsTableView;
    @FXML private TableColumn<Product, Integer> productIdCol;
    @FXML private TableColumn<Product, String> productCol;
    @FXML private TableColumn<Product, Double> priceOfUnitCol;
    @FXML private TableColumn<Product, Integer> quantityCol;
    @FXML private TableColumn<Product, String> categoryCol;
    @FXML private TableColumn<Product, Double> totalCol;

    // Top nav
    @FXML private Button dashboardBtn;
    @FXML private Button sellingBtn;
    @FXML private Button stockBtn;
    @FXML private Button suppliersBtn;
    @FXML private Button customersBtn;

    // Slider
    @FXML private Circle imageCircle ;
    @FXML private Pane openSliderPane;
    @FXML private Pane closeSliderPane;
    @FXML private ImageView openSliderImage;
    @FXML private AnchorPane slider;
    @FXML private AnchorPane x;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateOfPurchaseCol.setCellValueFactory(new PropertyValueFactory<>("dateOfPurchase"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        invoicesTableView.setItems(getInvoices());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceOfUnitCol.setCellValueFactory(new PropertyValueFactory<>("purchasedPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

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
                    endTransationBtn.disableProperty().bind(Bindings.isEmpty(productsTableView.getItems()));
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

    private double getTotal(){
        double total = 0;
        for (Product product : productsTableView.getItems()){
            total += product.getPurchasedPrice() * product.getQuantity();
        }
        return total;
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
//        String sqlQuery = "SELECT products.product_id, products.product_name," +
//                "products.purchased_price, products.expiration_date," +
//                "products.sold_price, products.quantity," +
//                "products.category FROM products\n" +
//                "JOIN invoices_products ON" +
//                "(products.product_id = invoices_products.product_id)\n" +
//                "JOIN invoices ON" +
//                "(invoices.invoice_id = invoices_products.invoice_id)\n" +
//                "WHERE invoices.invoice_id = " + invoiceId;

        String sqlQuery = "SELECT\n" +
                "products.product_id, products.product_name, \n" +
                "invoices_products.product_quantity, products.purchased_price,\n" +
                "products.sold_price, products.category, \n" +
                "products.expiration_date\n" +
                "FROM products JOIN invoices_products ON (products.product_id = invoices_products.product_id)\n" +
                "JOIN invoices ON (invoices.invoice_id = invoices_products.invoice_id)\n" +
                "WHERE invoices.invoice_id = " + invoiceId;

        Product product;
        try (Connection c = DBUtils.getConnection()){

            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            while(rs.next()){
                product = new Product(rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("purchased_price"),
                        rs.getDouble("sold_price"),
                        rs.getString("expiration_date"),
                        rs.getString("category"),
                        rs.getInt("product_quantity"),
                        rs.getInt("product_quantity") * rs.getDouble("purchased_price"));
                list.add(product);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    private void updateInvoices(){
        invoicesTableView.setItems(getInvoices());
    }

    private void updateProducts() {
        if (invoicesTableView.getSelectionModel().getSelectedItem() != null)
            productsTableView.setItems(getProducts(
                invoicesTableView.getSelectionModel().getSelectedItem().getId()
        ));
        else {
            productsTableView.getItems().clear();
        }
        totalPriceLabel.setText(String.format("%.2f", getTotal()));
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
        NameHolder.invoiceId = invoicesTableView.getSelectionModel().getSelectedItem().getId();
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
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        sqlQuery = "UPDATE products SET quantity = (quantity - " +
        productsTableView.getSelectionModel().getSelectedItem().getQuantity() + ")" +
                "WHERE product_id = " + productsTableView.getSelectionModel().getSelectedItem().getId();
        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            st.executeUpdate(sqlQuery);
        }
        catch (SQLException e){
            e.printStackTrace();
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

    public void endTransactionOnClick(ActionEvent actionEvent) {
        String sqlQuery = "UPDATE invoices SET total_sum = ? WHERE invoice_id = " +
                invoicesTableView.getSelectionModel().getSelectedItem().getId();
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)){
            pstm.setDouble(1, Double.parseDouble(totalPriceLabel.getText()));
            pstm.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        invoicesTableView.getSelectionModel().clearSelection();
    }
}
