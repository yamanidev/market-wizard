package controller.login;

import app.utils.DBUtils;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

public class SignUpController implements Initializable {

    //    slide menu items
    @FXML
    public Circle imageCircle ;

    @FXML public Pane openSliderPane;
    @FXML public Pane closeSliderPane;
    @FXML public ImageView openSliderImage;
    @FXML public AnchorPane slider;
    @FXML public AnchorPane x;
//    slide menu items

    @FXML
    private TextField fullNameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField repeatPasswordTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button SignUpBtn;



    public void initialize(URL location, ResourceBundle resources) {
        slider.setTranslateX(-255);
        openSliderPane.setVisible(true);
        closeSliderPane.setVisible(false);

        openSliderImage.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.2));
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
    //                      SLIDER BUTTONS                  //
    public void logInOnClick (ActionEvent event) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("../../view/login/login.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root,1280,679);
            stage.setScene(scene);
            stage.show();
    }


    public void signUpOnClick(ActionEvent event) throws IOException {
        if (createUser().equals("Success")){
            Parent root = FXMLLoader.load(getClass().getResource("../../view/login/login.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root,1280,679);
            stage.setScene(scene);
            stage.show();
        }
    }

    public String createUser() {
        String status = "Success";
        try (Connection c = DBUtils.getConnection();) {

            String fullName = fullNameTextField.getText().trim();
            String username = usernameTextField.getText().trim();
            String password = passwordTextField.getText();

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                errorLabel.setText("Please complete all the fills");
                errorLabel.setTextFill(RED);
                status = "fail";
            } else {
                if (password.length() < 6) {
                    errorLabel.setText("Password is too weak, please choose atleast 6 characters");
                    errorLabel.setTextFill(RED);
                    status = "fail";
                } else {
                    if (!passwordTextField.getText().equals(repeatPasswordTextField.getText())) {
                        errorLabel.setText("Passwords do not match, please retype.");
                        errorLabel.setTextFill(RED);
                        status = "fail";
                    } else {

                        String sql = "select * from users where username = ?";
                        PreparedStatement preparedStatement = c.prepareStatement(sql);
                        preparedStatement.setString(1, username);
                        ResultSet rs = preparedStatement.executeQuery();
                        if (rs.next()) {
                            errorLabel.setText("Username already taken, please try another username");
                            errorLabel.setTextFill(RED);
                            status = "fail";
                        } else {
                            String sql2 = "insert into users (full_name, username, password) values(?,?,?)";
                            preparedStatement = c.prepareStatement(sql2);

                            preparedStatement.setString(1, fullName);
                            preparedStatement.setString(2, username);
                            preparedStatement.setString(3, password);

                            preparedStatement.execute();

                            errorLabel.setText("Account successfully registered");
                            errorLabel.setTextFill(GREEN);
                        }
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
      return status;
    }
}
