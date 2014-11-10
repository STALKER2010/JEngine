package andrews.jengine;

import andrews.jengine.modules.Resources;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.*;

public class Background {
	public String sprite = "";
	public String name = "";
	public boolean visible = true;

    public Background() {

    }

    @JsonIgnore
	public Background(String name, Sprite sprite) {
		this.name = name;
        this.sprite = name + "_synth_anm";
        final Animation animation = new Animation(this.sprite);
        animation.currentStep = 0;
        animation.isPlaying = false;
        if (sprite != null) {
            animation.addStep(sprite);
        }
        Game.instance.resources.animations.generated.put(this.sprite, animation);
	}

    @JsonIgnore
    public void draw(final Graphics g) {
        final Animation ba = Resources.animation(sprite);
        if (ba != null) {
            final Animation.Step bs = ba.getStep();
            if ((bs != null) && (bs.sprite != null)) {
                Sprite bsp = Resources.sprite(bs.sprite);
                if (bsp != null) {
                    bsp.draw(g, 0, 0);
                }
            }
        }
    }
}
