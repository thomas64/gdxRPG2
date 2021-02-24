package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.quest.QuestGraph;

import java.util.Optional;
import java.util.function.Consumer;


@AllArgsConstructor
class QuestListListener extends InputListener {

    private final List<QuestGraph> questList;
    private final Consumer<QuestGraph> populateQuestSpecifics;

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        return switch (keycode) {
            case Input.Keys.UP,
                    Input.Keys.DOWN,
                    Input.Keys.HOME,
                    Input.Keys.END -> isSelected();
            case Input.Keys.LEFT -> setSelectedUp();
            case Input.Keys.RIGHT -> setSelectedDown();
            default -> false;
        };
    }

    private boolean setSelectedUp() {
        questList.setSelectedIndex(Math.max(questList.getSelectedIndex() - 10, 0));
        return isSelected();
    }

    private boolean setSelectedDown() {
        questList.setSelectedIndex(Math.min(questList.getSelectedIndex() + 10, questList.getItems().size - 1));
        return isSelected();
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
