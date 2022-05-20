package run.stitch.blog.config;

import com.google.code.kaptcha.BackgroundProducer;
import com.google.code.kaptcha.util.Configurable;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class NoKaptchaBackground extends Configurable implements BackgroundProducer {
    public NoKaptchaBackground() {
    }

    @Override
    public BufferedImage addBackground(BufferedImage bufferedImage) {
        int width = 200;
        int height = 60;
        BufferedImage imageWithBackground = new BufferedImage(width, height, 1);
        Graphics2D graph = (Graphics2D) imageWithBackground.getGraphics();
        graph.fill(new Rectangle2D.Double(0.0D, 0.0D, width, height));
        graph.drawImage(bufferedImage, 0, 0, null);
        return imageWithBackground;
    }
}
