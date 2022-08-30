package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.security.AlgorithmConstraints;
import java.sql.*;

public class CreateNewAccountController {

    public TextField txtUsername;
    public TextField txtEmail;
    public PasswordField txtNewPassword;
    public PasswordField txtConfirmPassword;
    public Button btnRegister;
    public Button btnAddNewUser;
    public Label lblUserID;
    public Label lblNewPassword;
    public Label lblConfirmPassword;
    public AnchorPane root;

    public void initialize(){
        txtUsername.setDisable(true);
        txtEmail.setDisable(true);
        txtNewPassword.setDisable(true);
        txtConfirmPassword.setDisable(true);
        btnRegister.setDisable(true);

        lblNewPassword.setVisible(false);
        lblConfirmPassword.setVisible(false);

    }



    public void btnAddNewUserOnAction(ActionEvent actionEvent) throws SQLException {
        txtUsername.setDisable(false);
        txtEmail.setDisable(false);
        txtNewPassword.setDisable(false);
        txtConfirmPassword.setDisable(false);
        btnRegister.setDisable(false);

        txtUsername.requestFocus();

        autoGenerateID();

    }


    public void autoGenerateID() throws SQLException {


            Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from user order by id desc limit 1");

            boolean isExit = resultSet.next();

            if(isExit){

                String oldID = resultSet.getString(1);

                int length=oldID.length();

                String id = oldID.substring(1, length);

                int intID = Integer.parseInt(id);

                intID =intID+1;

                if(intID <10){

                    lblUserID.setText("U00"+ intID);
                }else if(intID < 100){

                    lblUserID.setText("U0"+ intID);
                }else{
                    lblUserID.setText("U"+ intID);
                }



            }else{
                lblUserID.setText("U001");

            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public void btnRegisterOnAction(ActionEvent actionEvent) throws SQLException {

        String newpassword = txtNewPassword.getText();
        String confirmpassword = txtConfirmPassword.getText();

        boolean isEqual = newpassword.equals(confirmpassword);

        if(isEqual){

            txtNewPassword.setStyle("-fx-border-color: transparent");
            txtConfirmPassword.setStyle("-fx-border-color: transparent");

            lblNewPassword.setVisible(false);
            lblConfirmPassword.setVisible(false);

            register();

        }else{
            txtNewPassword.setStyle("-fx-border-color: red");
            txtConfirmPassword.setStyle("-fx-border-color: red");

            lblNewPassword.setVisible(true);
            lblConfirmPassword.setVisible(true);

            txtNewPassword.requestFocus();

        }

    } public void register() {

        String id = lblUserID.getText();
        String userName = txtUsername.getText();
        String email = txtEmail.getText();
        String password = txtConfirmPassword.getText();

        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("insert into user values(?,?,?,?)");
            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,userName);
            preparedStatement.setObject(3,password);
            preparedStatement.setObject(4,email);

            int i = preparedStatement.executeUpdate();

            if(i != 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Registration Success").showAndWait();
                Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
                Scene scene = new Scene(parent);

                Stage primaryStage = (Stage) this.root.getScene().getWindow();

                primaryStage.setScene(scene);
                primaryStage.setTitle("Login Form");
                primaryStage.centerOnScreen();


            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    }

