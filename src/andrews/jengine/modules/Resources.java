package andrews.jengine.modules;

import andrews.jengine.Animation;
import andrews.jengine.DB;
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
        loadInternalAssets();
    }

    private void loadInternalAssets() {
        {
            final Animation a = new Animation("new_game_splash_screen_anm");
            a.isPlaying = false;
            a.addStep(getSprite("stop_22.png"));
            a.addStep(getSprite("video.png"));
            a.addStep(getSprite("video_2.png"));
            a.addStep(getSprite("video_3.png"));
            a.addStep(getSprite("video_4.png"));
            a.addStep(getSprite("video_5.png"));
            a.addStep(getSprite("video_6.png"));
            a.addStep(getSprite("video_7.png"));
            a.addStep(getSprite("video_8.png"));
            a.addStep(getSprite("video_9.png"));
            a.addStep(getSprite("video_10.png"));
            a.runAfterEnd = new Runnable() {
                @Override
                public void run() {
                    Game.instance.currentRoom = "map1_room";
                    DB.db.rooms.get(Game.instance.currentRoom).goTo();
                }
            };
            animations.internal.put(a.name, a);
        }
    }

    private final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private final MediaTracker tracker;
    private final Map<String, Integer> imgMap = new HashMap<>();

    public Sprite getSprite(String path) {
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

        return sprite;
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
