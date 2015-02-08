package andrews.jengine;

import java.awt.*;

public class Sprite {
    private Image image;
    public String name;

    public Sprite(Image image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }

    public void draw(final Graphics g, final int x, final int y) {
        g.drawImage(image, x, y, null);
    }

    public void draw(final Graphics g, final double x, final double y) {
        final int xi = Long.valueOf(Math.round(x)).intValue();
        final int yi = Long.valueOf(Math.round(y)).intValue();
        g.drawImage(image, xi, yi, null);
    }
}