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
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initRootLayout();
        initShow();
    }

    public void initRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        rootLayout = loader.load(getClass().getResourceAsStream("/Menu.fxml"));
        MenuController controller = loader.getController();
        controller.setMain(this);
        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.show();
    }

    public void initShow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        AnchorPane pane = loader.load(getClass().getResourceAsStream("/Show.fxml"));
        ShowController controller = loader.getController();
        controller.setMain(this);
        rootLayout.setCenter(pane);
        primaryStage.setResizable(false);
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public static void main(String[] args) {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
}
