package controller.customers;

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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Customer;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    @FXML private AnchorPane x;
    @FXML private TextField searchTextField;
    @FXML private TableView<Customer> customersTableView;
    @FXML private TableColumn<Customer, Integer> idCol;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private TableColumn<Customer, String> phoneNumberCol;
    @FXML private TableColumn<Customer, String> emailCol;
    @FXML private TableColumn<Customer, Double> balanceCol;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button dashboardBtn;
    @FXML private Button sellingBtn;
    @FXML private Button stockBtn;
    @FXML private Button suppliersBtn;
    @FXML private Button customersBtn;
    @FXML private Button purchaseEntryBtn;
    @FXML private AnchorPane slider;
    @FXML private Circle imageCircle;
    @FXML private ImageView openSliderImage;
    @FXML private Pane openSliderPane;
    @FXML private Pane closeSliderPane;
    @FXML private Button homeBtn;
    @FXML private Button creditsBtn;
    @FXML private Button logOutBtn;
    @FXML private Button exitBtn;

    @Override
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

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        editBtn.disableProperty().bind(Bindings.isEmpty(customersTableView
                .getSelectionModel().getSelectedItems()));
        deleteBtn.disableProperty().bind(Bindings.isEmpty(customersTableView
                .getSelectionModel().getSelectedItems()));

        updateCustomers();

        FilteredList<Customer> customersFilteredList = new FilteredList<>(getCustomers(), b -> true);
        searchTextField.textProperty().addListener((observable,oldValue,newValue) -> {
            customersFilteredList.setPredicate(customer -> {
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else  if (customer.getEmail().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else  if (customer.getPhoneNumber().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else return false;
            });
        });
        SortedList<Customer> customersSortedList = new SortedList<>(customersFilteredList);
        customersSortedList.comparatorProperty().bind(customersTableView.comparatorProperty());
        customersTableView.setItems(customersSortedList);
    }

    private ObservableList<Customer> getCustomers() {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * FROM customers WHERE NOT (customer_id = 0)";

        try(Connection c = DBUtils.getConnection()) {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            Customer customer;

            while(rs.next()){
                customer = new Customer(rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("customer_phone_number"),
                        rs.getString("customer_email"),
                        rs.getDouble("customer_balance"));

                list.add(customer);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return list;
    }

    private void updateCustomers(){
        customersTableView.setItems(getCustomers());
    }


    public void dashboardOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/home/drakkath/IdeaProjects/MarketWizard/src/view/dashboard/dashboard.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void sellingEntryOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/selling_entry/selling-entry.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void stockOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/stock/stock.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void customersOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/customers/customers.fxml"));
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

    public void purchaseEntryOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/purchase_entry/purchase-entry.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void addOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("customers/add-customer.fxml",
                "Add New Customer");
        window.setOnHidden((e) -> {
            updateCustomers();
        });
    }

    public void deleteOnClick(ActionEvent actionEvent) {
        String sqlQuery = "DELETE FROM customers WHERE customer_id = "
                + customersTableView.getSelectionModel().getSelectedItem().getId();
        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            st.executeUpdate(sqlQuery);
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        updateCustomers();
    }

    public void editOnClick(ActionEvent actionEvent) throws IOException {
        NameHolder.customerId = customersTableView.getSelectionModel().getSelectedItem().getId();
        Stage window = HelperMethods.openWindow("customers/edit-customer.fxml",
                "Edit Customer");
        window.setOnHidden((e) -> {
            customersTableView.getSelectionModel().clearSelection();
            updateCustomers();
        });
    }

    public void homeOnClick(ActionEvent actionEvent) {
    }

    public void creditsOnClick(ActionEvent actionEvent) {
    }

    public void logOutOnClick(ActionEvent actionEvent) {
    }

    public void exitOnClick(ActionEvent actionEvent) {
    }
}
