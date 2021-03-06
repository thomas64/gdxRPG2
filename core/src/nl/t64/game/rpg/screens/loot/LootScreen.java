package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.ScreenToLoad;


public abstract class LootScreen extends LootSubject implements ScreenToLoad {

    final Stage stage;
    Loot loot;
    String lootTitle;

    public LootScreen() {
        this.stage = new Stage();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Utils.setGamepadInputProcessor(stage);

        var lootUI = new LootUI(this, loot, lootTitle);
        lootUI.show(stage);
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // empty
    }

    @Override
    public void pause() {
        // empty
    }

    @Override
    public void resume() {
        // empty
    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void setBackground(Image screenshot, Image parchment) {
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

    void closeScreen(boolean isAllTheLootCleared) {
        if (isAllTheLootCleared) {
            resolveAfterClearingContent();
        }

        Gdx.input.setInputProcessor(null);
        Utils.setGamepadInputProcessor(null);
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_NEXT);
        fadeParchment();
    }

    private void fadeParchment() {
        var screenshot = (Image) stage.getActors().get(0);
        var parchment = (Image) stage.getActors().get(1);
        stage.clear();
        setBackground(screenshot, parchment);
        parchment.addAction(Actions.sequence(Actions.fadeOut(Constant.FADE_DURATION),
                                             Actions.run(() -> Utils.getScreenManager().setScreen(ScreenType.WORLD))));
    }

    abstract void resolveAfterClearingContent();

}
