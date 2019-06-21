package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.constants.InventoryAttribute;

import static nl.t64.game.rpg.components.party.InventoryGroup.SHIELD;
import static nl.t64.game.rpg.components.party.InventoryGroup.WEAPON;
import static nl.t64.game.rpg.constants.InventoryAttribute.*;


class StatsTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final String TEXT_FONT = "fonts/spectral.ttf";
    private static final int TEXT_SIZE = 20;
    private static final float LINE_HEIGHT = 26f;
    private static final float FIRST_COLUMN_WIDTH = 200f;
    private static final float SECOND_COLUMN_WIDTH = 50f;
    private static final float THIRD_COLUMN_WIDTH = 50f;
    private static final float FOURTH_COLUMN_WIDTH = 50f;
    private static final float PADDING = 20f;

    final Table stats;
    private final BitmapFont font;

    StatsTable() {
        this.font = Utils.getResourceManager().getTrueTypeAsset(TEXT_FONT, TEXT_SIZE);
        this.stats = new Table(createSkin());
        this.stats.defaults().height(LINE_HEIGHT).align(Align.left);
        this.stats.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.stats.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.stats.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        this.stats.columnDefaults(3).width(FOURTH_COLUMN_WIDTH);
        this.stats.pad(PADDING);
        setTopBorder();
    }

    private Skin createSkin() {
        Skin statsSkin = new Skin();
        statsSkin.add("default", new Label.LabelStyle(font, Color.BLACK));
        return statsSkin;
    }

    private void setTopBorder() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        stats.setBackground(drawable);
    }

    void render() {
        HeroItem selectedHero = DynamicVars.selectedHero;
        stats.clear();

        stats.add("Intelligence");
        stats.add(String.valueOf(selectedHero.getOwnIntelligence()));
        stats.add("");
        stats.add("").row();
        stats.add("Willpower");
        stats.add("?");
        stats.add("");
        stats.add("").row();
        stats.add("Dexterity");
        stats.add("?");
        stats.add("");
        stats.add("").row();
        stats.add("Agility");
        stats.add("?");
        stats.add("");
        stats.add("").row();
        stats.add("Endurance");
        stats.add(String.valueOf(selectedHero.getOwnEndurance()));
        stats.add("");
        stats.add("").row();
        stats.add("Strength");
        stats.add(String.valueOf(selectedHero.getOwnStrength()));
        stats.add("");
        stats.add("").row();
        stats.add("Stamina");
        stats.add(String.valueOf(selectedHero.getOwnStamina()));
        stats.add("");
        stats.add("").row();
        stats.add("").row();

        stats.add("XP Remaining");
        stats.add(String.valueOf(selectedHero.getXpRemaining()));
        stats.add("");
        stats.add("").row();
        stats.add("Total XP");
        stats.add(String.valueOf(selectedHero.getTotalXp()));
        stats.add("");
        stats.add("").row();
        stats.add("Next Level");
        stats.add(String.valueOf(selectedHero.getNeededXpForNextLevel())); // todo, is dit de juiste van de 2 methodes?
        stats.add("");
        stats.add("").row();
        stats.add("").row();

        stats.add("Weight");
        stats.add("?");
        stats.add("");
        stats.add("").row();
        stats.add("Movepoints");
        stats.add("?");
        stats.add("");
        stats.add("").row();
        stats.add("Base Hit");
        stats.add(String.valueOf(selectedHero.getAttributeValueOf(WEAPON, BASE_HIT) + "%"));
        stats.add("");
        createPreview(BASE_HIT, selectedHero);
        stats.add("Damage");
        stats.add(String.valueOf(selectedHero.getAttributeValueOf(WEAPON, DAMAGE)));
        stats.add("");
        createPreview(DAMAGE, selectedHero);
        stats.add("Protection");
        stats.add("?");
        stats.add("");
        stats.add("").row();
        stats.add("Defense");
        stats.add(String.valueOf(selectedHero.getAttributeValueOf(SHIELD, DEFENSE)));
        stats.add("");
        createPreview(DEFENSE, selectedHero);
    }

    private void createPreview(InventoryAttribute attribute, HeroItem selectedHero) {
        InventoryItem hoveredItem = DynamicVars.hoveredItem;

        if (hoveredItem == null) {
            stats.add("").row();
        } else {
            int equippedValue = selectedHero.getAttributeValueOf(hoveredItem.getGroup(), attribute);
            int hoveredValue = hoveredItem.getAttribute(attribute);
            int difference = hoveredValue - equippedValue;
            if (difference > 0) {
                var label = new Label(String.format("+%s", difference), new Label.LabelStyle(font, Color.LIME));
                stats.add(label).row();
            } else if (difference < 0) {
                var label = new Label(String.valueOf(difference), new Label.LabelStyle(font, Color.SCARLET));
                stats.add(label).row();
            } else {
                stats.add("").row();
            }
        }
    }

}
