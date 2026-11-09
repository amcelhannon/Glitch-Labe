package Effects;
//import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.ThreadLocalRandom;

public class ScanLineEffect implements FrameEffect{
    static Random random = new Random();

    public BufferedImage apply(BufferedImage image, Random random) {
        return apply(
                image,
                120,
                0.75,
                60,
                0.3,
                0.8,
                true,
                true);
    }

    public BufferedImage apply(BufferedImage image, int imageLines, double thicknessPercentage, int alpha, double startOffset, double endOffset, boolean randomizeAlpha, boolean transitionLines) {
        if (imageLines <= 0) throw new IllegalArgumentException("spacing must be > 0");
        if (thicknessPercentage <= 0 || thicknessPercentage >1) throw new IllegalArgumentException("thicknessPercentage must be > 0 or < 1");
        if(startOffset < 0 || startOffset > 1) throw new IllegalArgumentException("startOffset must be between 0 and 1");
        if(endOffset < 0 || endOffset > 1) throw new IllegalArgumentException("endOffset must be between 0 and 1");
        if (startOffset > endOffset) throw new IllegalArgumentException("startOffset > endOffset");

        alpha = Math.clamp(alpha, 0, 255);
        int start = (int) Math.clamp(image.getHeight() * startOffset, 0, image.getHeight());
        int end = (int) Math.clamp(image.getHeight() * endOffset, 0, image.getHeight());

        int spacing = Math.max(1, image.getHeight()/ imageLines); //pixels per "row"
        int overlayThickness = Math.max(1, (int) (spacing * thicknessPercentage)); //pixels for darker "scanLine" overlay

        if(!transitionLines) return simpleApply(image, spacing, overlayThickness, alpha, start, end, randomizeAlpha);

        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = output.createGraphics();
        double blendCoefficient = 0.3;

        int currentAlpha = alpha;
        try {
            graphics.drawImage(image, 0, 0, null);

            for (int y = start; y < end; y += spacing) {
                if (randomizeAlpha) {
                    currentAlpha = getRandomizedAlpha(alpha);
                }


                // Top blend line
                int topY = y - 1;
                if (topY >= 0) {
                    graphics.setColor( new Color(0, 0, 0, (int) (currentAlpha * blendCoefficient)));
                    graphics.fillRect(0, topY, image.getWidth(), 1);
                }


                // Main scanline
                graphics.setColor(new Color(0, 0, 0, currentAlpha));

                graphics.fillRect(0, y, image.getWidth(), Math.min(overlayThickness, image.getHeight() - y));

                // Bottom blend line
                int bottomY = y + overlayThickness;
                if (bottomY < image.getHeight()) {
                    graphics.setColor( new Color(0, 0, 0, (int) (currentAlpha * blendCoefficient)));
                    graphics.fillRect(0, bottomY, image.getWidth(), 1);
                }
            }
        }
        finally {
            graphics.dispose();
        }

        return output;
    }

    public int getRandomizedAlpha(int alpha) {
        alpha += ThreadLocalRandom.current().nextInt(-10, 11);
        return Math.clamp(alpha, 0, 255);
    }

    public BufferedImage simpleApply(BufferedImage image, int spacing, int thickness, int alpha, int start, int end, boolean randomizeAlpha) {
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = output.createGraphics();
        Color lineColor = new Color(0,0,0, alpha);

        try {
            graphics.drawImage(image, 0, 0, null);

            for (int y = start; y < end; y += spacing) {
                if (randomizeAlpha)
                    lineColor = new Color(0,0,0, getRandomizedAlpha(alpha));

                graphics.setColor(lineColor);
                graphics.fillRect(0, y, image.getWidth(), Math.min(thickness, image.getHeight() - y));
            }
        }
        finally {
            graphics.dispose();
        }

        return output;

    }

    @Override
    public BufferedImage apply(BufferedImage image, String[] args) {
        if (args.length != 7)
            throw new IllegalArgumentException("Wrong number of arguments");

        //public BufferedImage apply(BufferedImage image, int imageLines, double thicknessPercentage, int alpha, double startOffset, double endOffset, boolean randomizeAlpha, boolean transitionLines) {
        int imageLines = Integer.parseInt(args[0]);
        double thicknessPercentage = Double.parseDouble(args[1]);
        int alpha = Integer.parseInt(args[2]);
        double startOffset = Double.parseDouble(args[3]);
        double endOffset = Double.parseDouble(args[4]);
        boolean randomizeAlpha = Boolean.parseBoolean(args[5]);
        boolean transitionLines = Boolean.parseBoolean(args[6]);

        return apply(image, imageLines, thicknessPercentage, alpha, startOffset, endOffset, randomizeAlpha, transitionLines);

    }
}