package controller.stock;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class StockController implements Initializable {
    //    slide menu items
    @FXML public Circle imageCircle ;

    @FXML public Pane openSliderPane;
    @FXML public Pane closeSliderPane;
    @FXML public ImageView openSliderImage;
    @FXML public AnchorPane slider;
    @FXML public AnchorPane x;
//    slide menu items

    //    dashboard   //
//    @FXML public Button dashboardBtn;
//    @FXML public Button sellingBtn;
//    @FXML public Button stockBtn;
//    @FXML public Button customersBtn;
//    @FXML public Button billsBtn;
//
    public Stage stage ;
    public Scene scene ;
    public Parent root;
//    dashboard   //
    @FXML private TableView<Product> productsTableView;
    @FXML private TableColumn<Product, Integer> productIdCol;
    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, String> categoryCol;
    @FXML private TableColumn<Product,Integer> quantityCol;
    @FXML private TableColumn<Product, Double> soldPriceCol;
    @FXML private TableColumn<Product, Double> purchasedPriceCol;
    @FXML private TableColumn<Product, Double> expirationCol;

    @FXML
    private Button newProductBtn;

    @FXML
    private Button editProductBtn;

    @FXML
    private Button deleteProductBtn;

    @FXML
    private TextField searchTextField;

    public void initialize(URL location, ResourceBundle resources) {
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

        //                      SLIDER                          //
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        soldPriceCol.setCellValueFactory(new PropertyValueFactory<>("soldPrice"));
        purchasedPriceCol.setCellValueFactory(new PropertyValueFactory<>("purchasedPrice"));
        expirationCol.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        productsTableView.setItems(getProducts());
        editProductBtn.disableProperty().bind(Bindings.isEmpty(productsTableView
                .getSelectionModel().getSelectedItems()));
        deleteProductBtn.disableProperty().bind(Bindings.isEmpty(productsTableView
                .getSelectionModel().getSelectedItems()));

        FilteredList<Product> productFilteredList = new FilteredList<>(getProducts(), b -> true);
        searchTextField.textProperty().addListener((observable,oldValue,newValue) -> {
            productFilteredList.setPredicate(product -> {
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (product.getName().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }
                else  if (product.getCategory().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }
                else return false;
            });
        });
        SortedList<Product> productSortedList = new SortedList<>(productFilteredList);

        productSortedList.comparatorProperty().bind(productsTableView.comparatorProperty());

        productsTableView.setItems(productSortedList);

    }

    private ObservableList<Product> getProducts(){
        ObservableList<Product> list = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * FROM products";

        try (Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            Product product;

            while(rs.next()){
                product = new Product(rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("purchased_price"),
                        rs.getDouble("sold_price"),
                        rs.getString("expiration_date"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getInt("quantity") * rs.getDouble("purchased_price"));
                list.add(product);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public void newProductOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("stock/add-new-product.fxml",
                "Add New Product");
        window.setOnHidden((e) -> {
            updateProducts();
        });
    }

    private void updateProducts() {
        productsTableView.setItems(getProducts());
    }

    public void editProductOnClick(ActionEvent event) throws IOException {
        NameHolder.productId = productsTableView.getSelectionModel().getSelectedItem().getId();
        Stage window = HelperMethods.openWindow("stock/edit-product.fxml",
                "Edit Product");
        window.setOnHidden((e) -> {
            productsTableView.getSelectionModel().clearSelection();
            updateProducts();
        });
    }

    public void deleteProductOnClick(ActionEvent event) {
        String sqlQuery = "DELETE FROM products WHERE product_id = "
                + productsTableView.getSelectionModel().getSelectedItem().getId();
        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            st.executeUpdate(sqlQuery);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        updateProducts();

    }

    //  navigation bar   //
    public void dashboardOnClick(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../../view/dashboard/dashboard.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root,1280,679);
        stage.setScene(scene);
        stage.show();
    }

    public void sellingOnClick(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../../view/selling_entry/selling-entry.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root,1280,679);
        stage.setScene(scene);
        stage.show();
    }

    public void stockOnClick(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../../view/stock/stock.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root,1280,679);
        stage.setScene(scene);
        stage.show();
    }

    public void purchaseEntryOnClick(ActionEvent actionEvent) throws Exception{
        root = FXMLLoader.load(getClass().getResource("../../view/purchase_entry/purchase-entry.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root,1280,679);
        stage.setScene(scene);
        stage.show();
    }
    public void customersOnClick(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../../view/customers/customers.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root,1280,679);
        stage.setScene(scene);
        stage.show();
    }

    public void suppliersOnClick(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../../view/suppliers/suppliers.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root,1280,679);
        stage.setScene(scene);
        stage.show();
    }


    public void logOutOnClick(ActionEvent event) throws IOException {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm logout");
        alert.setHeaderText(null);
        alert.setContentText("Continue logging out?");
        Optional<ButtonType> action =alert.showAndWait();

        if (action.get()==ButtonType.OK){
            Parent root = FXMLLoader.load(getClass().getResource("../../view/login/login.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root,1280,679);
            stage.setScene(scene);
            stage.show();
        }

    }
    public void exitOnClick(ActionEvent event) {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm exit");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to exit?");
        Optional<ButtonType> action =alert.showAndWait();

        if (action.get()==ButtonType.OK){
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void homeOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/dashboard/dashboard.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        stage.setScene(scene);
        stage.show();
    }

    public void creditsOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/credits/credits.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        stage.setScene(scene);
        stage.show();
    }


}
