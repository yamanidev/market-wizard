package controller.dashboard;

import app.utils.DBUtils;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML private AnchorPane x;
    @FXML private Label expiredProductsLabel;
    @FXML private Label stockOutLabel;
    @FXML private Label nearStockOutLabel;
    @FXML private Label customersCountLabel;
    @FXML private Label suppliersCountLabel;
    @FXML private Label productsCountLabel;
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

        updateStats();

    }

    private void updateStats(){
        stockOutLabel.setText(String.valueOf(getStockOut()));
        nearStockOutLabel.setText(String.valueOf(getNearStockOut()));
        expiredProductsLabel.setText(String.valueOf(getExpired()));
        productsCountLabel.setText(String.valueOf(getProduts()));
        customersCountLabel.setText(String.valueOf(getCustomers()));
        suppliersCountLabel.setText(String.valueOf(getSuppliers()));
    }

    private int getStockOut(){
        int stockout = 0;
        String sqlQuery = "SELECT COUNT(*) FROM products WHERE quantity = 0";
        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            stockout = rs.getInt("COUNT(*)");
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return stockout;
    }

    private int getNearStockOut(){
        String sqlQuery = "SELECT COUNT(*) FROM products WHERE quantity < 20 AND quantity >= 1";
        int nearStockOut = 0;
        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            nearStockOut = rs.getInt("COUNT(*)");
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return nearStockOut;
    }

    private int getExpired(){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = LocalDate.now().format(df);
        int expired = 0;
        String sqlQuery = "SELECT COUNT(*) FROM products WHERE expiration_date < " + currentDate;

        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            expired = rs.getInt("COUNT(*)");
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return expired;
    }

    private int getCustomers(){
        int customers = 0;
        String sqlQuery = "SELECT COUNT(*) FROM customers";

        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            customers = rs.getInt("COUNT(*)");
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return customers;
    }

    private int getSuppliers(){
        int suppliers = 0;
        String sqlQuery = "SELECT COUNT(*) FROM suppliers";

        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            suppliers = rs.getInt("COUNT(*)");
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return suppliers;
    }

    private int getProduts(){
        int products = 0;
        String sqlQuery = "SELECT COUNT(*) FROM products";

        try(Connection c = DBUtils.getConnection()){
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sqlQuery);
            products = rs.getInt("COUNT(*)");
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return products;
    }

    public void dashboardOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/dashboard/dashboard.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Dashboard");
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void sellingEntryOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/selling_entry/selling-entry.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Selling Entry");
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void stockOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/stock/stock.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Stock");
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void customersOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/customers/customers.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Customers");
        Scene scene = new Scene(root,1280,679);
        window.setScene(scene);
        window.show();
    }

    public void suppliersOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../view/suppliers/suppliers.fxml"));
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setTitle("Suppliers");
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
