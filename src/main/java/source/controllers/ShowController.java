package source.controllers;

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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import source.Main;
import source.entity.Numbers;
import source.entity.Screens;
import util.FileUtil;
import util.HibernateUtil;
import util.UsersAlert;
import util.opencv.MatUtil;
import util.opencv.Recognition;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
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
    public Button viewAllImages;
    private ObservableList<File> images = FXCollections.observableArrayList();
    private Main main;
    private File[] listOfFile;
    private CascadeClassifier cascadeClassifier;
    Mat recog;
    private File fileWithImage;

    public void setMain(Main main) {
        this.main = main;
        viewAllImages.setVisible(false);
    }

    /**
     *
     * @param actionEvent
     */
    public void downloadAction(ActionEvent actionEvent) {
        // При каждом нажатии кнопки "Выбрать папку" будем удалять все элементы из списка, а так же из массива
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
        // Пройдемся по массиву файлов и найдем картинки
            for(File i: listOfFile) {
                // Загружаем только те файлы, которые имеют в совем названии .jpg
                if(FileUtil.getFileExtension(i).equals("jpg") || FileUtil.getFileExtension(i).equals("png") || i.isDirectory()) {
                    // Пропустим все файлы, которые являются директориями
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
        if(imageListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        if(imageListView.getItems().isEmpty()) {
           return;
        }
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
        System.exit(0);
    }

    public void recognitionAction(ActionEvent actionEvent) {
        List<Mat> recogImages = new ArrayList<>();
        List<File> tmp = new ArrayList<>();
        /*if(binaryImage.getImage() != null) {
            binaryImage.setImage(null);
        }*/
        getCascade();
        Mat mat = Imgcodecs.imread(this.fileWithImage.getPath());
        System.out.println(this.fileWithImage.getAbsoluteFile());
        MatOfRect detect = new MatOfRect();
        this.cascadeClassifier.detectMultiScale(mat, detect);
        String pathWithFiles = this.fileWithImage.getParent();
        System.out.println(pathWithFiles);
        for(Rect rect : detect.toArray()) {
            Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
            recog = new Mat(mat, rect);
            recogImages.add(recog);
            tmp.add(new File(this.fileWithImage.getPath()));
        }
        System.out.println(String.format("Найдено %s номерных знака", recogImages.size()));
        List<Mat> binaryImages = Recognition.thresholdImage(recogImages);
        List<Mat> countorsImages = new ArrayList<>();
        /*for(Mat i : recogImages) {
            countorsImages.add(Recognition.doGaussianBlur(MatUtil.matToImage(i)));
        }*/
        if(recogImages.size() == 1) {
            showRecogPlate(recogImages.get(0));
            binaryImage.setImage(MatUtil.matToImage(binaryImages.get(0)));
//            countorsView.setImage(MatUtil.matToImage(countorsImages.get(0)));
            countorsView.setImage(MatUtil.matToImage(Recognition.doCanny(recogImages.get(0))));
        } else if(recogImages.size() == 0) {
            numberView.setImage(null);
            binaryImage.setImage(null);
            countorsView.setImage(null);
        } else {
            if(recogImages.size() > 1) {
                if(recogImages.size() == 2) {
                    showRecogPlate(recogImages.get(1));
                    binaryImage.setImage(MatUtil.matToImage(binaryImages.get(1)));
                    countorsView.setImage(MatUtil.matToImage(Recognition.doCanny(recogImages.get(1))));
                } else {
                    showRecogPlate(recogImages.get(0));
                    binaryImage.setImage(MatUtil.matToImage(binaryImages.get(0)));
                    countorsView.setImage(MatUtil.matToImage(Recognition.doCanny(recogImages.get(0))));
                    viewAllImages.setVisible(false); // need change to true
                }
            }
        }
        saveToDatabase(recogImages, pathWithFiles, tmp);
        recogImages.clear();
        binaryImages.clear();
        countorsImages.clear();
    }

    public void viewAllImagesAction(ActionEvent actionEvent) {

    }

    private void saveToDatabase(List<Mat> images, String pathWithFiles, List<File> files) {
        File file = new File(pathWithFiles + "\\Recognition");
        if(!file.isDirectory()) {
            file.mkdir();
        }
        String path = "";
        for(Mat i: images) {
            path = file.getAbsolutePath() + "\\" + new SimpleDateFormat("dd-MM-yyy-HH-mm-ss").format(System.currentTimeMillis()) + ".png";
            Imgcodecs.imwrite(path, i);
            addScreen(path);
            List<Screens> screenses = getScreen(path);
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            try {
                Numbers numbers = new Numbers(
                        new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(System.currentTimeMillis()),
                        "" + files.get(0).lastModified() + "",
                        InetAddress.getLocalHost().getHostAddress(),
                        InetAddress.getLocalHost().getHostName(),
                        "admin",
                        screenses.get(0).getScreenId()
                );
                session.save(numbers);
                session.getTransaction().commit();
            } catch (HibernateException e) {
//                UsersAlert.throwsException(e);
                e.printStackTrace();
            } catch (UnknownHostException e) {
                UsersAlert.throwsException(e);
            } finally {
                session.close();
            }
        }
    }

    protected void addScreen(String screenName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            Screens screens = new Screens(screenName);
            session.save(screens);
            session.getTransaction().commit();
            session.close();
        } catch (HibernateException e) {
//            UsersAlert.throwsException(e);
            e.printStackTrace();
        } finally {
            if(session != null && session.isOpen()){
                session.close();
            }
            System.out.println("SESSION CLOSE");
        }
    }

    protected List<Screens> getScreen(String path) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            Query screen = session.createQuery("from Screens where screenName = :screenName").setString("screenName", path);
            List<Screens> screens = (List<Screens>) screen.list();
            session.getTransaction().commit();
            session.close();
            return screens;
        } catch (HibernateException e) {
//            UsersAlert.throwsException(e);
            e.printStackTrace();
            return null;
        } finally {
            if(session != null && session.isOpen()){
                session.close();
            }
            System.out.println("SESSION CLOSE");
        }
    }
}
