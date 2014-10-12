package andrews.jengine.renders;

import andrews.jengine.Game;
import andrews.jengine.GameObject;
import andrews.jengine.Room;
import andrews.jengine.modules.Resources;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;

import static andrews.jengine.DB.db;

/**
 * @author STALKER_2010
 */
public class BasicRender {
    private final Game game;

    public BasicRender(Game game) {
        this.game = game;
    }

    public BufferStrategy bss;
    public Graphics gl;
    public boolean isInitialized = false;

    public boolean init() {
        bss = game.getBufferStrategy();
        if (bss == null) {
            game.createBufferStrategy(2);
            game.requestFocus();
            bss = game.getBufferStrategy();
        }
        if (bss == null) {
            System.err.println("BasicRender: Can't create buffer strategy");
            return false;
        }
        isInitialized = true;
        return true;
    }

    public void render(final Room room) {
        if (!isInitialized) {
            System.err.println("BasicRender: Render not initialized");
            return;
        }
        gl = bss.getDrawGraphics();
        gl.setColor(Color.black);
        gl.fillRect(0, 0, game.getWidth(), game.getHeight());
        {
            Resources.sprite(Resources.animation(db.backgrounds.get(room.background).sprite).getStep().sprite)
                    .draw(gl, 0, 0);
            java.util.List<GameObject> objs = new ArrayList<>(db.objects.values());
            Collections.sort(objs, GameObject.compareByDepth);
            for (GameObject o : objs) {
                if (room.objectsIDs.contains(o.name)) {
                    o.render(gl);
                }
            }
        }
        bss.show();
        gl.dispose();
    }
}
