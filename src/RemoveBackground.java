import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemoveBackground {

    public static void main(String[] args) throws IOException {
        // 加載本機 OpenCV 庫
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 加載圖像
        String imagePath = "input.jpg";
        Mat image = Imgcodecs.imread(imagePath);

        // 檢查圖像是否加載成功
        if (image.empty()) {
            System.out.println("加載圖像時出錯: " + imagePath);
            return;
        }

        // 將圖像轉換為灰度圖像
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // 應用高斯模糊
        Mat blur = new Mat();
        Imgproc.GaussianBlur(gray, blur, new Size(5, 5), 0);

        // Canny 邊緣檢測
        Mat edges = new Mat();
        Imgproc.Canny(blur, edges, 100, 200);

        // 查找輪廓
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(edges, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // 創建掩碼
        Mat mask = Mat.zeros(image.size(), CvType.CV_8UC1);

        // 用輪廓填充掩碼
        Imgproc.drawContours(mask, contours, -1, new Scalar(255), Core.FILLED);

        // 反轉掩碼
        Mat maskInv = new Mat();
        Core.bitwise_not(mask, maskInv);

        // 提取前景
        Mat foreground = new Mat();
        Core.bitwise_and(image, image, foreground, mask);

        // 提取背景（此處為黑色）
        Mat background = Mat.zeros(image.size(), image.type());

        // 合併前景和背景
        Mat result = new Mat();
        Core.add(foreground, background, result);

        // 保存結果
        String outputPath = "output.jpg";
        Imgcodecs.imwrite(outputPath, result);

        // 將 Mat 轉換為 BufferedImage 以顯示
        BufferedImage bufferedImage = MatToBufferedImage(result);
        ImageIO.write(bufferedImage, "jpg", new File(outputPath));

        System.out.println("背景已移除，結果保存到: " + outputPath);
    }

    private static BufferedImage MatToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.get(0, 0, targetPixels);
        return image;
    }
}
