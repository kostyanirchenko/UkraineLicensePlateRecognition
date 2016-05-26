package source.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import source.Main;

/**
 * Created by NKostya on 23.05.2016.
 */
public class MenuController {

    @FXML
    public MenuItem loginInMenu;
    @FXML
    public Menu userInfoMenu;


    public Main main;

    private boolean loginSuccess;

    @FXML //не уверен что тут тут нужна аннотация.
    private ObservableList<MenuItem> usersItems = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        //Этот метод стартует при загзрузке fxml файла к которому привязан контроллер
        userInfoMenu.setDisable(true);
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    /**
     * @param actionEvent
     */
    public void loginAction(ActionEvent actionEvent) {
        this.loginSuccess = true;
        if (loginSuccess) {
            userInfoMenu.setDisable(false);
            String username = "atatata";
            userInfoMenu.setText(username);
            usersItems.addAll(new MenuItem("�������� ������"), new MenuItem("�����"));
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
