package source.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import source.Main;

import java.io.File;

/**
 * Created by NKostya on 23.05.2016.
 */
public class ShowController {

    @FXML
    public ImageView img;

    @FXML
    public Button download;

    @FXML
    public AnchorPane stage;

    @FXML
    public ListView imageListView;

    @FXML
    public ImageView numberView;

    @FXML
    private ObservableList<File> images = FXCollections.observableArrayList();

    private File[] listOfFile;

    private Main main;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    /**
     *
     * @param actionEvent
     */
    public void downloadAction(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("�������� ����� � �������������");
        File file = directoryChooser.showDialog(main.getPrimaryStage());
        if(file != null) {
            listOfFile = file.listFiles();
        }
//        File[] listOfFile = file.listFiles();
//        List<File> list = new ArrayList<File>();
       /* if(directoryChooser.get) {

        }*/
        if(file != null) {
            for(File i: listOfFile) {
                // ��������� ������ �� �����, ������� ����� � ����� �������� .jpg
                if(i.getPath().contains(".jpg")) {
                    this.images.add(i);
                } else { //���� � �������� ����� �� ������� ����������� - ������� ���� � �������
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("������� ����� � �������������");
                    alert.setHeaderText("����������� �� �������");
                    alert.setContentText("����������, ������� �����, � ������� ��������� �����������");
                    alert.showAndWait();
                    break;
                }
            }
        }
        imageListView.setItems(this.images);
        /*for(File i : list) {
            System.out.println(i.getName());
        }*/
    }

    /**
     * ��� ������ �������� � {@link #imageListView} ���������� �������� ����������� � {@link #img}
     * @param event
     */
    public void onClickAction(Event event) {
        if(imageListView.getItems().isEmpty()) {
            return;
        }
        File fileWithImage = (File) imageListView.getSelectionModel().getSelectedItem();
        Image image = new Image("file:" + fileWithImage.getPath());
        img.setImage(image);
    }
}
