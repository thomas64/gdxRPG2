package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.components.tooltip.LootSlotTooltip;


class LootUI {

    private static final float WINDOW_PADDING_BOTTOM = 10f;
    private static final float LABEL_PADDING_TOP = 15f;

    private final Window lootWindow;
    private final LootSlotsTable lootSlotsContainer;
    private final LootSlotTooltip tooltip;
    private final Label buttonLabel;

    LootUI(LootScreen lootScreen, Loot loot, String title) {
        this.tooltip = new LootSlotTooltip();
        this.lootSlotsContainer = new LootSlotsTable(lootScreen, loot, this.tooltip);
        this.lootSlotsContainer.lootSlots.setBackground(Utils.createTopBorder());

        this.lootWindow = Utils.createDefaultWindow(title, this.lootSlotsContainer.lootSlots);
        this.lootWindow.setPosition((Gdx.graphics.getWidth() / 2f) - (this.lootWindow.getWidth() / 2f),
                                    (Gdx.graphics.getHeight() / 2f) - (this.lootWindow.getHeight() / 2f)
                                    + WINDOW_PADDING_BOTTOM);

        this.buttonLabel = new Label(this.createText(), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        this.buttonLabel.setPosition((Gdx.graphics.getWidth() / 2f) - (this.buttonLabel.getWidth() / 2f),
                                     (Gdx.graphics.getHeight() / 2f) - (this.lootWindow.getHeight() / 2f)
                                     - LABEL_PADDING_TOP);
    }

    void show(Stage stage) {
        stage.addActor(lootWindow);
        tooltip.addToStage(stage);
        stage.addActor(buttonLabel);

        stage.setKeyboardFocus(lootSlotsContainer.lootSlots);
    }

    private String createText() {
        if (Utils.isGamepadConnected()) {
            return "[A] Take      [Select] Tooltip      [B] Back";
        } else {
            return "[A] Take      [T] Tooltip      [Esc] Back";
        }
    }

}
