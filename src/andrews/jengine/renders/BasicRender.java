package andrews.jengine.renders;

import andrews.jengine.Background;
import andrews.jengine.Game;
import andrews.jengine.GameObject;
import andrews.jengine.Room;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

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
        System.setProperty("sun.java2d.transaccel", "True");
        System.setProperty("sun.java2d.d3d", "True");
        System.setProperty("sun.java2d.ddforcevram", "True");
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
        if (gl == null) {
            gl = bss.getDrawGraphics();
        }
        isInitialized = true;
        return true;
    }

    public final java.util.List<GameObject> objs = new ArrayList<>();

    public void render(final Room room) {
        gl.setColor(Color.black);
        {
            if (room.background != null) {
                final Background b = db.backgrounds.get(room.background);
                if (b != null) {
                    b.draw(gl);
                }
            }
            for (GameObject o : objs) {
                if (room.objectsIDs.contains(o.name)) {
                    if (o.visible) {
                        try {
                            o.render(gl);
                        } catch (Exception e) {
                            if (!(e instanceof ConcurrentModificationException)) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        bss.show();
    }
}
