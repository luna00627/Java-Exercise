package project.java2024;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

public class Gif {
    public static void main(String[] args) throws Exception {
        BufferedImage image1 = ImageIO.read(new File("C:/java-project/pig1.png"));
        BufferedImage image2 = ImageIO.read(new File("C:/java-project/pig2.png"));
        BufferedImage image3 = ImageIO.read(new File("C:/java-project/pig3.png"));
        BufferedImage image4 = ImageIO.read(new File("C:/java-project/pig4.png"));
        AnimatedGifEncoder e = new AnimatedGifEncoder();
        //生成的图片路径
        e.start(new FileOutputStream("C:/java-project-test/gif.gif"));
        //图片宽高
        e.setSize(300, 190);
        //图片之间间隔时间
        e.setDelay(400);
        //重复次数 0表示无限重复 默认不重复
        e.setRepeat(0);
        //添加图片
        e.addFrame(image1);
        e.addFrame(image2);
        e.addFrame(image3);
        e.addFrame(image4);
        e.finish();
    }
}