import com.aspose.imaging.Color;
import com.aspose.imaging.Image;
import com.aspose.imaging.Point;
import com.aspose.imaging.RasterImage;
import com.aspose.imaging.Rectangle;
import com.aspose.imaging.fileformats.png.PngColorType;
import com.aspose.imaging.imageoptions.PngOptions;
import com.aspose.imaging.masking.ImageMasking;
import com.aspose.imaging.masking.IMaskingSession;
import com.aspose.imaging.masking.options.AutoMaskingArgs;
import com.aspose.imaging.masking.options.AutoMaskingGraphCutOptions;
import com.aspose.imaging.masking.options.DetectedObjectType;
import com.aspose.imaging.masking.options.SegmentationMethod;
import com.aspose.imaging.masking.result.MaskingResult;
import com.aspose.imaging.sources.FileCreateSource;
import com.aspose.imaging.masking.options.AssumedObjectData;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Main {

    // Folder that contains images to process
    static final String templatesFolder = "D:\\Data\\";

    public static void main(String[] args) {
        // Run the example
        removeBackgroundGenericExample();
    }

    public static void removeBackgroundGenericExample() {
        List<String> rasterFormats = Arrays.asList("jpg", "png", "bmp", "apng", "dicom", "jp2", "j2k", "tga", "webp", "tif", "gif");

        List<String> vectorFormats = Arrays.asList("svg", "otg", "odg", "eps", "wmf", "emf", "wmz", "emz", "cmx", "cdr");

        List<String> allFormats = new LinkedList<>(rasterFormats);
        allFormats.addAll(vectorFormats);

        allFormats.forEach(new Consumer<String>() {
            @Override
            public void accept(String formatExt) {
                String inputFile = templatesFolder + "cat." + formatExt;

                boolean isVectorFormat = vectorFormats.contains(formatExt);

                // Need to rasterize vector formats before background remove
                if (isVectorFormat) {
                    inputFile = rasterizeVectorImage(formatExt, inputFile);
                }

                String outputFile = templatesFolder + "remove_background." + formatExt;

                System.out.println("Processing " + formatExt);

                try (RasterImage image = (RasterImage) Image.load(inputFile)) {
                    AutoMaskingGraphCutOptions maskingOptions = new AutoMaskingGraphCutOptions();
                    maskingOptions.setCalculateDefaultStrokes(true);
                    maskingOptions.setFeatheringRadius(1);
                    maskingOptions.setMethod(SegmentationMethod.GraphCut);
                    PngOptions pngOptions = new PngOptions();
                    pngOptions.setColorType(PngColorType.TruecolorWithAlpha);
                    pngOptions.setSource(new FileCreateSource(outputFile, false));
                    maskingOptions.setExportOptions(pngOptions);
                    maskingOptions.setBackgroundReplacementColor(Color.getGreen());

                    try (MaskingResult maskingResult = new ImageMasking(image).decompose(maskingOptions)) {
                        try (Image resultImage = maskingResult.get_Item(1).getImage()) {
                            resultImage.save();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Remove rasterized vector image
                if (isVectorFormat) {
                    new File(inputFile).delete();
                }
            }
        });
    }

    private static String rasterizeVectorImage(String formatExt, String inputFile) {
        String outputFile = templatesFolder + "happy." + formatExt + ".png";
        try (Image image = Image.load(inputFile)) {
            image.save(outputFile, new PngOptions());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputFile;
    }

    public static void removeBackgroundProcessingWithManualRectangles() {
        List<String> rasterFormats = Arrays.asList("jpg","jpeg", "png", "bmp", "apng", "dicom", "jp2", "j2k", "tga", "webp", "tif", "gif");

        List<String> vectorFormats = Arrays.asList("svg", "otg", "odg", "eps", "wmf", "emf", "wmz", "emz", "cmx", "cdr");

        List<String> allFormats = new LinkedList<>(rasterFormats);
        allFormats.addAll(vectorFormats);

        allFormats.forEach(new Consumer<String>() {
            @Override
            public void accept(String formatExt) {
                String inputFile = templatesFolder + "cat." + formatExt;

                boolean isVectorFormat = vectorFormats.contains(formatExt);

                // Need to rasterize vector formats before background remove
                if (isVectorFormat) {
                    inputFile = rasterizeVectorImage(formatExt, inputFile);
                }

                String outputFile = templatesFolder + "remove_background_manual_rectangles." + formatExt;

                System.out.println("Processing " + formatExt);

                try (RasterImage image = (RasterImage) Image.load(inputFile)) {
                    AutoMaskingGraphCutOptions maskingOptions = new AutoMaskingGraphCutOptions();
                    maskingOptions.setFeatheringRadius(2);
                    maskingOptions.setMethod(SegmentationMethod.GraphCut);

                    AutoMaskingArgs maskingArgs = new AutoMaskingArgs();
                    maskingArgs.setObjectsRectangles(new Rectangle[]{
                            // girl's bound box
                            new Rectangle(87, 47, 123, 308),
                            // boy's bound box
                            new Rectangle(180, 24, 126, 224)
                    });
                    maskingOptions.setArgs(maskingArgs);

                    PngOptions pngOptions = new PngOptions();
                    pngOptions.setColorType(PngColorType.TruecolorWithAlpha);
                    pngOptions.setSource(new FileCreateSource(outputFile, false));
                    maskingOptions.setExportOptions(pngOptions);

                    IMaskingSession maskingSession = new ImageMasking(image).createSession(maskingOptions);
                    try {
                        // first run of segmentation
                        maskingSession.decompose().dispose();

                        AutoMaskingArgs argsWithUserMarkers = new AutoMaskingArgs();
                        argsWithUserMarkers.setObjectsPoints(new Point[][]{
                                // background markers
                                null,
                                // foreground markers
                                new UserMarker()
                                        // boy's head
                                        .addPoint(218, 48, 10)
                                        // girl's head
                                        .addPoint(399, 66, 10)
                                        // girl's body
                                        .addPoint(158, 141, 10)
                                        .addPoint(158, 209, 20)
                                        .addPoint(115, 225, 5)
                                        .getPoints()
                        });

                        try (MaskingResult maskingResult = maskingSession.improveDecomposition(argsWithUserMarkers)) {
                            try (Image resultImage = maskingResult.get_Item(1).getImage()) {
                                resultImage.save();
                            }
                        }
                    } finally {
                        maskingSession.dispose();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Remove rasterized vector image
                if (isVectorFormat) {
                    new File(inputFile).delete();
                }
            }
        });
    }

    public static void removeBackgroundAutoProcessingWithAssumedObjects() {
        List<String> rasterFormats = Arrays.asList("jpg","jpeg", "png", "bmp", "apng", "dicom", "jp2", "j2k", "tga", "webp", "tif", "gif");

        List<String> vectorFormats = Arrays.asList("svg", "otg", "odg", "eps", "wmf", "emf", "wmz", "emz", "cmx", "cdr");

        List<String> allFormats = new LinkedList<>(rasterFormats);
        allFormats.addAll(vectorFormats);

        allFormats.forEach(new Consumer<String>() {
            @Override
            public void accept(String formatExt) {
                String inputFile = templatesFolder + "couple." + formatExt;

                boolean isVectorFormat = vectorFormats.contains(formatExt);

                // Need to rasterize vector formats before background remove
                if (isVectorFormat) {
                    inputFile = rasterizeVectorImage(formatExt, inputFile);
                }

                String outputFile = templatesFolder + "remove_background_auto_assumed_objects." + formatExt;

                System.out.println("Processing " + formatExt);

                try (RasterImage image = (RasterImage) Image.load(inputFile)) {
                    AutoMaskingGraphCutOptions maskingOptions = new AutoMaskingGraphCutOptions();
                    final LinkedList<AssumedObjectData> assumedObjects = new LinkedList<>();
                    // girl's bound box
                    assumedObjects.add(new AssumedObjectData(DetectedObjectType.Human, new Rectangle(87, 47, 123, 308)));
                    // boy's bound box
                    assumedObjects.add(new AssumedObjectData(DetectedObjectType.Human, new Rectangle(180, 24, 126, 224)));
                    maskingOptions.setAssumedObjects(assumedObjects);
                    maskingOptions.setCalculateDefaultStrokes(true);
                    maskingOptions.setFeatheringRadius(1);
                    maskingOptions.setMethod(SegmentationMethod.GraphCut);
                    PngOptions pngOptions = new PngOptions();
                    pngOptions.setColorType(PngColorType.TruecolorWithAlpha);
                    pngOptions.setSource(new FileCreateSource(outputFile, false));
                    maskingOptions.setExportOptions(pngOptions);
                    maskingOptions.setBackgroundReplacementColor(Color.getGreen());

                    try (MaskingResult maskingResult = new ImageMasking(image).decompose(maskingOptions)) {
                        try (Image resultImage = maskingResult.get_Item(1).getImage()) {
                            resultImage.save();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Remove rasterized vector image
                if (isVectorFormat) {
                    new File(inputFile).delete();
                }
            }
        });
    }

    public static void removeBackgroundAutoProcessing() {
        List<String> rasterFormats = Arrays.asList("jpg","jpeg", "png", "bmp", "apng", "dicom", "jp2", "j2k", "tga", "webp", "tif", "gif");

        List<String> vectorFormats = Arrays.asList("svg", "otg", "odg", "eps", "wmf", "emf", "wmz", "emz", "cmx", "cdr");

        List<String> allFormats = new LinkedList<>(rasterFormats);
        allFormats.addAll(vectorFormats);

        allFormats.forEach(new Consumer<String>() {
            @Override
            public void accept(String formatExt) {
                String inputFile = templatesFolder + "couple." + formatExt;

                boolean isVectorFormat = vectorFormats.contains(formatExt);

                // Need to rasterize vector formats before background remove
                if (isVectorFormat) {
                    inputFile = rasterizeVectorImage(formatExt, inputFile);
                }

                String outputFile = templatesFolder + "remove_background_auto." + formatExt;

                System.out.println("Processing " + formatExt);

                try (RasterImage image = (RasterImage) Image.load(inputFile)) {
                    AutoMaskingGraphCutOptions maskingOptions = new AutoMaskingGraphCutOptions();
                    maskingOptions.setFeatheringRadius(1);
                    maskingOptions.setMethod(SegmentationMethod.GraphCut);
                    PngOptions pngOptions = new PngOptions();
                    pngOptions.setColorType(PngColorType.TruecolorWithAlpha);
                    pngOptions.setSource(new FileCreateSource(outputFile, false));
                    maskingOptions.setExportOptions(pngOptions);
                    maskingOptions.setBackgroundReplacementColor(Color.getGreen());

                    try (MaskingResult maskingResult = new ImageMasking(image).decompose(maskingOptions)) {
                        try (Image resultImage = maskingResult.get_Item(1).getImage()) {
                            resultImage.save();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Remove rasterized vector image
                if (isVectorFormat) {
                    new File(inputFile).delete();
                }
            }
        });
    }

    static class UserMarker {
        private final List<Point> list = new LinkedList<>();

        public UserMarker addPoint(int left, int top, int radius) {
            for (int y = top - radius; y <= top + radius; y++) {
                for (int x = left - radius; x <= left + radius; x++) {
                    this.list.add(new Point(x, y));
                }
            }
            return this;
        }

        public Point[] getPoints() {
            return this.list.toArray(new Point[0]);
        }
    }
}
