package andrews.jengine;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Room implements Iterable<String> {
    public String background = "";
    public List<String> objectsIDs = new ArrayList<>();
    public String name = "";

    public Room() {

    }

    @JsonIgnore
    public Room(String name) {
        this.name = name;
    }

    @Override
    @JsonIgnore
    public Iterator<String> iterator() {
        return objectsIDs.listIterator();
    }

    @JsonIgnore
    public void update() {
    }

    @JsonIgnore
    public void goTo() {

    }
}
