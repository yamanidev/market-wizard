package controller.purchase_entry;

import app.utils.DBUtils;
import app.utils.HelperMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Supplier;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SelectSupplierController implements Initializable {
    @FXML  public Button newSupplierBtn;
    @FXML  public Button cancelBtn;
    @FXML  public Button confirmBtn;
    @FXML public TableView<Supplier> suppliersTableView;
    @FXML public TableColumn<Supplier, String> supplierCol;
    @FXML public TableColumn<Supplier, String> wilayaCol;
    @FXML public TableColumn<Supplier, String> phoneNumberCol;

    public ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("Supplier"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        wilayaCol.setCellValueFactory(new PropertyValueFactory<>("Wilaya"));
        suppliersTableView.setItems(getSuppliers());
    }

    public void updateSuppliers(){
        suppliersTableView.setItems(getSuppliers());
    }

    public ObservableList<Supplier> getSuppliers(){
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        Connection c = DBUtils.getConnectionWith("suppliers.db");
        String sqlQuery = "select * from suppliers";
        Statement st;
        ResultSet rs;
        try {
            st = c.createStatement();
            rs = st.executeQuery(sqlQuery);
            Supplier supplier;
            while(rs.next()){
                supplier = new Supplier(rs.getString("full_name"), rs.getString("phone_number"), rs.getString("wilaya"));
                list.add(supplier);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

    public void cancelOnClick(ActionEvent actionEvent) {
        Stage window = (Stage) cancelBtn.getScene().getWindow();
        window.close();
    }


    public void newSupplierOnClick(ActionEvent actionEvent) throws IOException {
        HelperMethods helper = new HelperMethods();
        Stage window = helper.openWindow2("add-supplier.fxml", "Add New Supplier");
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                System.out.println("something");
            }
        });
        window.close();
    }


}
