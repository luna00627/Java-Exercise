package project.java2024;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class HomePage extends JFrame {
    JMenuBar menuBar;
    JMenu menu, subMenu;
    JMenuItem item1, item2, stickerWork, gifWork;

    public HomePage() {
        initUI();
    }

    private void initUI() {
        setTitle("首頁");
        setLocation(20, 30);
        setSize(1000, 700);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        menuBar = new JMenuBar();
        menu = new JMenu("菜單");
        subMenu = new JMenu("我的作品集");

        item1 = new JMenuItem("貼圖生成");
        item2 = new JMenuItem("GIF生成");
        stickerWork = new JMenuItem("貼圖作品");
        gifWork = new JMenuItem("GIF作品");

        item1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在此處理貼圖生成的功能
                JOptionPane.showMessageDialog(null, "執行貼圖生成功能");
            }
        });

        item2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在此處理GIF生成的功能
                JOptionPane.showMessageDialog(null, "執行GIF生成功能");
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
                JOptionPane.showMessageDialog(null, "打開我的GIF作品集");
            }
        });

        /*menu.setPreferredSize(new Dimension(200, 100)); //設定選項大小
        subMenu.setPreferredSize(new Dimension(200, 100));
        menu.setFont(new Font("", Font.BOLD, 16));//設定字體大小
        subMenu.setFont(new Font("", Font.BOLD, 16));*/
        
        menu.add(item1);
        menu.add(item2);
        menu.add(subMenu);
        subMenu.add(stickerWork);
        subMenu.add(gifWork);
        menuBar.add(menu);
        setJMenuBar(menuBar);
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

