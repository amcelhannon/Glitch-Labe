package Effects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EffectPipeline
{
     ArrayList <BufferedImage> images = new ArrayList<>();

    public void addImage(String filename) throws IOException {
        images.add(ImageIO.read(new File(filename)));
    }

    public ArrayList<BufferedImage>  apply(FrameEffect effect, String ... args) {
        ArrayList<BufferedImage> output = new ArrayList<>();
        for(BufferedImage image : images) {

            output.add(effect.apply(image, args));
        }
        return output;
    }
}