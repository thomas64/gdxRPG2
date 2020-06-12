package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.Consumer;


class ListenerKeyInputField extends InputListener {

    private static final String VALID_CHARACTERS = "^[0-9a-zA-Z]*$";

    private final Consumer<StringBuilder> updateInputFunction;
    private final int maxSizeOfInput;

    private StringBuilder inputField;

    ListenerKeyInputField(Consumer<StringBuilder> updateInputFunction, int maxSizeOfInput) {
        this.updateInputFunction = updateInputFunction;
        this.maxSizeOfInput = maxSizeOfInput;
    }

    void updateInputField(StringBuilder newInputField) {
        inputField = newInputField;
    }

    @Override
    public boolean keyTyped(InputEvent event, char character) {
        final String inputCharacter = String.valueOf(character);
        if (inputCharacter.matches(VALID_CHARACTERS)
                && inputField.length() < maxSizeOfInput) {
            inputField.insert(inputField.length() - 1, inputCharacter);
            updateInputFunction.accept(inputField);
        }
        return true;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (keycode == Input.Keys.BACKSPACE
                && inputField.length() - 1 > 0) {
            inputField.deleteCharAt(inputField.length() - 2);
            updateInputFunction.accept(inputField);
        }
        return true;
    }

}
