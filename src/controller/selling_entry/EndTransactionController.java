package controller.selling_entry;

import app.utils.DBUtils;
import app.utils.NameHolder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EndTransactionController implements Initializable {
    @FXML private Label totalAmountLabel;
    @FXML private Label balanceLabel;
    @FXML private TextField amountPaidTextField;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        amountPaidTextField.requestFocus();
        totalAmountLabel.setText(String.valueOf(NameHolder.totalAmount));

        amountPaidTextField.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    balanceLabel.setText(String.valueOf(getBalance()));
                }
            }
        });
    }

    private double getBalance(){
        return NameHolder.totalAmount - Double.parseDouble(amountPaidTextField.getText());
    }

    private void addBalanceToCustomer(int customerId, double balance){
        String sqlQuery = "UPDATE customers SET customer_balance = (customer_balance + ?) " +
                "WHERE customer_id = " + customerId;
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)) {
            pstm.setDouble(1, balance);
            pstm.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void addTotalToBill(int billId, double total){
        String sqlQuery = "UPDATE bills SET total_price = ?" +
                "WHERE bill_id = " + billId;
        try(Connection c = DBUtils.getConnection();
            PreparedStatement pstm = c.prepareStatement(sqlQuery)) {
            pstm.setDouble(1, total);
            pstm.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void confirmOnClick(ActionEvent actionEvent) {
        addTotalToBill(NameHolder.billId, Double.parseDouble(totalAmountLabel.getText()));
        addBalanceToCustomer(NameHolder.customerId, Double.parseDouble(balanceLabel.getText()));
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    public void cancelOnClick(ActionEvent actionEvent) {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }


}
