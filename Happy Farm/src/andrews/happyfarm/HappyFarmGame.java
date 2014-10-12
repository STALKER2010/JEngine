package andrews.happyfarm;

import andrews.happyfarm.background.GameBackground;
import andrews.happyfarm.objects.Controller;
import andrews.happyfarm.objects.Player;
import andrews.happyfarm.rooms.GameRoom;
import andrews.jengine.DB;
import andrews.jengine.Game;

/**
 * @author STALKER_2010
 */
public class HappyFarmGame extends Game {
    public static void main(String[] args) {
        launch(new HappyFarmGame());
        NAME = "Happy Farm";
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void init() {
        super.init();
        {
            final GameBackground b = new GameBackground("game_bg");
            DB.db.backgrounds.put(b.name, b);
        }
        {
            final Controller r = new Controller("controller");
            DB.db.objects.put(r.name, r);
        }
        {
            final Player r = new Player("player");
            DB.db.objects.put(r.name, r);
        }
        {
            final GameRoom r = new GameRoom("game_room");
            DB.db.rooms.put(r.name, r);
        }
        currentRoom = "game_room";
    }
}
