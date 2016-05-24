package source.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import source.Main;

/**
 * Created by NKostya on 23.05.2016.
 */
public class MenuController {
    public MenuItem loginInMenu;
    public Menu userInfoMenu;
    public Main main;
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
        this.loginSuccess = true;
        if(loginSuccess) {
            userInfoMenu.setDisable(false);
            String username = "atatata";
            userInfoMenu.setText(username);
            usersItems.addAll(new MenuItem("Изменить данные"), new MenuItem("Выход"));
            userInfoMenu.getItems().addAll(usersItems);
            initUser();
        }
    }

    public void initUser() {

        MenuItem exit = usersItems.get(1);
        exit.setOnAction(event -> {
            System.exit(0);
        });
    }
}
