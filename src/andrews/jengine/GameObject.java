package andrews.jengine;

import andrews.jengine.modules.Resources;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.*;
import java.util.Comparator;

public class GameObject {
    public String sprite = "";
    public String name = "";
    public int depth = 0;
    public boolean visible = true;
    public int x = 0;
    public int y = 0;

    public GameObject() {
    }

    @JsonIgnore
    public GameObject(String name) {
        this.name = name;
        this.sprite = name + "_synth_anm";
        final Animation animation = new Animation(this.sprite);
        animation.currentStep = 0;
        animation.isPlaying = false;
        Game.instance.resources.animations.generated.put(this.sprite, animation);
    }

    @JsonIgnore
    public void update() {
        Resources.animation(sprite).update();
    }

    @JsonIgnore
    public void render(Graphics g) {
        if (visible) {
            final Animation animation = Resources.animation(sprite);
            if (animation != null) {
                Animation.Step step = animation.getStep();
                if ((step != null) && (step.sprite != null)) {
                    Resources.sprite(step.sprite).draw(g, x, y);
                }
            }
        }
    }

    /**
     * When key is NOW pressed
     *
     * @param keycode
     */
    @JsonIgnore
    public void onKeyPress(int keycode) {

    }

    /**
     * When key was pressed and released.
     *
     * @param keycode
     */
    @JsonIgnore
    public void onKey(int keycode) {

    }

    @JsonIgnore
    public void onMouseClick() {
        System.out.println("Clicked object " + name);
    }

    @JsonIgnore
    public void onGlobalMouseClick(final double x, final double y) {
        if (visible) {
            if ((x >= this.x) && (y >= this.y)) {
                final Animation.Step st = Resources.animation(sprite).getStep();
                if (st != null) {
                    final Sprite spr = Resources.sprite(st.sprite);
                    if (spr != null) {
                        if ((this.x + spr.getWidth() > x)
                                && (this.y + spr.getHeight() > y)) {
                            onMouseClick();
                        }
                    }
                }
            }
        }
    }

    @JsonIgnore
    public static final Comparator<GameObject> compareByDepth = new Comparator<GameObject>() {
        @Override
        public int compare(GameObject o1, GameObject o2) {
            return Integer.compare(o1.depth, o2.depth);
        }
    };
}
