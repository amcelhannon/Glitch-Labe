package Effects;

import java.awt.image.BufferedImage;

public interface FrameEffect
{

    BufferedImage apply(BufferedImage image, String[] args);
}