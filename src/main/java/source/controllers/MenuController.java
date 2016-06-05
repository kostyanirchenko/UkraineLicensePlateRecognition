package source.controllers;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import source.Main;

import java.util.Optional;

/**
 * Created by NKostya on 19.05.2016.
 */
public class MenuController {

    public MenuItem loginInMenu;
    public Menu userInfoMenu;
    private Main main;
    private boolean loginSuccess;
    private ObservableList<MenuItem> usersItems = FXCollections.observableArrayList();

    public void setMain(Main main) {
        userInfoMenu.setDisable(true);
        this.main = main;
    }

    /**
     *
     * @param actionEvent
     */
    public void loginAction(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Вход администратора");
        dialog.setHeaderText("Пожалуйста, выполните вход администратора");
        dialog.setGraphic(new ImageView("file:" + System.getProperty("user.dir") + "\\src\\resources\\images\\login.png"));
        ButtonType loginButton = new ButtonType("Вход", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Назад", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButton, cancelButton);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField username = new TextField();
        username.setPromptText("Введите ваш логин");
        PasswordField password = new PasswordField();
        password.setPromptText("Введите ваш пароль");
        grid.add(new Label("Логин :"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Пароль :"), 0, 1);
        grid.add(password, 1, 1);
        Node login = dialog.getDialogPane().lookupButton(loginButton);
        login.setDisable(true);
        username.textProperty().addListener(((observable, oldValue, newValue) -> {
            login.setDisable(newValue.trim().isEmpty());
        }));
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> username.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButton) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(usernamePassword -> {
            //TODO need to write code which connect to database and search. Hibernate!

        });
        loginSuccess = true;
        /*if(loginSuccess) {
            userInfoMenu.setDisable(false);
            String username = "atatata";
            userInfoMenu.setText(username);
            usersItems.addAll(new MenuItem("Изменить данные"), new MenuItem("Выход"));
            userInfoMenu.getItems().addAll(usersItems);
            initUser();
        }*/
    }

    public void initUser() {

        MenuItem exit = usersItems.get(1);
        exit.setOnAction(event -> {
            System.exit(0);
        });
    }
}
