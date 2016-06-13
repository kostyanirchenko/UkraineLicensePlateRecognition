package source;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.objdetect.CascadeClassifier;
import source.controllers.EditUserInfoController;
import source.controllers.MenuController;
import source.controllers.ShowController;
import source.entity.Users;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Главный класс. Загружает все контроллеры, нативную библиотеку, устанавливает java.library.path
 *
 * Created by Kostya Nirchenko.
 * @sinse 19.05.2016
 */
public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private CascadeClassifier cascadeClassifier;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        setCascadeClassifier(System.getProperty("user.dir") + "\\src\\main\\resources\\source\\haarcascade_russian_plate_number.xml");
        this.primaryStage.setTitle("Ukraine Automatic Licence Plate Recognition");
        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/anpr.png")));
        initRootLayout();
        initShow();
    }

    public void initRootLayout() throws IOException {
            FXMLLoader loader = new FXMLLoader();
            rootLayout = loader.load(getClass().getResourceAsStream("/Menu.fxml"));
            MenuController menuController = loader.getController();
            menuController.setMain(this);
            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.show();
    }

    public void initShow() throws IOException {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane pane = loader.load(getClass().getResourceAsStream("/Show.fxml"));
            ShowController showController = loader.getController();
            showController.setMain(this);
            rootLayout.setCenter(pane);
            primaryStage.setResizable(false);
    }

    public boolean editUserInfo(Users users, int userId) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        AnchorPane pane = loader.load(getClass().getResourceAsStream("/EditUserInfo.fxml"));
        Stage editStage = new Stage();
        editStage.setTitle("Редактирование");
        editStage.getIcons().add(new Image(getClass().getResourceAsStream("/edit.png")));
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.initOwner(primaryStage);
        EditUserInfoController editUserInfoController = loader.getController();
        editUserInfoController.setEditStage(editStage);
        editUserInfoController.setUser(users);
        editUserInfoController.setUserId(userId);
        editStage.setScene(new Scene(pane));
        editStage.showAndWait();
        return editUserInfoController.isNextClicked();
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    private void setCascadeClassifier(String path) {
        this.cascadeClassifier = new CascadeClassifier(path);
    }

    public CascadeClassifier getCascadeClassifier() {
        return this.cascadeClassifier;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        String sysArch = System.getProperty("sun.arch.data.model");
        String path = System.getProperty("user.dir").replaceAll("\\\\", "/");
        if(sysArch.equals("64")) {
            System.setProperty("java.library.path", path + "/src/main/resources/source");
        } else {
            System.setProperty("java.library.path", path + "/src/main/resources/source/x86");
        }
        Field sysPath = ClassLoader.class.getDeclaredField("sys_paths");
        sysPath.setAccessible(true);
        sysPath.set(null, null);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
}
