package source;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import source.controllers.MenuController;
import source.controllers.ShowController;

import java.io.IOException;

/**
 * Created by NKostya on 23.05.2016.
 */
public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        initRootLayout();
        initShow();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(Main.class.getResource("resources/fxml/Menu.fxml"));
            System.out.println(getClass().getResource("/Menu.fxml"));
//            rootLayout = (BorderPane) loader.load(Main.class.getResource("/resources/Menu.fxml"));
            rootLayout = (BorderPane) loader.load(getClass().getResource("/Menu.fxml"));
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            MenuController menuController = loader.getController();
            menuController.setMain(this);
            primaryStage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void initShow() {
        try {
            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResourceAsStream("resources/fxml/Show.fxml"));
            AnchorPane pane = (AnchorPane) loader.load(getClass().getResource("/Show.fxml"));
            rootLayout.setCenter(pane);
            primaryStage.setResizable(false);
            ShowController showController = loader.getController();
            showController.setMain(this);
        } catch (IOException e) { }
    }
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public static void main(String[] args) {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
}
