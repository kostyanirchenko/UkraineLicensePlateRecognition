package source;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.objdetect.CascadeClassifier;
import source.controllers.MenuController;
import source.controllers.ShowController;

import java.io.IOException;
import java.lang.reflect.Field;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private CascadeClassifier cascadeClassifier;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        System.out.println(System.getProperty("user.dir"));
        setCascadeClassifier(System.getProperty("user.dir") + "\\src\\main\\resources\\source\\haarcascade_russian_plate_number.xml");
        this.primaryStage.setTitle("Ukraine Automatic Licence Plate Recognition");
        initRootLayout();
        initShow();
    }

    public void initRootLayout() throws IOException{
            FXMLLoader loader = new FXMLLoader();
            rootLayout = loader.load(getClass().getResourceAsStream("/Menu.fxml"));
            MenuController menuController = loader.getController();
            menuController.setMain(this);
            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.show();
    }

    public void initShow() throws IOException{
            FXMLLoader loader = new FXMLLoader();
            AnchorPane pane = loader.load(getClass().getResourceAsStream("/Show.fxml"));
            ShowController showController = loader.getController();
            showController.setMain(this);
            rootLayout.setCenter(pane);
            primaryStage.setResizable(false);
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
        String path = System.getProperty("user.dir").replaceAll("\\\\", "/");
        System.setProperty("java.library.path", path + "/src/main/resources/source");
        Field sysPath = ClassLoader.class.getDeclaredField("sys_paths");
        sysPath.setAccessible(true);
        sysPath.set(null, null);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
}
