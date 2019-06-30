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
import nl.t64.game.rpg.components.party.StatType;

import static nl.t64.game.rpg.components.party.InventoryGroup.SHIELD;
import static nl.t64.game.rpg.components.party.InventoryGroup.WEAPON;


class StatsTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final String TEXT_FONT = "fonts/spectral.ttf";
    private static final int TEXT_SIZE = 20;
    private static final float LINE_HEIGHT = 26f;
    private static final float FIRST_COLUMN_WIDTH = 190f;
    private static final float SECOND_COLUMN_WIDTH = 40f;
    private static final float THIRD_COLUMN_WIDTH = 30f;
    private static final float FOURTH_COLUMN_WIDTH = 30f;
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

    void renderStats() {
        HeroItem selectedHero = InventoryUtils.selectedHero;
        stats.clear();

        stats.add(StatType.INTELLIGENCE.getTitle());
        stats.add(String.valueOf(selectedHero.getOwnStatOf(StatType.INTELLIGENCE)));
        stats.add("");
        stats.add("").row();
        stats.add("Willpower");
        stats.add("?");
        stats.add("");
        stats.add("").row();
        stats.add(StatType.DEXTERITY.getTitle());
        stats.add(String.valueOf(selectedHero.getOwnStatOf(StatType.DEXTERITY)));
        createBonusFromInventory(StatType.DEXTERITY, selectedHero);
        createPreview(StatType.DEXTERITY, selectedHero);
        stats.add("Agility");
        stats.add("?");
        stats.add("");
        stats.add("").row();
        stats.add(StatType.ENDURANCE.getTitle());
        stats.add(String.valueOf(selectedHero.getOwnStatOf(StatType.ENDURANCE)));
        stats.add("");
        stats.add("").row();
        stats.add(StatType.STRENGTH.getTitle());
        stats.add(String.valueOf(selectedHero.getOwnStatOf(StatType.STRENGTH)));
        stats.add("");
        stats.add("").row();
        stats.add(StatType.STAMINA.getTitle());
        stats.add(String.valueOf(selectedHero.getOwnStatOf(StatType.STAMINA)));
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
    }

    void renderCalcStats() {
        HeroItem selectedHero = InventoryUtils.selectedHero;
        stats.clear();

        stats.add("Weight");
        stats.add("?");
        stats.add("");
        stats.add("").row();

        stats.add("Movepoints");
        stats.add("?");
        stats.add("");
        stats.add("").row();

        stats.add(StatType.BASE_HIT.getTitle());
        stats.add(String.format("%s%%", selectedHero.getStatValueOf(WEAPON, StatType.BASE_HIT)));
        stats.add("");
        createPreview(StatType.BASE_HIT, selectedHero);

        stats.add(StatType.DAMAGE.getTitle());
        stats.add(String.valueOf(selectedHero.getStatValueOf(WEAPON, StatType.DAMAGE)));
        stats.add("");
        createPreview(StatType.DAMAGE, selectedHero);

        stats.add(String.format("Total %s", StatType.PROTECTION.getTitle()));
        stats.add(String.valueOf(selectedHero.getTotalStatOf(StatType.PROTECTION)));
        stats.add("");
        createPreview(StatType.PROTECTION, selectedHero);

        stats.add(String.format("Shield %s", StatType.PROTECTION.getTitle()));
        stats.add(String.valueOf(selectedHero.getStatValueOf(SHIELD, StatType.PROTECTION)));
        stats.add("");
        createShieldPreview(StatType.PROTECTION, selectedHero);

        stats.add(StatType.DEFENSE.getTitle());
        stats.add(String.valueOf(selectedHero.getStatValueOf(SHIELD, StatType.DEFENSE)));
        stats.add("");
        createPreview(StatType.DEFENSE, selectedHero);

        stats.add("Spell Battery");
        stats.add("?");
        stats.add("");
        stats.add("").row();
    }

    private void createBonusFromInventory(StatType statType, HeroItem selectedHero) {
        int totalFromInventory = selectedHero.getTotalInventoryStatOf(statType);
        if (totalFromInventory > 0) {
            var label = new Label(String.format("+%s", totalFromInventory), new Label.LabelStyle(font, Color.FOREST));
            stats.add(label);
        } else if (totalFromInventory < 0) {
            var label = new Label(String.valueOf(totalFromInventory), new Label.LabelStyle(font, Color.FIREBRICK));
            stats.add(label);
        } else {
            stats.add("");
        }
    }

    private void createShieldPreview(StatType statType, HeroItem selectedHero) {
        InventoryItem hoveredItem = InventoryUtils.hoveredItem;
        if (hoveredItem == null) {
            stats.add("").row();
        } else if (hoveredItem.getGroup().equals(SHIELD)) {
            createPreview(statType, selectedHero);
        } else {
            stats.add("").row();
        }
    }

    private void createPreview(StatType statType, HeroItem selectedHero) {
        InventoryItem hoveredItem = InventoryUtils.hoveredItem;

        if (hoveredItem == null) {
            stats.add("").row();
        } else {
            int equippedValue = selectedHero.getStatValueOf(hoveredItem.getGroup(), statType);
            int hoveredValue = hoveredItem.getAttributeOfStatType(statType);
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
