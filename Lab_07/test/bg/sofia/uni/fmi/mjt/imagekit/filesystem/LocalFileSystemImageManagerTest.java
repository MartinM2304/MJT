package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class LocalFileSystemImageManagerTest {

    private File file;
    private File dir;
    private LocalFileSystemImageManager manager;

    @BeforeEach
    void start() {
        file = mock(File.class);
        dir = mock(File.class);
        manager = new LocalFileSystemImageManager();
    }

    @Test
    void testLoadImageWithNullFile() {
        assertThrows(IllegalArgumentException.class, () -> manager.loadImage(null));
    }

    @Test
    void testLoadImageFileNonExistent() {
        assertThrows(IOException.class, () -> manager.loadImage(file));
    }

    @Test
    void testLoadImageSuccessfully() throws IOException {
        dir = Files.createTempDirectory("tmpDir").toFile();
        dir.deleteOnExit();

        file = Files.createTempFile(dir.toPath(), "tmpImg", ".png").toFile();
        file.deleteOnExit();

        BufferedImage tmpImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(tmpImg, "png", file);

        BufferedImage image = manager.loadImage(file);
        assertNotNull(image, "succ");
    }


    @Test
    void testLoadImageWithUnsupportedFormat() throws IOException {
        file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.getPath()).thenReturn("invalidFile.tmp");

        BufferedImage mockImage = null;
        mockStatic(ImageIO.class);
        when(ImageIO.read(file)).thenReturn(mockImage);

        assertThrows(IOException.class, () -> manager.loadImage(file)
                , "Expected IOException");
    }

    @Test
    void testLoadImageWithUnExistentFile() {
        File notExist = new File("notExist.png");
        assertThrows(IOException.class,
                () -> manager.loadImage(notExist),
                "Expected IOException for ");
    }

    @Test
    void testLoadImageInvalid() {
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(false);

        assertThrows(IOException.class, () -> manager.loadImage(file));
    }

    @Test
    void testLoadImagesFromNotEmptyDirectory() throws IOException {
        dir = Files.createTempDirectory("tmpDir").toFile();
        dir.deleteOnExit();

        file = Files.createTempFile(dir.toPath(), "tmpImg", ".png").toFile();
        file.deleteOnExit();

        BufferedImage tmpImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(tmpImg, "png", file);
        List<BufferedImage> images = manager.loadImagesFromDirectory(dir);
        assertNotNull(images, "Images list should not be null.");
    }

    @Test
    void testLoadImagesFromDirectoryWithNull() {
        assertThrows(IllegalArgumentException.class,
                () -> manager.loadImagesFromDirectory(null),
                "Expected IllegalArgumentException");
    }

    @Test
    void testSaveImageWithNullImage() {
        assertThrows(IllegalArgumentException.class, () -> manager.saveImage(null, file),
                "Expected IllegalArgumentException");
    }

    @Test
    void testLoadImagesFromDirectoryWithUnExistentDirectory() {
        File notDir = new File("notDir");
        assertThrows(IOException.class,
                () -> manager.loadImagesFromDirectory(notDir),
                "Expected IOException");
    }

    @Test
    void testLoadImagesFromDirectoryInvalidDir() throws IOException {
        dir = Files.createTempDirectory("tmpDir").toFile();
        dir.deleteOnExit();

        file = Files.createTempFile(dir.toPath(), "tmpImg", ".png").toFile();
        file.deleteOnExit();

        BufferedImage tmpImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(tmpImg, "png", file);
        File invalidDir = new File(dir, "invalidDirectory.txt");
        assertThrows(IOException.class, () -> {
            manager.loadImagesFromDirectory(invalidDir);
        }, "Expected IOException.");
    }


    @Test
    void testSaveImageFromDirNotExist() {
        File tmpDir = new File("tempDir/test.png");
        File parentDir = tmpDir.getParentFile();
        if (parentDir != null && parentDir.exists()) {
            parentDir.delete();
        }
        BufferedImage tmpImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        assertThrows(IOException.class, () -> manager.saveImage(tmpImg, tmpDir),
                "Expected IOException if parent directory cannot be created");
    }

    @Test
    void testSaveImageWithUnsupportedFormat() throws IOException {
        dir = Files.createTempDirectory("tmpDir").toFile();
        dir.deleteOnExit();
        file = Files.createTempFile(dir.toPath(), "tmpImg", ".png").toFile();
        file.deleteOnExit();
        BufferedImage tmpImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(tmpImg, "png", file);
        File tmpFormat = new File(dir, "tmpFile.tmp");
        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        assertThrows(IOException.class, () -> manager.saveImage(testImage, tmpFormat),
                "Expected IOException");
    }

    @Test
    void testSaveImageSuccessfuly() throws IOException {
        dir = Files.createTempDirectory("tmpDir").toFile();
        dir.deleteOnExit();
        file = Files.createTempFile(dir.toPath(), "tmpImg", ".png").toFile();
        file.deleteOnExit();

        BufferedImage tmpImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(tmpImg, "png", file);
        File outputFile = new File(dir, "saved.png");

        BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        manager.saveImage(testImage, outputFile);
        assertTrue(outputFile.exists(), "Image file should be saved.");
        outputFile.deleteOnExit();
    }

    @Test
    void testSaveImageWithNoExtension() throws IOException {
        dir = Files.createTempDirectory("tmpDir").toFile();
        dir.deleteOnExit();
        file = Files.createTempFile(dir.toPath(), "tmpImg", ".png").toFile();
        file.deleteOnExit();
        BufferedImage tmpImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(tmpImg, "png", file);
        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        File fileWithoutExtension = new File(dir, "no-extension-file");

        assertThrows(IOException.class, () -> {
            manager.saveImage(testImage, fileWithoutExtension);
        }, "Expected IOException");
    }

    @Test
    void testSaveImageWithExistingFile() throws IOException {
        dir = Files.createTempDirectory("tmpDir").toFile();
        dir.deleteOnExit();
        file = Files.createTempFile(dir.toPath(), "tmpImg", ".png").toFile();
        file.deleteOnExit();
        BufferedImage tmpImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(tmpImg, "png", file);

        BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        File existingFile = new File(dir, "existing-image.png");
        ImageIO.write(testImage, "png", existingFile);
        assertThrows(IOException.class, () -> manager.saveImage(testImage, existingFile),
                "Expected IOException");
    }

    @Test
    void testLoadImagesFromDirectoryWithMixedFiles() throws IOException {
        dir = Files.createTempDirectory("tmpDir").toFile();
        dir.deleteOnExit();
        file = Files.createTempFile(dir.toPath(), "validImg", ".png").toFile();
        file.deleteOnExit();
        BufferedImage validImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(validImage, "png", file);

        File invalidFile = new File(dir, "invalidFile.txt");
        invalidFile.createNewFile();
        invalidFile.deleteOnExit();

        List<BufferedImage> images = manager.loadImagesFromDirectory(dir);
        assertEquals(1, images.size(), "Only valid images should be loaded.");
    }

    @Test
    void testSaveImageToDirectory() throws IOException {
        dir = Files.createTempDirectory("tmpDir").toFile();
        dir.deleteOnExit();
        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        assertThrows(IOException.class, () -> manager.saveImage(testImage, dir),
                "Expected IOException");
    }

    @Test
    void testLoadImageValidUnsupportedFile() throws IOException {
        dir = Files.createTempDirectory("tmpDir").toFile();
        dir.deleteOnExit();
        file = Files.createTempFile(dir.toPath(), "validUnsupported", ".tiff").toFile();
        file.deleteOnExit();

        assertThrows(IOException.class, () -> manager.loadImage(file),
                "Expected IOException");
    }

    @Test
    void testSaveImageToReadOnlyFile() throws IOException {
        dir = Files.createTempDirectory("tmpDir").toFile();
        dir.deleteOnExit();
        File readOnlyFile = new File(dir, "readonly.png");
        readOnlyFile.createNewFile();
        readOnlyFile.setWritable(false);
        readOnlyFile.deleteOnExit();

        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        assertThrows(IOException.class, () -> manager.saveImage(testImage, readOnlyFile),
                "Expected IOException");
    }

    @Test
    void testSaveImageParentDirectoryCreationFails() {
        File mockFile = mock(File.class);
        File mockParent = mock(File.class);

        when(mockFile.getParentFile()).thenReturn(mockParent);
        when(mockFile.exists()).thenReturn(false);
        when(mockParent.exists()).thenReturn(false);
        when(mockParent.mkdirs()).thenReturn(false);

        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        assertThrows(IOException.class, () -> manager.saveImage(testImage, mockFile),
                "Expected IOException when parent directory creation fails.");
    }

}