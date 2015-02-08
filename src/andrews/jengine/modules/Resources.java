package andrews.jengine.modules;

import andrews.jengine.Animation;
import andrews.jengine.Game;
import andrews.jengine.Sprite;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author STALKER_2010
 */
public class Resources {
    public AnimationsDB animations = new AnimationsDB();

    public Resources(final Game game) {
        tracker = new MediaTracker(game);
    }

    private final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private final MediaTracker tracker;
    private final Map<String, Integer> imgMap = new HashMap<>();
    private final Map<String, Sprite> sprites = new HashMap<>();

    public Sprite getSprite(String path) {
        if (sprites.containsKey(path)) {
            return sprites.get(path);
        } else {
            Image image = toolkit.getImage("assets/" + path);
            {
                int id;
                if (imgMap.containsKey(path)) {
                    id = imgMap.get(path);
                } else {
                    id = imgMap.size();
                    imgMap.put(path, id);
                }
                tracker.addImage(image, id);
            }
            try {
                tracker.waitForAll();
            } catch (InterruptedException e1) {
                e1.printStackTrace(System.err);
            }

            Sprite sprite = new Sprite(image);
            sprite.name = path;

            sprites.put(path, sprite);

            return sprite;
        }
    }

    public static Sprite sprite(final String path) {
        return Game.instance.resources.getSprite(path);
    }

    public static Animation animation(final String name) {
        return Game.instance.resources.animations.get(name);
    }

    public static class AnimationsDB {
        public Map<String, Animation> generated = new HashMap<>();

        public AnimationsDB() {

        }

        @JsonIgnore
        public Map<String, Animation> internal = new HashMap<>();

        @JsonIgnore
        public Animation get(final String name) {
            Animation res = null;
            if (internal.containsKey(name)) {
                res = internal.get(name);
            } else if (generated.containsKey(name)) {
                res = generated.get(name);
            }
            if (res == null) {
                new RuntimeException("Animation not found: " + name).printStackTrace(System.out);
            }
            return res;
        }
    }
}
