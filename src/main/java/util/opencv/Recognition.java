package util.opencv;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import source.controllers.ShowController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kostya Nirchenko.
 * @since 25.05.2016
 */
public class Recognition {
    private static ShowController showController;
    private static List<Mat> bWImages = new ArrayList<>();
    private static List<Mat> grayImages = new ArrayList<>();
    private static Mat gaussianBlur;

    public static List<Mat> thresholdImage(List<Mat> images) {
        if(!bWImages.isEmpty()) {
            bWImages.clear();
        }
        Mat grayImage = new Mat();
        Mat bWImage = new Mat();
        for(Mat recog : images) {
            Imgproc.cvtColor(recog, grayImage, Imgproc.COLOR_BGR2GRAY);
            Imgproc.threshold(grayImage, bWImage, 127, 255, Imgproc.THRESH_BINARY);
            grayImages.add(grayImage);
            bWImages.add(bWImage);
        }
//        rotateImage(bWImages.get(0));
//        Imgproc.GaussianBlur(grayImage, gaussianBlur, new Size(3, 3), 0, 1, 1);
//        Mat workingImage = new Mat();
//        Mat workingImage = doBackgroundRemove(image);
//        Imgproc.erode(bWImage, workingImage, new Mat(), new Point(-1, -1), 1);
//        Imgproc.dilate(workingImage, workingImage, new Mat(), new Point(-1, -1), 1);
//        Imgcodecs.imwrite("D:\\test.jpg", workingImage);

//        Mat smooth = new Mat();
//        Imgproc.GaussianBlur(image, smooth, Imgproc.CV_GAUSSIAN, 11);
//        findContours(grayImages);
        return bWImages;
    }

    public static Mat sobel(Image image) {
        Mat sobelMat = MatUtil.imageToMat(image);
        Mat out = new Mat();
        Imgproc.Sobel(sobelMat, out, sobelMat.depth(), 2, 2);
        return out;
    }

    private static void rotateImage(Mat mat) {

    }

    public static Mat doGaussianBlur(Image image) {
        gaussianBlur = MatUtil.imageToMat(image);
        Imgproc.cvtColor(gaussianBlur, gaussianBlur, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gaussianBlur, gaussianBlur, new Size(3, 3), 0, 1, 1);

        return gaussianBlur;
        /*List<Line> result = new ArrayList<>();
        Mat lines = new Mat();
        Imgproc.HoughLinesP(gaussianBlur, lines, 1, Math.PI / 180, 20, 35, 5);
        for(int x = 0; x < lines.cols(); x++) {
            double[] vecHoughLines = lines.get(0, x);
            if (vecHoughLines.length == 0)
                break;
            double x1 = vecHoughLines[0];
            double y1 = vecHoughLines[1];
            double x2 = vecHoughLines[2];
            double y2 = vecHoughLines[3];
            Point p1 = new Point();
            Point p2 = new Point();
            p1.x = x1;
            p1.y = y1;
            p2.x = x2;
            p2.y = y2;
            Imgproc.line(gaussianBlur, p1, p2, new Scalar(255, 0, 0, 255), 2);
        }*/
//        Imgproc.Sobel(gaussianBlur, gaussianBlur, 1, 0, 1);

    }

    /*private static Mat rotateImage(Mat source, double angle) {
        MatOfPoint2f center = new MatOfPoint2f(source.cols()/2.0f, source.rows()/2.0f);
    }*/

    private static void setShowController() {

    }

    private static Mat doBackgroundRemove(Mat frame) {
        Mat hsvImg = new Mat();
        List<Mat> hsvPlanes = new ArrayList<>();
        Mat thresholdImg = new Mat();
        int thresh_type = Imgproc.THRESH_BINARY;
        // threshold the image with the average hue value
        hsvImg.create(frame.size(), CvType.CV_8U);
        Imgproc.cvtColor(frame, hsvImg, Imgproc.COLOR_BGR2HSV);
        Core.split(hsvImg, hsvPlanes);

        // get the average hue value of the image
        double threshValue = getHistAverage(hsvImg, hsvPlanes.get(0));
        Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 179.0, thresh_type);
        Imgproc.blur(thresholdImg, thresholdImg, new Size(5, 5));
        // dilate to fill gaps, erode to smooth edges
        Imgproc.dilate(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 1);
        Imgproc.erode(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 3);
        Imgproc.threshold(thresholdImg, thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY);
        // create the new image
        Mat foreground = new Mat(frame.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        frame.copyTo(foreground, thresholdImg);
        return foreground;
    }

    private static double getHistAverage(Mat hsvImg, Mat hueValues)
    {
        // init
        double average = 0.0;
        Mat hist_hue = new Mat();
        // 0-180: range of Hue values
        MatOfInt histSize = new MatOfInt(180);
        List<Mat> hue = new ArrayList<>();
        hue.add(hueValues);

        // compute the histogram
        Imgproc.calcHist(hue, new MatOfInt(0), new Mat(), hist_hue, histSize, new MatOfFloat(0, 179));

        // get the average Hue value of the image
        // (sum(bin(h)*h))/(image-height*image-width)
        // -----------------
        // equivalent to get the hue of each pixel in the image, add them, and
        // divide for the image size (height and width)
        for (int h = 0; h < 180; h++)
        {
            // for each bin, get its value and multiply it for the corresponding
            // hue
            average += (hist_hue.get(h, 0)[0] * h);
        }

        // return the average hue of the image
        return average = average / hsvImg.size().height / hsvImg.size().width;
    }

    private static Image toGrayScale(Mat mat) {
        BufferedImage image = SwingFXUtils.fromFXImage(MatUtil.matToImage(mat), null);
        int width = image.getWidth();
        int height = image.getHeight();
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                Color c = new Color(image.getRGB(j, i));
                int red = (int)(c.getRed() * 0.299);
                int green = (int)(c.getGreen() * 0.587);
                int blue = (int)(c.getBlue() *0.114);
                Color newColor = new Color(red+green+blue,
                        red+green+blue,red+green+blue);
                image.setRGB(j,i,newColor.getRGB());
            }
        }
        return SwingFXUtils.toFXImage(image, null);
    }

    public static Mat doCanny(Mat frame)
    {
        // init
        Mat grayImage = new Mat();
        Mat detectedEdges = new Mat();

        // convert to grayscale
        Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

        // reduce noise with a 3x3 kernel
        Imgproc.blur(grayImage, detectedEdges, new Size(3, 3));

        // canny detector, with ratio of lower:upper threshold of 3:1
        Imgproc.Canny(detectedEdges, detectedEdges, 100, 200);

        // using Canny's output as a mask, display the result
        Mat dest = new Mat();
        frame.copyTo(dest, detectedEdges);

        return dest;
    }
}
