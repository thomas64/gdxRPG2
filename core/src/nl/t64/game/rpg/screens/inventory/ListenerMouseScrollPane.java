package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class ListenerMouseScrollPane extends ClickListener {

    private final Stage stage;
    private final ScrollPane scrollPane;

    public ListenerMouseScrollPane(Stage stage, ScrollPane scrollPane) {
        this.stage = stage;
        this.scrollPane = scrollPane;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        stage.setScrollFocus(scrollPane);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        stage.setScrollFocus(null);
    }

}
