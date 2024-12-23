package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SobelEdgeDetectionTest {
    LuminosityGrayscale grayscale = mock();
    SobelEdgeDetection edgeDetection = new SobelEdgeDetection(grayscale);

    @Test
    void testProcessWithNullImage() {
        assertThrows(IllegalArgumentException.class, () -> edgeDetection.process(null));
    }

    @Test
    public void testProcessWithSinglePixelImage() {
        BufferedImage singlePixelImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_BGR);
        singlePixelImage.setRGB(0, 0, new Color(50, 50, 50).getRGB());

        when(grayscale.process(singlePixelImage)).thenReturn(singlePixelImage);
        BufferedImage result = edgeDetection.process(singlePixelImage);
        assertEquals(1, result.getWidth());
        assertEquals(1, result.getHeight());
        int grayValue = new Color(result.getRGB(0, 0)).getRed();
        assertEquals(0, grayValue);
    }

    @Test
    void testProcessWith2Pixels() {
        BufferedImage smallImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_BGR);
        smallImage.setRGB(0, 0, new Color(10, 20, 30).getRGB());
        smallImage.setRGB(1, 0, new Color(40, 50, 60).getRGB());
        smallImage.setRGB(0, 1, new Color(70, 80, 90).getRGB());
        smallImage.setRGB(1, 1, new Color(100, 110, 120).getRGB());
        when(grayscale.process(smallImage)).thenReturn(smallImage);

        BufferedImage result = edgeDetection.process(smallImage);
        assertEquals(2, result.getWidth());
        assertEquals(2, result.getHeight());

        int grayValue = new Color(result.getRGB(1, 1)).getRed();
        assertTrue(grayValue >= 0 && grayValue <= 255);
    }

    @Test
    public void testProcessWithMultiPixelImage() {
        BufferedImage multiPixelImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_BGR);

        multiPixelImage.setRGB(0, 0, new Color(10, 20, 30).getRGB());
        multiPixelImage.setRGB(1, 0, new Color(40, 50, 60).getRGB());
        multiPixelImage.setRGB(2, 0, new Color(70, 80, 90).getRGB());
        multiPixelImage.setRGB(0, 1, new Color(100, 110, 120).getRGB());
        multiPixelImage.setRGB(1, 1, new Color(130, 140, 150).getRGB());
        multiPixelImage.setRGB(2, 1, new Color(160, 170, 180).getRGB());
        multiPixelImage.setRGB(0, 2, new Color(190, 200, 210).getRGB());
        multiPixelImage.setRGB(1, 2, new Color(220, 230, 240).getRGB());
        multiPixelImage.setRGB(2, 2, new Color(250, 255, 255).getRGB());

        when(grayscale.process(multiPixelImage)).thenReturn(multiPixelImage);
        BufferedImage result = edgeDetection.process(multiPixelImage);
        assertEquals(3, result.getWidth());
        assertEquals(3, result.getHeight());
 int grayValueCenter = new Color(result.getRGB(1, 1)).getRed();
        assertTrue(grayValueCenter >= 0 && grayValueCenter <= 255);
}
}