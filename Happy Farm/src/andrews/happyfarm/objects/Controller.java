package andrews.happyfarm.objects;

import andrews.jengine.Animation;
import andrews.jengine.Game;
import andrews.jengine.GameObject;
import andrews.jengine.modules.Resources;
import andrews.jengine.modules.SaveGame;

import java.awt.event.KeyEvent;

import static andrews.jengine.DB.db;

public class Controller extends GameObject {
    public Controller(String name) {
        super(name);
        visible = false;
    }

    public Controller() {
        super();
    }

    @Override
    public void onKey(int keycode) {
        super.onKey(keycode);
        if (keycode == KeyEvent.VK_1) {
            if (Game.instance.currentRoom.equals("game_room")) {
                Game.instance.currentRoom = "new_game_splash_screen_room";
                db.rooms.get(Game.instance.currentRoom).goTo();
                final Animation acur = Resources.animation(
                        db.backgrounds.get(db.rooms.get(Game.instance.currentRoom).background).sprite);
                acur.reset();
                acur.isPlaying = true;
            }
        }
        if (keycode == KeyEvent.VK_2) {
            System.exit(0);
        }
        if (keycode == KeyEvent.VK_3) {
            final SaveGame.SaveGameItem item = new SaveGame.SaveGameItem();
            item.path = "assets/test.sav";
            item.name = "test.sav";
            Game.instance.saveGame.saveGame(item);
            Game.instance.saveGame.saves.put(item.name, item);
        }
        if (keycode == KeyEvent.VK_4) {
            final SaveGame.SaveGameItem item = new SaveGame.SaveGameItem();
            item.path = "assets/test.sav";
            item.name = "test.sav";
            Game.instance.saveGame.loadGame(item);
        }
    }
}