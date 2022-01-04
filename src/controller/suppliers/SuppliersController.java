package controller.suppliers;

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
import model.Supplier;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class SuppliersController implements Initializable {
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

    @FXML private TableView<Supplier> suppliersTableView;

    @FXML private TableColumn<Supplier, Integer> idCol;

    @FXML private TableColumn<Supplier, String> supplierNameCol;

    @FXML private TableColumn<Supplier, String> phoneNumberCol;

    @FXML private TableColumn<Supplier, String> addressCol;

    @FXML private TableColumn<Supplier, String> registryCol;

    @FXML private TableColumn<Supplier, String> nisCol;

    @FXML private TableColumn<Supplier, String> nifCol;

    @FXML private Button newSupplierBtn;

    @FXML private Button editSupplierBtn;

    @FXML private Button deleteSupplierBtn;

    @FXML private TextField searchTextField;

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


        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        supplierNameCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        registryCol.setCellValueFactory(new PropertyValueFactory<>("registry"));
        nisCol.setCellValueFactory(new PropertyValueFactory<>("nis"));
        nifCol.setCellValueFactory(new PropertyValueFactory<>("nif"));
        suppliersTableView.setItems(getSuppliers());
        editSupplierBtn.disableProperty().bind(Bindings.isEmpty(suppliersTableView
                .getSelectionModel().getSelectedItems()));
        deleteSupplierBtn.disableProperty().bind(Bindings.isEmpty(suppliersTableView
                .getSelectionModel().getSelectedItems()));

        FilteredList<Supplier> supplierFilteredList = new FilteredList<>(getSuppliers(), b -> true);
        searchTextField.textProperty().addListener((observable,oldValue,newValue) -> {
            supplierFilteredList.setPredicate(supplier -> {
                if (newValue==null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (supplier.getSupplierName().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }
                else  if (supplier.getPhoneNumber().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }
                else return false;
            });
        });
        SortedList<Supplier> supplierSortedList = new SortedList<>(supplierFilteredList);

        supplierSortedList.comparatorProperty().bind(suppliersTableView.comparatorProperty());

        suppliersTableView.setItems(supplierSortedList);
    }

    public void updateSuppliers(){
        suppliersTableView.setItems(getSuppliers());
    }

    private ObservableList<Supplier> getSuppliers(){
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        String sqlQuery = "SELECT * FROM suppliers";

        try (Connection c = DBUtils.getConnection();
             Statement st = c.createStatement()){
            ResultSet rs = st.executeQuery(sqlQuery);
            Supplier supplier;

            while(rs.next()){
                supplier = new Supplier(rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("registry"),
                        rs.getString("nis"),
                        rs.getString("nif"));
                list.add(supplier);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return list;
    }

    public void newSupplierOnClick(ActionEvent event) throws IOException {
        Stage window = HelperMethods.openWindow("suppliers/add-supplier.fxml",
                "Add New Supplier");
        window.setOnHidden((e) -> {
            updateSuppliers();
        });
    }

    public void editSupplierOnClick(ActionEvent event) throws IOException {
        NameHolder.supplierId = suppliersTableView.getSelectionModel().getSelectedItem().getId();
        Stage window = HelperMethods.openWindow("suppliers/edit-supplier.fxml",
                "Edit Supplier");
        window.setOnHidden((e) -> {
            updateSuppliers();
        });
    }

    public void deleteSupplierOnClick(ActionEvent event) {
        String sqlQuery = "DELETE FROM suppliers WHERE " +
                "supplier_id = " +
                suppliersTableView.getSelectionModel().getSelectedItem().getId() ;
        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            st.executeUpdate(sqlQuery);
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        updateSuppliers();
    }


    //  dashboard   //
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

    public void billsOnClick(ActionEvent actionEvent) throws Exception{
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
//          SLIDER BUTTONS              //

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
