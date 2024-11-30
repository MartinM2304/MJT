package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements  GrayscaleAlgorithm{

    @Override
    public BufferedImage process(BufferedImage image) {
        if(image==null){
            throw  new IllegalArgumentException("image is null");
        }

        BufferedImage result=new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_BGR);
        for(int i=0;i<result.getWidth();i++){
            for(int j=0;j<result.getHeight();j++){
                Color color=new Color(image.getRGB(i,j));
                int scale= (int)(color.getRed()*0.21+ color.getGreen()*0.72+ color.getBlue()*0.07);
                Color gray= new Color(scale,scale,scale);
                result.setRGB(i,j,gray.getRGB());
            }
        }
        return result;
    }
}
