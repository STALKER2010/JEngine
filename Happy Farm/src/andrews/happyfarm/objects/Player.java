package andrews.happyfarm.objects;

import andrews.jengine.Animation;
import andrews.jengine.GameObject;
import andrews.jengine.modules.Resources;

import java.awt.event.KeyEvent;

/**
 * @author STALKER_2010
 */
public class Player extends GameObject {
    protected Integer inertion = 0;
    public Integer direction = 0;

    public Player() {
        super();
    }

    public Player(String name) {
        super(name);
        final Animation a = Resources.animation(sprite);
        a.steps.clear();
        a.addStep(Resources.sprite("sPlayer_0.png"));
        a.addStep(Resources.sprite("sPlayer_1.png"));
        a.addStep(Resources.sprite("sPlayer_2.png"));
        a.isLooped = true;
        a.isPlaying = true;
    }

    @Override
    public void update() {
        super.update();
        inertion--;
        if (inertion < 0)
            inertion = 0;
        if (direction == 0) {
            y -= inertion;
        } else if (direction == 90) {
            x += inertion;
        } else if (direction == 180) {
            y += inertion;
        } else if (direction == 270) {
            x -= inertion;
        }
    }

    @Override
    public void onKeyPress(int keycode) {
        super.onKeyPress(keycode);
        if (keycode == KeyEvent.VK_A) {
            inertion += 5;
            direction = 270;
        }
        if (keycode == KeyEvent.VK_D) {
            inertion += 5;
            direction = 90;
        }
        if (keycode == KeyEvent.VK_W) {
            inertion += 5;
            direction = 0;
        }
        if (keycode == KeyEvent.VK_S) {
            inertion += 5;
            direction = 180;
        }
        if (inertion > 15)
            inertion = 15;
    }
}
