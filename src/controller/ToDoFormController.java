package controller;



import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tm.ToDoTm;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;


public class ToDoFormController {


    public Label lblWelcomNote;
    public Label lblUserId;
    public AnchorPane root;
    public Pane setRoot;
    public TextField txtAddNew;
    public ListView<ToDoTm> lstToDoList;
    public Label lblToDoID;
    public TextField txtSelectedToDo;
    public Button btnDelete;
    public Button btnUpdate;


    public String id;
    public Label txtCheck;

    public void initialize() throws SQLException {
        lblWelcomNote.setText("Hi "+ LoginFormController.enteredUserName +" Welcome ");
        lblUserId.setText(LoginFormController.enteredId);
        setRoot.setVisible(false);
        txtCheck.setVisible(false);

        loadList();

        txtSelectedToDo.setDisable(true);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);

        lstToDoList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoTm>() {
            @Override
            public void changed(ObservableValue<? extends ToDoTm> observable, ToDoTm oldValue, ToDoTm newValue) {
                btnDelete.setDisable(false);
                btnUpdate.setDisable(false);
                txtSelectedToDo.setDisable(false);

                txtSelectedToDo.requestFocus();
                setRoot.setVisible(false);

                if(newValue == null){
                    return;
                }

                String description = newValue.getDescription();
                txtSelectedToDo.setText(description);

                id = newValue.getId();

            }

        });

    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do You Want To Log Out" , ButtonType.YES,ButtonType.NO);

        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get().equals(ButtonType.YES)){
            Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
            Scene scene=new Scene(parent);

            Stage primaryStage = (Stage) this.root.getScene().getWindow();

            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Form");
            primaryStage.centerOnScreen();

        }

    }

    public void btnAddNewOnAction(ActionEvent actionEvent){
        setRoot.setVisible(true);
        txtAddNew.requestFocus();

        txtSelectedToDo.setDisable(true);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        txtCheck.setVisible(false);

    }

    public void btnAddOnAction(ActionEvent actionEvent) throws SQLException {

        if(txtAddNew.getText().trim().isEmpty()){

            txtCheck.setVisible(true);
            txtAddNew.requestFocus();
        }else{

            String id = autoGenerateId();
            String description = txtAddNew.getText();
            String user_id = lblUserId.getText();

            Connection connection = DBConnection.getInstance().getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into todos values (?,?,?)");

                preparedStatement.setObject(1,id);
                preparedStatement.setObject(2,description);
                preparedStatement.setObject(3,user_id);

                int i = preparedStatement.executeUpdate();

                System.out.println(i);
                setRoot.setVisible(false);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            loadList();
            txtAddNew.clear();
            txtCheck.setVisible(false);
        }


    }
    

    public String autoGenerateId() throws SQLException {


        Connection connection = DBConnection.getInstance().getConnection();
        String newId= "";

        try {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from todos order by id desc limit 1");

            boolean isExit = resultSet.next();


            if(isExit){
                String oldID = resultSet.getString(1);

                int length=oldID.length();

                String id = oldID.substring(1, length);

                int intID = Integer.parseInt(id);

                intID =intID+1;

                if(intID < 10){
                  newId="T00" + intID;

                }else if(intID <100){
                    newId="T0" + intID;
                }else{
                    newId="T" + intID;
                }

            }else{

                newId="T001";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newId;
    }

    public void loadList() throws SQLException {
        ObservableList<ToDoTm>todos = lstToDoList.getItems();
        todos.clear();

        Connection connection = DBConnection.getInstance().getConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select * from todos where user_id = ? ");
            preparedStatement.setObject(1,LoginFormController.enteredId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                String id = resultSet.getString(1);
                String description = resultSet.getString(2);
                String user_id = resultSet.getString(3);

                ToDoTm toDoTm = new ToDoTm(id,description,user_id);
                todos.add(toDoTm);
            }
            lstToDoList.refresh();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException {

        String description = txtSelectedToDo.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("update todos set description = ? where  id = ?");
        preparedStatement.setObject(1,description);
        preparedStatement.setObject(2,id);

        preparedStatement.executeUpdate();
        loadList();

        txtSelectedToDo.clear();
        txtSelectedToDo.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);


    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete todo..? ", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get().equals(ButtonType.YES)){
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("delete from todos where id = ?");

            preparedStatement.setObject(1,id);
            preparedStatement.executeUpdate();

            loadList();

            txtSelectedToDo.clear();
            txtSelectedToDo.setDisable(true);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);

        }






    }
}

