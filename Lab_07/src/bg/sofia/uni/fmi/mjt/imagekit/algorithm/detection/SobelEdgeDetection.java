package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.GrayscaleAlgorithm;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.min;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm{

    private final ImageAlgorithm grayScale;
    private final int kernelX[][]={
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };
    private final int kernelY[][]={
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}
    };

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm){
        this.grayScale=grayscaleAlgorithm;
    }
    @Override
    public BufferedImage process(BufferedImage image) {
        if(image==null){
            throw  new IllegalArgumentException("image is null");
        }

        BufferedImage grayScaleImage=grayScale.process(image);
        BufferedImage edgeImage=new BufferedImage(grayScaleImage.getWidth(),grayScaleImage.getHeight(),BufferedImage.TYPE_INT_BGR);

        for(int x=1;x<image.getWidth()-1;x++){
            for(int y=1;y< image.getHeight()-1;y++){
                int pixelX=0;
                int pixelY=0;
                for(int i=-1;i<2;i++){
                    for(int j=-1;j<2;j++){
                        int grayColor= new Color(grayScaleImage.getRGB(x + i, y + j)).getGreen();
                        pixelX+=grayColor* kernelX[i+1][j+1];
                        pixelY+=grayColor* kernelY[i+1][j+1];
                    }
                }
                int magnitude=min((int) Math.sqrt(pixelX*pixelX+pixelY*pixelY),255);
                Color edgeColor= new Color(magnitude,magnitude,magnitude);
                edgeImage.setRGB(x,y,edgeColor.getRGB());
            }
        }
        return edgeImage;
    }
}
