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


class StatsTable implements SlotOverObserver {

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
    private InventoryItem hoveredItem;

    StatsTable() {
        var statsSkin = new Skin();
        this.font = Utils.getResourceManager().getTrueTypeAsset(TEXT_FONT, TEXT_SIZE);
        statsSkin.add("default", new Label.LabelStyle(this.font, Color.BLACK));

        this.stats = new Table(statsSkin);
        this.stats.defaults().height(LINE_HEIGHT).align(Align.left);
        this.stats.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.stats.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.stats.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        this.stats.columnDefaults(3).width(FOURTH_COLUMN_WIDTH);
        this.stats.pad(PADDING);
//        this.stats.debugAll();

        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        this.stats.setBackground(drawable);

        this.hoveredItem = null;
    }

    @Override
    public void receive(InventoryItem inventoryItem) {
        hoveredItem = inventoryItem;
    }

    void render() {
        HeroItem heroItem = Utils.getGameData().getParty().getHero(0);    // todo, fix index.
        stats.clear();

        stats.add("Intelligence");
        stats.add("?");
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
        stats.add(String.valueOf(heroItem.getOwnEndurance()));
        stats.add("");
        stats.add("").row();
        stats.add("Strength");
        stats.add(String.valueOf(heroItem.getOwnStrength()));
        stats.add("");
        stats.add("").row();
        stats.add("Stamina");
        stats.add(String.valueOf(heroItem.getOwnStamina()));
        stats.add("");
        stats.add("").row();
        stats.add("").row();

        stats.add("XP Remaining");
        stats.add(String.valueOf(heroItem.getXpRemaining()));
        stats.add("");
        stats.add("").row();
        stats.add("Total XP");
        stats.add(String.valueOf(heroItem.getTotalXp()));
        stats.add("");
        stats.add("").row();
        stats.add("Next Level");
        stats.add(String.valueOf(heroItem.getNeededXpForNextLevel())); // todo, is dit de juiste van de 2 methodes?
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
        stats.add(String.valueOf(heroItem.getAttributeValueOf(WEAPON, BASE_HIT) + "%"));
        stats.add("");
        createPreview(heroItem, BASE_HIT);
        stats.add("Damage");
        stats.add(String.valueOf(heroItem.getAttributeValueOf(WEAPON, DAMAGE)));
        stats.add("");
        createPreview(heroItem, DAMAGE);
        stats.add("Protection");
        stats.add("?");
        stats.add("");
        stats.add("").row();
        stats.add("Defense");
        stats.add(String.valueOf(heroItem.getAttributeValueOf(SHIELD, DEFENSE)));
        stats.add("");
        createPreview(heroItem, DEFENSE);
    }

    private void createPreview(HeroItem heroItem, InventoryAttribute attribute) {
        if (hoveredItem == null) {
            stats.add("").row();
        } else {
            int equippedValue = heroItem.getAttributeValueOf(hoveredItem.getGroup(), attribute);
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
