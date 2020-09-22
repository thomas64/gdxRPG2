package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import nl.t64.game.rpg.components.quest.QuestGraph;

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
        return isSelected();
    }

    private boolean isSelected() {
        QuestGraph quest = questList.getSelected();
        if (quest == null) return false;
        populateQuestSpecifics.accept(quest);
        return true;
    }

}
