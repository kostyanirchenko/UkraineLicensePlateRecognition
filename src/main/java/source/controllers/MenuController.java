package source.controllers;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import source.Main;
import source.entity.Users;
import util.HibernateUtil;
import util.UsersAlert;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kostya Nirchenko.
 * @since 19.05.2016
 */
public class MenuController {

    public MenuItem loginInMenu;
    public Menu userInfoMenu;
    private Main main;
    private boolean loginSuccess;
    private ObservableList<MenuItem> usersItems = FXCollections.observableArrayList();
    private Session session = null;
    private List<Users> adminUser;

    public void setMain(Main main) {
        userInfoMenu.setVisible(false);
        this.main = main;
    }

    /**
     * Выполняет вход администратора
     * @param actionEvent
     */
    public void loginAction(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Вход администратора");
        dialog.setHeaderText("Пожалуйста, выполните вход администратора");
        dialog.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/login.png"))));
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
            try {
                openSession();
                this.session.beginTransaction();
                Query query = this.session.createQuery("from Users where username = :login and userPassword = :password").setString("login", usernamePassword.getKey()).setString("password", usernamePassword.getValue());
                adminUser = (List<Users>) query.list();
                this.session.getTransaction().commit();
                if(!adminUser.isEmpty()) {
                    if(adminUser.get(0).getUserGroup().equals("admin")) {
                        this.loginSuccess = true;
                        userInfoMenu.setVisible(true);
                        userInfoMenu.setText(usernamePassword.getKey());
                        usersItems.addAll(new MenuItem("Изменить данные"), new MenuItem("Выход"));
                        userInfoMenu.getItems().addAll(usersItems);
                        initUser();
                    } else {
                        Alert logErr = UsersAlert.showSimpleDialog(Alert.AlertType.ERROR, "Ошибка", null, "Пользователь не находится в группе \"Администраторы \"", true);
                        Stage logErrStage = (Stage) logErr.getDialogPane().getScene().getWindow();
                        logErrStage.getIcons().add(new Image(Main.class.getResourceAsStream("/error.png")));
                        logErrStage.showAndWait();
                    }
                } else {
                    Alert passErr = UsersAlert.showSimpleDialog(Alert.AlertType.ERROR, "Ошибка", null, "Неверное имя пользователя или пароль, попробуйте еще раз", true);
                    Stage passErrStage = (Stage) passErr.getDialogPane().getScene().getWindow();
                    passErrStage.getIcons().add(new Image(Main.class.getResourceAsStream("/error.png")));
                    passErrStage.showAndWait();
                }
            } catch (HibernateException e) {
                UsersAlert.throwsException(e);
            } finally {
                closeSession();
            }
        });
    }

    public void initUser() {

        MenuItem exit = usersItems.get(1);
        exit.setOnAction(event -> {
            userInfoMenu.getItems().clear();
            usersItems.clear();
            userInfoMenu.setVisible(false);
        });
        MenuItem edit = usersItems.get(0);
        edit.setOnAction(event -> {
            for(Users i : adminUser) {
                int userId = i.getUserId();
                String username = i.getUsername();
                String userPassword = i.getUserPassword();
                String created_at = i.getCreated_at();
                String userGroup = i.getUserGroup();
                Users admin = new Users(
                        username,
                        userPassword,
                        created_at,
                        userGroup
                );
                try {
                    boolean nextClicked = main.editUserInfo(admin, userId);
                    if(nextClicked) {
                        Alert confirm = UsersAlert.showSimpleDialog(Alert.AlertType.INFORMATION, "Изменение данных", "Операция прошла успешно",
                                "Данные успешно изменены!", true);
                        Stage confirmStage = (Stage) confirm.getDialogPane().getScene().getWindow();
                        confirmStage.getIcons().add(new Image(Main.class.getResourceAsStream("/confirm.png")));
                        confirmStage.showAndWait();
                    }
                } catch (IOException e) {
                    UsersAlert.throwsException(e);
                }
            }
        });
    }

    private void openSession() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    private void closeSession() {
        if(this.session != null && this.session.isOpen()){
            this.session.close();
        }
    }
}
