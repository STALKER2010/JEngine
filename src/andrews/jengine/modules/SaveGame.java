package andrews.jengine.modules;

import andrews.jengine.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk7.Jdk7Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author STALKER_2010
 */
public class SaveGame {
    public final ObjectMapper mapper;

    public SaveGame() {
        mapper = new ObjectMapper();
        mapper.registerModule(new Jdk7Module());
        mapper.registerModule(new AfterburnerModule());
        mapper.registerModule(new MrBeanModule());
        mapper.registerModule(new ParanamerModule());
        //mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        mapper.enable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
        mapper.enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        mapper.enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        mapper.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        mapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Map<String, SaveGameItem> saves = new HashMap<>();
    public String SAVE_DIR = "assets/";

    public void listSaves() {
        final File save_dir = new File(SAVE_DIR);
        if (save_dir.exists() && save_dir.canRead() && save_dir.canExecute()) {
            File[] fList = save_dir.listFiles();
            if (fList != null) {
                for (File f : fList) {
                    if (f.canRead()) {
                        if (f.getName().endsWith(".sav")) {
                            final SaveGameItem save = new SaveGameItem();
                            save.name = f.getName();
                            save.path = f.getAbsolutePath();
                            saves.put(save.name, save);
                        }
                    }
                }
            }
        } else {
            System.err.println("SaveGame: Access to dir with saves is denied!");
        }
    }

    public void loadGame(SaveGameItem save) {
        Game.instance.unstableState = true;
        try {
            save = mapper.readValue(new File(save.path), SaveGameItem.class);
            DB.db.rooms.clear();
            DB.db.objects.clear();
            DB.db.backgrounds.clear();
            for (Map.Entry<String, SaveGameItem.BackgroundData> rd : save.backgrounds.entrySet()) {
                Class<?> cgv = Class.forName(rd.getValue().className);
                if (cgv != null) {
                    Class<? extends Background> cv = cgv.asSubclass(Background.class);
                    Background v = cv.newInstance();
                    v.name = rd.getValue().name;
                    v.sprite = rd.getValue().sprite;
                    v.visible = rd.getValue().visible;
                    for (Map.Entry<String, Object> adds : rd.getValue().additionalData.entrySet()) {
                        try {
                            Field f = cv.getField(adds.getKey());
                            f.setAccessible(true);
                            f.set(v, adds.getValue());
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace(System.err);
                        }
                    }
                    DB.db.backgrounds.put(rd.getKey(), v);
                }
            }
            for (Map.Entry<String, SaveGameItem.RoomData> rd : save.rooms.entrySet()) {
                Class<?> cgv = Class.forName(rd.getValue().className);
                if (cgv != null) {
                    Class<? extends Room> cv = cgv.asSubclass(Room.class);
                    Room v = cv.newInstance();
                    v.name = rd.getValue().name;
                    v.objectsIDs = rd.getValue().objectsIDs;
                    v.background = rd.getValue().background;
                    for (Map.Entry<String, Object> adds : rd.getValue().additionalData.entrySet()) {
                        try {
                            Field f = cv.getField(adds.getKey());
                            f.setAccessible(true);
                            f.set(v, adds.getValue());
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace(System.err);
                        }
                    }
                    DB.db.rooms.put(rd.getKey(), v);
                }
            }
            for (Map.Entry<String, SaveGameItem.ObjectData> rd : save.objects.entrySet()) {
                Class<?> cgv = Class.forName(rd.getValue().className);
                if (cgv != null) {
                    Class<? extends GameObject> cv = cgv.asSubclass(GameObject.class);
                    GameObject v = cv.newInstance();
                    v.name = rd.getValue().name;
                    v.sprite = rd.getValue().sprite;
                    v.depth = rd.getValue().depth;
                    v.visible = rd.getValue().visible;
                    v.redraw = rd.getValue().redraw;
                    v.x = rd.getValue().x;
                    v.y = rd.getValue().y;
                    for (Map.Entry<String, Object> adds : rd.getValue().additionalData.entrySet()) {
                        try {
                            Field f = cv.getField(adds.getKey());
                            f.setAccessible(true);
                            f.set(v, adds.getValue());
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace(System.err);
                        }
                    }
                    DB.db.objects.put(rd.getKey(), v);
                }
            }
            Game.instance.currentRoom = save.currentRoom;
            Game.instance.resources.animations.generated = save.generatedAnimations;
            System.gc();
        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace(System.err);
        }
        Game.instance.unstableState = false;
    }

    public void saveGame(final SaveGameItem save) {
        save.generatedAnimations = Game.instance.resources.animations.generated;
        save.currentRoom = Game.instance.currentRoom;
        for (Map.Entry<String, Room> rd : DB.db.rooms.entrySet()) {
            SaveGameItem.RoomData v = new SaveGameItem.RoomData();
            Class<? extends Room> cv = rd.getValue().getClass();
            v.className = cv.getName();
            v.name = rd.getValue().name;
            v.objectsIDs = rd.getValue().objectsIDs;
            v.background = rd.getValue().background;
            final List<String> names = Arrays.asList("name", "objectsIDs", "background");
            for (Field f : cv.getFields()) {
                if (!(names.contains(f.getName()))) {
                    try {
                        f.setAccessible(true);
                        v.additionalData.put(f.getName(), f.get(rd.getValue()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace(System.err);
                    }
                }
            }
            save.rooms.put(rd.getKey(), v);
        }
        for (Map.Entry<String, GameObject> rd : DB.db.objects.entrySet()) {
            SaveGameItem.ObjectData v = new SaveGameItem.ObjectData();
            Class<? extends GameObject> cv = rd.getValue().getClass();
            v.className = cv.getName();
            v.name = rd.getValue().name;
            v.sprite = rd.getValue().sprite;
            v.depth = rd.getValue().depth;
            v.visible = rd.getValue().visible;
            v.redraw = rd.getValue().redraw;
            v.x = rd.getValue().x;
            v.y = rd.getValue().y;
            final List<String> names = Arrays.asList("name", "sprite", "depth", "visible", "x", "y", "compareByDepth");
            for (Field f : cv.getFields()) {
                if (!(names.contains(f.getName()))) {
                    try {
                        f.setAccessible(true);
                        v.additionalData.put(f.getName(), f.get(rd.getValue()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace(System.err);
                    }
                }
            }
            save.objects.put(rd.getKey(), v);
        }
        for (Map.Entry<String, Background> rd : DB.db.backgrounds.entrySet()) {
            SaveGameItem.BackgroundData v = new SaveGameItem.BackgroundData();
            Class<? extends Background> cv = rd.getValue().getClass();
            v.className = cv.getName();
            v.name = rd.getValue().name;
            v.sprite = rd.getValue().sprite;
            v.visible = rd.getValue().visible;
            final List<String> names = Arrays.asList("name", "sprite", "visible");
            for (Field f : cv.getFields()) {
                if (!(names.contains(f.getName()))) {
                    try {
                        f.setAccessible(true);
                        v.additionalData.put(f.getName(), f.get(rd.getValue()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace(System.err);
                    }
                }
            }
            save.backgrounds.put(rd.getKey(), v);
        }
        try {
            mapper.writeValue(new File(save.path), save);
            System.gc();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public static class SaveGameItem {
        @JsonIgnore
        public String path = "";
        public String name = "";
        public Map<String, ObjectData> objects = new HashMap<>();
        public Map<String, BackgroundData> backgrounds = new HashMap<>();
        public Map<String, RoomData> rooms = new HashMap<>();
        public String currentRoom = "";
        public Map<String, Animation> generatedAnimations = null;

        public static class ObjectData {
            public String className = "andrews.jengine.GameObject";
            public String sprite = "";
            public String name = "";
            public int depth = 0;
            public boolean visible = true;
            public double x = 0;
            public double y = 0;
            public boolean redraw = true;
            public Map<String, Object> additionalData = new HashMap<>();
        }

        public static class BackgroundData {
            public String className = "andrews.jengine.Background";
            public String sprite = "";
            public String name = "";
            public boolean visible = true;
            public Map<String, Object> additionalData = new HashMap<>();
        }

        public static class RoomData {
            public String className = "andrews.jengine.Room";
            public String background = "";
            public List<String> objectsIDs = new ArrayList<>();
            public String name = "";
            public Map<String, Object> additionalData = new HashMap<>();
        }
    }
}
