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
import model.Supplier;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SelectSupplierController implements Initializable {

    @FXML private Button newSupplierBtn;
    @FXML private Button cancelBtn;
    @FXML private Button confirmBtn;
    @FXML private TableView<Supplier> suppliersTableView;
    @FXML private TableColumn<Supplier, String> supplierNameCol;
    @FXML private TableColumn<Supplier, String> addressCol;
    @FXML private TableColumn<Supplier, String> phoneNumberCol;
    @FXML private TableColumn<Supplier, Integer> supplierIdCol;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmBtn.disableProperty().bind(Bindings.isEmpty(suppliersTableView.getSelectionModel().getSelectedItems()));
        supplierNameCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        supplierIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        suppliersTableView.setItems(getSuppliers());
    }

    public void updateSuppliers(){
        suppliersTableView.setItems(getSuppliers());
    }

    public ObservableList<Supplier> getSuppliers(){
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * FROM suppliers";

        try (Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            Supplier supplier;

            while(rs.next()){
                supplier = new Supplier(rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("nis"),
                        rs.getString("nif"));

                list.add(supplier);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public void cancelOnClick(ActionEvent actionEvent) throws SQLException {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    public void newSupplierOnClick(ActionEvent actionEvent) throws IOException {
        Stage window = HelperMethods.openWindow("purchase_entry/add-supplier.fxml",
                "Add New Supplier");
        window.setOnHidden((e) -> {
            updateSuppliers();
        });
    }


    public void confirmOnClick(ActionEvent actionEvent) throws SQLException {
        NameHolder.supplierName = suppliersTableView.
                getSelectionModel().getSelectedItem().getSupplierName();
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }
}
