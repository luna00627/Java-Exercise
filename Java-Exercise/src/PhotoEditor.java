import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

public class PhotoEditor {

    private static JLabel imageLabel = new JLabel();
    private static BufferedImage selectedImage;
    private static BufferedImage originalImage;
    private static double zoomFactor = 1.0;
    private static Color selectedColor = Color.RED;  // 預設文字顏色
    private static String selectedFontName = "宋體"; // 預設字體名稱
    private static int fontSize = 50; // 預設字體大小
    private static JTextField positionOfX;
    private static JTextField positionOfY;

    public static void main(String[] args) {
        JFrame frame = new JFrame("照片編輯器");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Initial frame size

        JButton uploadButton = new JButton("選擇照片");
        JButton addTextButton = new JButton("添加文字");
        JButton clearTextButton = new JButton("清除文字");
        JButton saveImageButton = new JButton("保存圖片");
        JTextField textField = new JTextField(20);
        JTextField fontSizeField = new JTextField("50", 3); // 預設字體大小為50
        JSlider zoomSlider = new JSlider(10, 200, 100);  // Slider range from 10% to 200%

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("選擇照片");
                fileChooser.setFileFilter(new ImageFilter());
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        selectedImage = ImageIO.read(selectedFile);
                        originalImage = ImageIO.read(selectedFile);
                        zoomFactor = 1.0;
                        positionOfX.setText(Integer.toString(selectedImage.getWidth() / 2));
                        positionOfY.setText(Integer.toString(selectedImage.getHeight() / 2));
                        updateImageLabel();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        addTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedImage != null && !textField.getText().isEmpty()) {
                    try {
                        fontSize = Integer.parseInt(fontSizeField.getText());
                        addTextToImage(selectedImage, textField.getText());
                        updateImageLabel();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "請輸入有效的字體大小（整數）");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "請先選擇照片並輸入文字");
                }
            }
        });

        clearTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage != null) {
                    selectedImage = deepCopy(originalImage);
                    updateImageLabel();
                }
            }
        });

        saveImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedImage != null) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("保存圖片");
                    fileChooser.setFileFilter(new ImageFilter());
                    int result = fileChooser.showSaveDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File saveFile = fileChooser.getSelectedFile();
                        try {
                            ImageIO.write(selectedImage, "jpg", saveFile);
                            JOptionPane.showMessageDialog(frame, "圖片保存成功！");
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(frame, "圖片保存失敗：" + ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "請先選擇並編輯照片");
                }
            }
        });

        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                zoomFactor = zoomSlider.getValue() / 100.0;
                updateImageLabel();
            }
        });

        JPanel controlPanelTop = new JPanel();
        controlPanelTop.add(uploadButton);
        controlPanelTop.add(new JLabel("輸入文字:"));
        controlPanelTop.add(textField);
        controlPanelTop.add(new JLabel("文字大小:"));
        controlPanelTop.add(fontSizeField);
        controlPanelTop.add(addTextButton);
        controlPanelTop.add(clearTextButton);
        controlPanelTop.add(new JLabel("縮放:"));
        controlPanelTop.add(zoomSlider);
        controlPanelTop.add(saveImageButton);

        String[] colors = { "Blue", "Cyan", "Gray", "Green", "Magenta", "Orange", "Pink", "Red", "Yellow", "White", "Black" };
        JComboBox<String> colorComboBox = new JComboBox<>(colors);
        String[] fonts = { "宋體", "黑體", "微軟正黑體", "標楷體", "新細明體", "Arial", "Courier New", "Times New Roman", "Verdana", "Tahoma" };
        JComboBox<String> fontComboBox = new JComboBox<>(fonts);
        String[] positions = { "左上角", "上方中間", "右上角", "左方中間", "中心", "右方中間", "左下角", "下方中間", "右下角" };
        JComboBox<String> positionComboBox = new JComboBox<>(positions);

        colorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedColorName = (String) colorComboBox.getSelectedItem();
                switch (selectedColorName) {
                    case "White":
                        selectedColor = Color.WHITE;
                        break;
                    case "Black":
                        selectedColor = Color.BLACK;
                        break;
                    case "Blue":
                        selectedColor = Color.BLUE;
                        break;
                    case "Cyan":
                        selectedColor = Color.CYAN;
                        break;
                    case "Gray":
                        selectedColor = Color.GRAY;
                        break;
                    case "Green":
                        selectedColor = Color.GREEN;
                        break;
                    case "Magenta":
                        selectedColor = Color.MAGENTA;
                        break;
                    case "Orange":
                        selectedColor = Color.ORANGE;
                        break;
                    case "Pink":
                        selectedColor = Color.PINK;
                        break;
                    case "Red":
                        selectedColor = Color.RED;
                        break;
                    case "Yellow":
                        selectedColor = Color.YELLOW;
                        break;
                }
            }
        });

        fontComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedFontName = (String) fontComboBox.getSelectedItem();
            }
        });

        positionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle position selection
            }
        });

        positionOfX = new JTextField(3);
        positionOfY = new JTextField(3);

        JPanel controlPanelBottom = new JPanel();
        controlPanelBottom.add(new JLabel("文字顏色:"));
        controlPanelBottom.add(colorComboBox);
        controlPanelBottom.add(new JLabel("字體:"));
        controlPanelBottom.add(fontComboBox);
        controlPanelBottom.add(new JLabel("文字位置(x):"));
        controlPanelBottom.add(positionOfX);
        controlPanelBottom.add(new JLabel("文字位置(y):"));
        controlPanelBottom.add(positionOfY);

        frame.add(controlPanelTop, "North");
        frame.add(controlPanelBottom, "South");
        frame.add(new JScrollPane(imageLabel), "Center");

        frame.setVisible(true);
    }

    private static void updateImageLabel() {
        if (selectedImage != null) {
            ImageIcon icon = new ImageIcon(selectedImage);
            imageLabel.setIcon(scaleImageIcon(icon, imageLabel.getWidth(), imageLabel.getHeight()));
        }
    }

    private static ImageIcon scaleImageIcon(ImageIcon icon, int maxWidth, int maxHeight) {
        Image image = icon.getImage();
        int width = (int) (image.getWidth(null) * zoomFactor);
        int height = (int) (image.getHeight(null) * zoomFactor);

        double aspectRatio = (double) width / height;
        if (width > maxWidth || height > maxHeight) {
            double scale = Math.min((double) maxWidth / width, (double) maxHeight / height);
            width = (int) (width * scale);
            height = (int) (height * scale);
        }

        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private static void addTextToImage(BufferedImage image, String text) {
        Graphics2D g = image.createGraphics();
        g.setColor(selectedColor);
        g.setFont(new Font(selectedFontName, Font.PLAIN, fontSize));
        int x = Integer.parseInt(positionOfX.getText());
        int y = Integer.parseInt(positionOfY.getText());
        g.drawString(text, x, y);
        g.dispose();
    }

    private static void saveImage(BufferedImage image, String outputPath) {
        try {
            File outputFile = new File(outputPath);
            ImageIO.write(image, "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        BufferedImage copy = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        Graphics2D g = copy.createGraphics();
        g.drawImage(bi, 0, 0, null);
        g.dispose();
        return copy;
    }

    private static class ImageFilter extends FileFilter {
        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            String extension = getExtension(file);
            if (extension != null && (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png"))) {
                return true;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "圖像檔案 (*.jpg, *.png)";
        }

        private String getExtension(File file) {
            String fileName = file.getName();
            int index = fileName.lastIndexOf('.');
            if (index > 0) {
                return fileName.substring(index + 1);
            }
            return null;
        }
    }
}
