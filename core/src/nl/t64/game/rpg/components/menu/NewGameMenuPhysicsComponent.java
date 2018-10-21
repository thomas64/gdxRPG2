package nl.t64.game.rpg.components.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.entities.Menu;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.menu.*;

import java.util.ArrayList;
import java.util.List;


public class NewGameMenuPhysicsComponent extends PhysicsComponent {

    private static final int NUMBER_OF_ITEMS = 2;
    private static final int EXIT_INDEX = 1;

    private Stage stage;

    private StringBuilder profileName = new StringBuilder("_");

    private int selectedIndex;
    private Vector3 mouseSelectCoordinates;
    private boolean isSelected = false;

    public NewGameMenuPhysicsComponent() {
        this.mouseSelectCoordinates = new Vector3(0, 0, 0);
    }

    @Override
    public void receive(Event event) {
        if (event instanceof InitMenuEvent) {
            selectedIndex = ((InitMenuEvent) event).getSelectedIndex();
            stage = ((InitMenuEvent) event).getStage();
        }
        if (event instanceof PressCharEvent) {
            if (profileName.length() <= 8) {
                profileName.insert(profileName.length() - 1, (((PressCharEvent) event).getCharacter()));
            }
        }
        if (event instanceof PressBackspaceEvent) {
            if (profileName.length() - 1 > 0) {
                profileName.deleteCharAt(profileName.length() - 2);
            }
        }
        if (event instanceof PressUpEvent) {
            setIndexDown();
        }
        if (event instanceof PressDownEvent) {
            setIndexUp();
        }
        if (event instanceof PressEnterEvent) {
            isSelected = true;
        }
        if (event instanceof PressEscapeEvent) {
            selectedIndex = EXIT_INDEX;
            isSelected = true;
        }
        if (event instanceof MouseMoveEvent) {
            mouseSelectCoordinates = ((MouseMoveEvent) event).getMouseCoordinates();
            updateSelectedIndexWithMouse();
        }
        if (event instanceof MouseClickEvent) {
            setIsSelected();
        }
    }

    private void setIndexDown() {
        if (selectedIndex <= 0) {
            selectedIndex = 0;
        } else {
            selectedIndex -= 1;
        }
    }

    private void setIndexUp() {
        if (selectedIndex >= NUMBER_OF_ITEMS - 1) {
            selectedIndex = NUMBER_OF_ITEMS - 1;
        } else {
            selectedIndex += 1;
        }
    }

    private void updateSelectedIndexWithMouse() {
        int i = 0;
        for (Actor menuItem : getMenuItems()) {
            if (collidesWithMouse(menuItem)) {
                selectedIndex = i;
            }
            i++;
        }
    }

    private void setIsSelected() {
        if (getMenuItems().stream()
                          .anyMatch(this::collidesWithMouse)) {
            isSelected = true;
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Menu newGameMenu, GdxRpg2 game) {
        newGameMenu.send(new UpdateProfileNameEvent(profileName.toString()));
        newGameMenu.send(new UpdateIndexEvent(selectedIndex));
        if (isSelected) {
            selectMenuItem(game);
            isSelected = false;
        }
    }

    private void selectMenuItem(GdxRpg2 game) {
        switch (selectedIndex) {
            case 0:

                break;
            case 1:
                game.setScreen(game.getScreenType(ScreenType.MAIN_MENU));
                break;
            default:
                throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private List<Actor> getMenuItems() {
        List<Actor> actors = new ArrayList<>();
        Table outerTable = (Table) stage.getActors().get(0); // the only actor(=table) inside this stage.
        Table lowerTable = (Table) outerTable.getChildren().get(1); // two tables inside the table, get the second.
        for (Actor actor : lowerTable.getChildren()) {
            actors.add(actor);
        }
        return actors;
    }

    private boolean collidesWithMouse(Actor actor) {
        float x = mouseSelectCoordinates.x;
        float y = Gdx.graphics.getHeight() - mouseSelectCoordinates.y;
        Vector2 actorLocation = actor.localToStageCoordinates(new Vector2(0, 0));
        return x > actorLocation.x && x < actorLocation.x + actor.getWidth() &&
                y > actorLocation.y && y < actorLocation.y + actor.getHeight();
    }

}
