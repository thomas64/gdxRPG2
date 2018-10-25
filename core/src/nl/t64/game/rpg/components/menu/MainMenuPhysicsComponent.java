package nl.t64.game.rpg.components.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.constants.KeyDown;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.entities.Menu;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.menu.*;

import java.util.ArrayList;
import java.util.List;


public class MainMenuPhysicsComponent extends PhysicsComponent {

    private static final int NUMBER_OF_ITEMS = 4;
    private static final int EXIT_INDEX = 3;

    private Stage stage;

    private int selectedIndex;
    private Vector3 mouseSelectCoordinates;
    private boolean isSelected = false;

    public MainMenuPhysicsComponent() {
        this.mouseSelectCoordinates = new Vector3(0, 0, 0);
    }

    @Override
    public void receive(Event event) {
        if (event instanceof InitMenuEvent) {
            selectedIndex = ((InitMenuEvent) event).getSelectedIndex();
            stage = ((InitMenuEvent) event).getStage();
        }
        if (event instanceof KeyDownEvent) {
            handleKeyDownEvent(((KeyDownEvent) event).getKeyDown());
        }
        if (event instanceof MouseMoveEvent) {
            mouseSelectCoordinates = ((MouseMoveEvent) event).getMouseCoordinates();
            updateSelectedIndexWithMouse();
        }
        if (event instanceof MouseClickEvent) {
            setIsSelected();
        }
    }

    private void handleKeyDownEvent(KeyDown keyDown) {
        switch (keyDown) {
            case UP:
                setIndexDown();
                break;
            case DOWN:
                setIndexUp();
                break;
            case ENTER:
                isSelected = true;
                break;
            case ESCAPE:
                selectedIndex = EXIT_INDEX;
                isSelected = true;
                break;
            default:
                break;
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
    public void update(Menu mainMenu, GdxRpg2 game) {
        mainMenu.send(new UpdateIndexEvent(selectedIndex));
        if (isSelected) {
            selectMenuItem(game);
            isSelected = false;
        }
    }

    private void selectMenuItem(GdxRpg2 game) {
        switch (selectedIndex) {
            case 0:
                game.setScreen(game.getScreenType(ScreenType.NEW_GAME_MENU));
                break;
            case 1:
                game.setScreen(game.getScreenType(ScreenType.LOAD_GAME_MENU));
                break;
            case 2:
                game.setScreen(game.getScreenType(ScreenType.SETTINGS_MENU));
                break;
            case 3:
                Gdx.app.exit();
                break;
            default:
                throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private List<Actor> getMenuItems() {
        List<Actor> actors = new ArrayList<>();
        Table table = (Table) stage.getActors().get(0); // the only actor(=table) inside this stage.
        for (Actor actor : table.getChildren()) {
            if (actor instanceof TextButton) {
                actors.add(actor);
            }
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
