package com.project.utils;

import com.project.SearchFaceMatchingImageCollection;
import com.project.constants.Constants;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewFaceCapture extends JFrame {
    static
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private VideoCapture capture;
    private final JLabel videoLabel;
    private CascadeClassifier fd = new CascadeClassifier(Constants.casscade);
    private String baseDirectory = Constants.baseDir;
    private String currentPersonDIR;
    private String personName;
    private BufferedImage image;
    private Mat faceROI;
    public String absolutePath;
    private boolean isSaving = false;

    private int photosToTake = 1;
    private int photoCount = 0;
    private static final int RESIZED_WIDTH = 224;
    private static final int RESIZED_HEIGHT = 224;

    // list of already seen faces
    private List<String> seenFaces = new ArrayList<String>();

    private ExecutorService savingThreadPool = Executors.newFixedThreadPool(5);

    public NewFaceCapture() {
        JFrame videoFrame = new JFrame("Face Capture");
        videoLabel = new JLabel();
        capture = new VideoCapture(0, Videoio.CAP_DSHOW);

        if (!capture.isOpened()) {
            System.out.println("Error occurred during opening webcam!");
            return;

        }

        setVisible(true);
        setSize(640, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(null);

        videoLabel.setBounds(0, 0, 640, 400);
        add(videoLabel);

        startCapture();
    }

    private void startCapture() {
        Thread captureThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Mat frame = new Mat();
                    if (capture.read(frame)) {
                        detectAndRecognize(frame);
                    } else {
                        System.out.println("Oh noo !!!!");
                        break;
                    }
                }
            }
        });
        captureThread.start();
    }

    private void detectAndRecognize(Mat frame) {
        MatOfRect faceDetections = new MatOfRect();
        fd.detectMultiScale(frame, faceDetections, 1.1, 4, 1, new Size(40, 40), new Size());

        Mat displayFrame = frame.clone();
        for (Rect rect : faceDetections.toArray()) {
            faceROI = frame.submat(rect);

            Imgproc.rectangle(displayFrame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);

            ImageIcon image = new ImageIcon(Mat2BI(displayFrame));
            videoLabel.setIcon(image);

            Thread saveImageThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        saveFaceImage(faceROI);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            saveImageThread.start();

        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }

    private void saveFaceImage(Mat faceROI) throws Exception {
        if (!isSaving)
        {
            isSaving = true;
            savingThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        File imageFile = null;
                        if (faceROI != null) {

                            File baseDir = new File(baseDirectory);
                            if (!baseDir.exists()) {
                                baseDir.mkdirs();
                            }

                            File currentDirectory = new File(baseDirectory);
                            if (!currentDirectory.exists()) {
                                currentDirectory.mkdirs();
                            }

                            Mat resizedFace = new Mat();
                            Imgproc.resize(faceROI, resizedFace, new Size(RESIZED_WIDTH, RESIZED_HEIGHT));

                            String fileName = "test.png";
                            imageFile = new File(currentDirectory, fileName);
                            photoCount++;

                            try {
                                saveImageToDirectory(imageFile, resizedFace);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            absolutePath = currentDirectory.getAbsolutePath();
                            String faceId = SearchFaceMatchingImageCollection.search(imageFile.getAbsolutePath());

                            // if face is not in seen print the faceId
                            if (!seenFaces.contains(faceId)) {
                                System.out.println("The faceId is " + faceId);
                                seenFaces.add(faceId);
                            }

                            isSaving = false;

                        }
                        imageFile.delete();
                    }

                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        else {
            // Wait for a short duration if saving is already happening
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToDirectory(File imageFile, Mat imageMat) throws IOException {
        BufferedImage bufferedImage = Mat2BI(imageMat);
        if (bufferedImage == null) {
            throw new IOException("Error converting Mat to BufferedImage.");
        }
        ImageIO.write(bufferedImage, "png", imageFile);
    }

    private BufferedImage Mat2BI(Mat matrix) {
        int cols = matrix.cols();
        int rows = matrix.rows();
        int elemSize = (int) matrix.elemSize();
        byte[] data = new byte[cols * rows * elemSize];
        int type;
        matrix.get(0, 0, data);

        switch (matrix.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;

            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;

                byte b;
                for (int i = 0; i < data.length; i += 3) {
                    b = data[i];
                    data[i] = data[i + 2];
                    data[i + 2] = b;
                }
                break;

            default:
                return null;
        }
        image = new BufferedImage(cols, rows, type);
        image.getRaster().setDataElements(0, 0, cols, rows, data);
        return image;
    }

    public static void main(String[] args) {
        new NewFaceCapture();
    }

}
