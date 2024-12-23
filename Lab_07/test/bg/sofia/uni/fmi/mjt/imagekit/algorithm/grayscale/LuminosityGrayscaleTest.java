package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LuminosityGrayscaleTest {

    private final LuminosityGrayscale luminosityGrayscale = new LuminosityGrayscale();

    @Test
    void testProcessWithNullImage() {
        assertThrows(IllegalArgumentException.class, () -> luminosityGrayscale.process(null));
    }

    @Test
    void testProcessWith1Pixel() {
        BufferedImage singlePixelImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_BGR);
        singlePixelImage.setRGB(0, 0, new Color(50, 100, 150).getRGB());
        BufferedImage result = luminosityGrayscale.process(singlePixelImage);
        int grayValue = new Color(result.getRGB(0, 0)).getRed();
        int expectedGrayValue = (int) (50 * 0.21 + 100 * 0.72 + 150 * 0.07);
        assertEquals(expectedGrayValue, grayValue);
    }

    @Test
    public void testProcessWithMultiPixelImage() {
        BufferedImage multiPixelImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_BGR);
        multiPixelImage.setRGB(0, 0, new Color(10, 20, 30).getRGB());
        multiPixelImage.setRGB(1, 0, new Color(100, 150, 200).getRGB());
        multiPixelImage.setRGB(0, 1, new Color(255, 255, 255).getRGB());
        multiPixelImage.setRGB(1, 1, new Color(0, 0, 0).getRGB());

        BufferedImage result = luminosityGrayscale.process(multiPixelImage);

        assertEquals((int) (10 * 0.21 + 20 * 0.72 + 30 * 0.07), new Color(result.getRGB(0, 0)).getRed());
        assertEquals((int) (100 * 0.21 + 150 * 0.72 + 200 * 0.07), new Color(result.getRGB(1, 0)).getRed());
        assertEquals(254, new Color(result.getRGB(0, 1)).getRed());
        assertEquals(0, new Color(result.getRGB(1, 1)).getRed());
    }

    @Test
    public void testProcessDoesnChangeWidthHeight() {
        BufferedImage inputImage = new BufferedImage(5, 7, BufferedImage.TYPE_INT_BGR);
        BufferedImage result = luminosityGrayscale.process(inputImage);
        assertEquals(5, result.getWidth());
        assertEquals(7, result.getHeight());
    }
}