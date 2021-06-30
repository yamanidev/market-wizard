package controller.selling_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import app.utils.NameHolder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import model.Supplier;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SelectCustomerController implements Initializable {
    @FXML private TableView<Customer> customersTableView;
    @FXML private TableColumn<Customer, Integer> idCol;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private TableColumn<Customer, String> phoneNumberCol;
    @FXML private TableColumn<Customer, String> emailCol;
    @FXML private TextField searchTextField;
    @FXML private Button cancelBtn;
    @FXML private Button confirmBtn;
    @FXML private Button newCustomerBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        updateCustomers();
    }

    private void updateCustomers(){
        customersTableView.setItems(getCustomers());
    }

    private ObservableList<Customer> getCustomers(){
        ObservableList<Customer> list = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * FROM customers WHERE NOT (customer_id = 0)";

        try (Connection c = DBUtils.getConnection();
             Statement st = c.createStatement()){
            ResultSet rs = st.executeQuery(sqlQuery);
            Customer customer;

            while(rs.next()){
                customer = new Customer(rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("customer_phone_number"),
                        rs.getString("customer_email"));

                list.add(customer);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return list;
    }

    public void newCustomerOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("selling_entry/add-customer.fxml",
                "Add New Customer");
        window.setOnHidden((e) -> {
            updateCustomers();
        });
    }

    public void cancelOnClick(ActionEvent actionEvent) {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    public void confirmOnClick(ActionEvent actionEvent) {
        NameHolder.customerId = customersTableView.getSelectionModel().getSelectedItem().getId();
        NameHolder.customerName = customersTableView.getSelectionModel().getSelectedItem().getName();
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }
}
