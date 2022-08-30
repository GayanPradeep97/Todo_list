package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public TextField txtUserNameLogin;
    public PasswordField txtPasswordLoign;
    public Button btnLoginForm;
    public AnchorPane root;

    public static String enteredUserName;
    public static String enteredId;

    public void btnLoginLoginFormOnAction(ActionEvent actionEvent) {

        String userName = txtUserNameLogin.getText();
        String password = txtPasswordLoign.getText();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from user where user_name  = ? and password =?");

            preparedStatement.setObject(1,userName);
            preparedStatement.setObject(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();

            boolean isExit = resultSet.next();

            if(isExit){

                enteredId = resultSet.getString(1);
                enteredUserName = resultSet.getString(2);


                Parent parent = FXMLLoader.load(this.getClass().getResource("../view/ToDoForm.fxml"));
                Scene scene =new Scene(parent);

                Stage primarystage = (Stage) this.root.getScene().getWindow();



                primarystage.setScene(scene);
                primarystage.setTitle("ToDo Form");
                primarystage.centerOnScreen();

            }else{
                new Alert(Alert.AlertType.ERROR,"Invalid user name or Password").showAndWait();
            }


        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }


    }

    public void lblCreateNewAccountOnMouseClicked(MouseEvent mouseEvent) throws IOException {

        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/CreateNewAccountForm.fxml"));
        Scene scene =new Scene(parent);

        Stage primarystage = (Stage) this.root.getScene().getWindow();

        primarystage.setScene(scene);
        primarystage.setTitle("Create New Account");
        primarystage.centerOnScreen();

    }
}
