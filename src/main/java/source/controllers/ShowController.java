package source.controllers;

import source.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import util.FileUtil;
import util.opencv.MatUtil;
import util.opencv.Recognition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kostya Nirchenko.
 * @since 19.05.2016
 */
public class ShowController {

    public ImageView img;
    public Button download;
    public AnchorPane stage;
    public ListView imageListView;
    public ImageView numberView;
    public Button exitButton;
    public Button recognitionButton;
    public ImageView binaryImage;
    public ImageView countorsView;
    private ObservableList<File> images = FXCollections.observableArrayList();
    private Main main;
    private File[] listOfFile;
    private CascadeClassifier cascadeClassifier;
    Mat recog;
    private File fileWithImage;

    public void setMain(Main main) {
        this.main = main;
    }

    /**
     *
     * @param actionEvent
     */
    public void downloadAction(ActionEvent actionEvent) {
        if(!this.images.isEmpty() && !imageListView.getItems().isEmpty()) {
            this.images.removeAll();
            imageListView.getItems().clear();
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите папку с изображениями");
        File file = directoryChooser.showDialog(main.getPrimaryStage());
        if(file != null) {
            listOfFile = file.listFiles();
        }
            for(File i: listOfFile) {
                // Загружаем только те файлы, которые имеют в совем названии .jpg
                if(FileUtil.getFileExtension(i).equals("jpg") | FileUtil.getFileExtension(i).equals("png") | i.isDirectory()) {
                    if(!i.isDirectory()) {
                        this.images.add(i);
                    }
                } else { //Если в указаной папке не найдены изображение - выведем окно с ошибкой
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Укажите папку с изображениями");
                    alert.setHeaderText("Изображения не найдены");
                    alert.setContentText("Пожалуйста, укажите папку, в которой находятся изображения");
                    alert.showAndWait();
                    break;
                }
            }
        imageListView.setItems(this.images);
    }

    /**
     * При выборе элемента в {@link #imageListView} производит загрузку изображения в {@link #img}
     * @param event
     */
    public void onClickAction(Event event) {
        if(imageListView.getItems().isEmpty()) {
           return;
        }
//        getCascade();
        this.fileWithImage = (File) imageListView.getSelectionModel().getSelectedItem();
        Image image = new Image("file:" + fileWithImage.getPath());
        img.setImage(image);
    }

    private void showRecogPlate(Mat mat) {
        Image img = MatUtil.matToImage(mat);
        numberView.setImage(img);
    }

    private void getCascade() {
        this.cascadeClassifier = this.main.getCascadeClassifier();
    }

    public void exitAction(ActionEvent actionEvent) {
    }

    public void recognitionAction(ActionEvent actionEvent) {
        if(binaryImage.getImage() != null) {
            binaryImage.setImage(null);
        }
        getCascade();
        Mat mat = Imgcodecs.imread(this.fileWithImage.getPath());
        MatOfRect faceDetection = new MatOfRect();
        this.cascadeClassifier.detectMultiScale(mat, faceDetection);
        System.out.println(String.format("Detected %s faces", faceDetection.toArray().length));
//        int i = 0;
        List<Mat> recogImages = new ArrayList<>();
        for(Rect rect : faceDetection.toArray()) {
//            ++i;
            Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
            recog = new Mat(mat, rect);
            recogImages.add(recog);
//            Imgcodecs.imwrite(String.format("D:\\recog%s.jpg", i), Recognition.thresholdImage(recog));
            // Нужно сохранять все обнаруженные области интереса
        }
        System.out.println(String.format("Найдено %s номерных знака", recogImages.size()));
        if(recogImages.size() >= 1) {
            List<Mat> binaryImages = Recognition.thresholdImage(recogImages);
            showRecogPlate(recog);
//            countorsView.setImage(MatUtil.matToImage(Recognition.doCanny(recog)));
//            countorsView.setImage(MatUtil.matToImage(Recognition.doGaussianBlur(MatUtil.matToImage(recog))));
            countorsView.setImage(MatUtil.matToImage(Recognition.sobel(MatUtil.matToImage(recog))));
            if(binaryImages.size() > 1) {
                //TODO
                binaryImage.setImage(MatUtil.matToImage(binaryImages.get(0)));
            } else {
                binaryImage.setImage(MatUtil.matToImage(binaryImages.get(0)));
            }
        } else {
            numberView.setImage(null);
            binaryImage.setImage(null);
            countorsView.setImage(null);

        }
//        String fileName = "D:\\detect.png";
//        String recogCar = "D:\\car.png";
//        System.out.println(String.format("Writing %s", fileName));
//        Imgcodecs.imwrite(recogCar, mat);
//        Imgcodecs.imwrite(fileName, recog);
//        MatUtil.matToImage(recog);
        /*if(!recog.empty() || faceDetection.toArray().length != 0) {
            showRecogPlate(recog);
        } else {
            numberView.setImage(null);
        }*/
//        showRecogPlate(Recognition.thresholdImage(recog));
//        Imgcodecs.imwrite("D:\\recog.png", Recognition.thresholdImage(recog));
    }
}
