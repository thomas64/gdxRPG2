package nl.t64.game.rpg.screens.world.conversation;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.conversation.ConversationChoice;


class ConversationDialogListener extends InputListener {

    private final List<ConversationChoice> answers;
    private final Runnable selectAnswer;

    ConversationDialogListener(List<ConversationChoice> answers, Runnable selectAnswer) {
        this.answers = answers;
        this.selectAnswer = selectAnswer;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.UP -> inputUp();
            case Input.Keys.DOWN -> inputDown();
            case Input.Keys.ENTER,
                    Input.Keys.A -> inputEnter();
        }
        return true;
    }

    private void inputUp() {
        if (answers.getItems().size > 1 && answers.getSelectedIndex() > 0) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_CURSOR);
            answers.setSelectedIndex(answers.getSelectedIndex() - 1);
        }
        if (answers.getSelectedIndex() == -1) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_CURSOR);
            answers.setSelectedIndex(answers.getItems().size - 1);
        }
    }

    private void inputDown() {
        if (answers.getItems().size > 1 && answers.getSelectedIndex() < answers.getItems().size - 1) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_CURSOR);
            answers.setSelectedIndex(answers.getSelectedIndex() + 1);
        }
    }

    private void inputEnter() {
        if (answers.getSelectedIndex() != -1) {
            selectAnswer.run();
        }
    }

}
