package nl.t64.game.rpg.components.event;

import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class EventContainer {

    private static final String EVENT_CONFIGS = "configs/events/";
    private static final String FILE_LIST = EVENT_CONFIGS + "_files.txt";

    private final Map<String, Event> events;

    public EventContainer() {
        this.events = new HashMap<>();
        this.loadEvents();
    }

    public Event getEventById(String eventId) {
        return events.get(eventId);
    }

    private void loadEvents() {
        String[] configFiles = Gdx.files.local(FILE_LIST).readString().split(System.lineSeparator());
        Arrays.stream(configFiles)
              .map(filePath -> Gdx.files.local(EVENT_CONFIGS + filePath).readString())
              .map(json -> Utils.readValue(json, Event.class))
              .forEach(events::putAll);
    }

}
