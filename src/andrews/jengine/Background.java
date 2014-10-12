package andrews.jengine;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
}
