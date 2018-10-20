package nl.t64.game.rpg.components.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.entities.Menu;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.menu.*;

import java.util.ArrayList;
import java.util.List;


public class MainMenuPhysicsComponent extends PhysicsComponent {

    private static final int NUMBER_OF_ITEMS = 4;

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
        if (event instanceof PressUpEvent) {
            setIndexDown();
        }
        if (event instanceof PressDownEvent) {
            setIndexUp();
        }
        if (event instanceof PressEnterEvent) {
            isSelected = true;
        }
        if (event instanceof MouseMoveEvent) {
            mouseSelectCoordinates = ((MouseMoveEvent) event).getMouseCoordinates();
        }
        if (event instanceof MouseClickEvent) {
            setIsSelected();
        }
    }

    private void setIndexDown() {
        if (selectedIndex <= 1) {
            selectedIndex = 1;
        } else {
            selectedIndex -= 1;
        }
    }

    private void setIndexUp() {
        if (selectedIndex >= NUMBER_OF_ITEMS) {
            selectedIndex = NUMBER_OF_ITEMS;
        } else {
            selectedIndex += 1;
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
    }

    @Override
    public void update(Menu mainMenu, GdxRpg2 game) {
        updateSelectedIndexWithMouse();
        mainMenu.send(new UpdateIndexEvent(selectedIndex));
        if (isSelected) {
            selectMenuItem(game);
            isSelected = false;
        }
    }

    private void updateSelectedIndexWithMouse() {
        int i = 1;
        for (Actor menuItem : getMenuItems()) {
            if (collidesWithMouse(menuItem)) {
                selectedIndex = i;
            }
            i++;
        }
    }

    private void selectMenuItem(GdxRpg2 game) {
        switch (selectedIndex) {
            case 1:
                game.setScreen(game.getScreenType(ScreenType.NEW_GAME));
                break;
            case 2:
                game.setScreen(game.getScreenType(ScreenType.LOAD_GAME));
                break;
            case 3:
                game.setScreen(game.getScreenType(ScreenType.SETTINGS));
                break;
            case 4:
                Gdx.app.exit();
                break;
            default:
        }
    }

    private List<Actor> getMenuItems() {
        List<Actor> actors = new ArrayList<>();
        for (Actor actor : ((Table) stage.getActors().get(0)).getChildren()) {
            if (actor instanceof TextButton) {
                actors.add(actor);
            }
        }
        return actors;
    }

    private boolean collidesWithMouse(Actor actor) {
        float x = mouseSelectCoordinates.x;
        float y = Gdx.graphics.getHeight() - mouseSelectCoordinates.y;
        return x > actor.getX() && x < actor.getX() + actor.getWidth() &&
                y > actor.getY() && y < actor.getY() + actor.getHeight();
    }

}