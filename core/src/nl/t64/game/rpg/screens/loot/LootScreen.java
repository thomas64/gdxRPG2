package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.screens.ScreenToLoad;


abstract class LootScreen extends ScreenToLoad {

    public final LootSubject lootSubject;
    Loot loot;
    String lootTitle;

    LootScreen() {
        this.lootSubject = new LootSubject();
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
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
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

    abstract void resolveAfterClearingContent();

}
