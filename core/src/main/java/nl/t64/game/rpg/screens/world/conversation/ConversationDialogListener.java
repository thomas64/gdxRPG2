package nl.t64.game.rpg.screens.world.conversation;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import nl.t64.game.rpg.conversation.ConversationChoice;


class ConversationDialogListener extends InputListener {

    private final List<ConversationChoice> answers;
    private final Runnable selectAnswers;

    ConversationDialogListener(List<ConversationChoice> answers, Runnable selectAnser) {
        this.answers = answers;
        this.selectAnswers = selectAnser;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.UP -> inputUp();
            case Input.Keys.DOWN -> inputDown();
            case Input.Keys.ENTER -> inputEnter();
        }
        return true;
    }

    private void inputUp() {
        if (answers.getSelectedIndex() > 0) {
            answers.setSelectedIndex(answers.getSelectedIndex() - 1);
        }
    }

    private void inputDown() {
        if (answers.getSelectedIndex() < answers.getItems().size - 1)
            answers.setSelectedIndex(answers.getSelectedIndex() + 1);
    }

    private void inputEnter() {
        selectAnswers.run();
    }

}
