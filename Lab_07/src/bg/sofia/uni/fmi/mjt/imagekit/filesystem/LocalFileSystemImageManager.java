package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {
    /**
     * Loads a single image from the given file path.
     *
     * @param imageFile the file containing the image.
     * @return the loaded BufferedImage.
     * @throws IllegalArgumentException if the file is null
     * @throws IOException              if the file does not exist, is not a regular file,
     *                                  or is not in one of the supported formats.
     */
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("Image file is null");
        }
        if (!imageFile.isFile()) {
            throw new IOException("Invalid file: " + imageFile.getPath());
        }

        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Unsupported image format for file: " + imageFile.getPath());
        }

        return image;
    }

    /**
     * Loads all images from the specified directory.
     *
     * @param imagesDirectory the directory containing the images.
     * @return A list of BufferedImages representing the loaded images.
     * @throws IllegalArgumentException if the directory is null.
     * @throws IOException              if the directory does not exist, is not a directory,
     *                                  or contains files that are not in one of the supported formats.
     */
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("Images directory cannot be null");
        }
        if (!imagesDirectory.isDirectory()) {
            throw new IOException("Invalid directory: " + imagesDirectory.getPath());
        }

        List<BufferedImage> images = new ArrayList<>();
        for (File file : imagesDirectory.listFiles()) {
            try {
                images.add(loadImage(file));
            } catch (IOException ignored) {
                System.out.println("exception caught");
            }
        }

        return images;
    }

    /**
     * Saves the given image to the specified file path.
     *
     * @param image     the image to save.
     * @param imageFile the file to save the image to.
     * @throws IllegalArgumentException if the image or file is null.
     * @throws IOException              if the file already exists or the parent directory does not exist.
     */
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("Image or file cannot be null");
        }
        if (imageFile.exists()) {
            throw new IOException("File already exists: " + imageFile.getPath());
        }

        String formatName = getFromat(imageFile.getName());
        ImageIO.write(image, formatName, imageFile);
    }

    private String getFromat(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png") && !extension.equals("bmp")) {
            throw new IllegalArgumentException("Unsupported file format: " + extension);
        }
        return extension;
    }
}
