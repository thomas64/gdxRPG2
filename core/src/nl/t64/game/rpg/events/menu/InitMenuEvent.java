package nl.t64.game.rpg.events.menu;

import com.badlogic.gdx.scenes.scene2d.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class InitMenuEvent implements Event {
    @Getter
    private final Stage stage;
    @Getter
    private final int selectedIndex;
}
