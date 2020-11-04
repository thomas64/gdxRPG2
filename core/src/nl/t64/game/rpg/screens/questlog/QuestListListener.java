package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.quest.QuestGraph;

import java.util.Optional;
import java.util.function.Consumer;


class QuestListListener extends ClickListener {

    private final List<QuestGraph> questList;
    private final Consumer<QuestGraph> populateQuestSpecifics;

    QuestListListener(List<QuestGraph> questList, Consumer<QuestGraph> populateQuestSpecifics) {
        this.questList = questList;
        this.populateQuestSpecifics = populateQuestSpecifics;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return isSelected();
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        return switch (keycode) {
            case Input.Keys.UP,
                    Input.Keys.DOWN,
                    Input.Keys.HOME,
                    Input.Keys.END -> isSelected();
            default -> false;
        };
    }

    private boolean isSelected() {
        return Optional.ofNullable(questList.getSelected())
                       .map(this::isSelected)
                       .orElse(false);
    }

    private boolean isSelected(QuestGraph quest) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        populateQuestSpecifics.accept(quest);
        return true;
    }

}
