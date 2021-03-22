package nl.t64.game.rpg.screens.inventory.tooltip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.PersonalityItem;
import nl.t64.game.rpg.components.party.SkillItemId;
import nl.t64.game.rpg.screens.inventory.InventoryUtils;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;

import java.util.function.Supplier;


public class PersonalityTooltip extends BaseTooltip {

    private static final float DELAY = 0.5f;

    private float x;
    private Supplier<Float> y;

    @Override
    public void toggle(ItemSlot notUsedHere) {
        boolean isEnabled = Utils.getGameData().isTooltipEnabled();
        Utils.getGameData().setTooltipEnabled(!isEnabled);
        window.setVisible(!isEnabled);
    }

    public void setPosition(float x, Supplier<Float> y) {
        this.x = x;
        this.y = y;
    }

    public void refresh(Label label, PersonalityItem personalityItem) {
        hide();
        setupTooltip(label, personalityItem);
        if (Utils.getGameData().isTooltipEnabled()) {
            window.addAction(Actions.sequence(Actions.delay(DELAY),
                                              Actions.show()));
        }
    }

    private void setupTooltip(Label label, PersonalityItem personalityItem) {
        var localCoords = new Vector2(label.getX(), label.getY());
        label.localToStageCoordinates(localCoords);
        updateDescription(personalityItem);
        window.setPosition(localCoords.x + x, localCoords.y + y.get());
        window.toFront();
    }

    private void updateDescription(PersonalityItem personalityItem) {
        window.clear();

        final int totalLoremaster = InventoryUtils.getSelectedHero().getCalculatedTotalSkillOf(SkillItemId.LOREMASTER);
        final String description = personalityItem.getDescription(totalLoremaster);
        final var labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        final var label = new Label(description, labelStyle);
        window.add(label);

        window.pack();
    }

}
