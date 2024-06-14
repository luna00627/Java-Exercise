package project;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;


public class HomePage extends JFrame {
    private JMenuBar menuBar;
    private JMenu menu, subMenu;
    private JMenuItem item1, item2, stickerWork, gifWork;
    private JPanel mainPanel;
    private ImageIcon gifIcon;
    private JLabel gifLabel;

    public HomePage() {
        initUI();
    }

    private void initUI() {
        setTitle("首頁");
        setLocation(20, 30);
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        menu = new JMenu("菜單");
        subMenu = new JMenu("我的作品集");

        item1 = new JMenuItem("貼圖生成");
        item2 = new JMenuItem("GIF生成");
        stickerWork = new JMenuItem("貼圖作品");
        gifWork = new JMenuItem("GIF作品");

        Font menuFont = new Font("Microsoft JhengHei", Font.PLAIN, 14);
        menu.setFont(menuFont);
        subMenu.setFont(menuFont);
        item1.setFont(menuFont);
        item2.setFont(menuFont);
        stickerWork.setFont(menuFont);
        gifWork.setFont(menuFont);

        /*item1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 啟動照片編輯器，在此處理貼圖生成的功能
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JFrame photoEditorFrame = new JFrame("照片編輯器");
                        photoEditorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        photoEditorFrame.setSize(800, 600);

                        // 創建 PhotoEditor 的實例
                        PhotoEditor photoEditor = new PhotoEditor();
                        photoEditorFrame.setContentPane(photoEditor.createContentPane());
                        
                        photoEditorFrame.setVisible(true);
                    }
                });
            }
        });*/

         item1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在此處理貼圖生成的功能
                JOptionPane.showMessageDialog(null, "執行貼圖生成功能");
            }
        });


        item2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    generateGIF();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "無法生成 GIF：" + ex.getMessage());
                }
            }
        });

        stickerWork.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在此處理我的作品集中的貼圖作品功能
                JOptionPane.showMessageDialog(null, "打開我的貼圖作品集");
            }
        });

        gifWork.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在此處理我的作品集中的GIF作品功能
                showGIF();
            }
        });

        menu.add(item1);
        menu.add(item2);
        menu.add(subMenu);
        subMenu.add(stickerWork);
        subMenu.add(gifWork);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        mainPanel = new JPanel(new BorderLayout());
        gifLabel = new JLabel();
        mainPanel.add(gifLabel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void generateGIF() throws IOException {
        BufferedImage image1 = ImageIO.read(new File("C:/java-project/pig1.png"));
        BufferedImage image2 = ImageIO.read(new File("C:/java-project/pig2.png"));
        BufferedImage image3 = ImageIO.read(new File("C:/java-project/pig3.png"));
        BufferedImage image4 = ImageIO.read(new File("C:/java-project/pig4.png"));
        
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(new FileOutputStream("C:/java-project-test/gif.gif"));
        encoder.setSize(300, 190);
        encoder.setDelay(400);
        encoder.setRepeat(0);
        encoder.addFrame(image1);
        encoder.addFrame(image2);
        encoder.addFrame(image3);
        encoder.addFrame(image4);
        encoder.finish();
        
        JOptionPane.showMessageDialog(null, "GIF生成成功！");
    }

    private void showGIF() {
        String gifFilePath = "C:/java-project-test/gif.gif"; // GIF 檔案的路徑
        File gifFile = new File(gifFilePath);
        
        if (gifFile.exists() && gifFile.isFile()) {
            gifIcon = new ImageIcon(gifFilePath);
            gifLabel.setIcon(gifIcon);
        } else {
            JOptionPane.showMessageDialog(null, "找不到 GIF 檔案：" + gifFilePath);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                HomePage homePage = new HomePage();
                homePage.setVisible(true);
            }
        });
    }
}
