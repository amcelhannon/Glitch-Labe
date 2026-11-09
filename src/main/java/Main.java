import Effects.EffectPipeline;
import Effects.ScanLineEffect;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Main
{
    public static void main(String[] args)
            throws Exception
    {
        //BufferedImage image = ImageIO.read(new File("input/a.png"));

        EffectPipeline pipeline = new EffectPipeline();
        pipeline.addImage("input/a.png");
        pipeline.addImage("input/b.png");
        pipeline.addImage("input/c.jpg");

        //public BufferedImage apply(BufferedImage image, int desiredLines, double thicknessPercentage, int alpha, double startOffset, double endOffset, boolean randomizeAlpha, boolean transitionLines) {

        String [] arguments = {
                "120",    // imageLines
                ".3",    // thicknessPercentage
                "150",   // alpha
                ".3",    // start offset
                "0.8",    // end offset
                "true", // randomizeAlpha
                "false"};// transitionLines

        ArrayList<BufferedImage> output  = pipeline.apply(new ScanLineEffect(), arguments);
        BufferedImage image;
        for (int i = 0; i < output.size(); i++){
            image = output.get(i);
            ImageIO.write(image, "png", new File("output/output" + i +".png"));
        }

    }
}