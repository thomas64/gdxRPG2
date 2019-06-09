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


class StatsTable {

    private static final String SPRITE_TOP_BORDER = "sprites/top_border.png";
    private static final String TEXT_FONT = "fonts/spectral.ttf";
    private static final int TEXT_SIZE = 20;
    private static final float LINE_HEIGHT = 26f;
    private static final float FIRST_COLUMN_WIDTH = 200f;
    private static final float SECOND_COLUMN_WIDTH = 75f;
    private static final float PADDING = 20f;

    Table stats;

    StatsTable() {
        var statsSkin = new Skin();
        BitmapFont font = Utils.getResourceManager().getTrueTypeAsset(TEXT_FONT, TEXT_SIZE);
        statsSkin.add("default", new Label.LabelStyle(font, Color.BLACK));

        this.stats = new Table(statsSkin);
        this.stats.defaults().height(LINE_HEIGHT).align(Align.left);
        this.stats.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.stats.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.stats.pad(PADDING);

        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_TOP_BORDER);
        var ninepatch = new NinePatch(texture, 0, 0, 1, 0);
        var drawable = new NinePatchDrawable(ninepatch);
        this.stats.setBackground(drawable);
    }

    void render() {
        HeroItem heroItem = Utils.getGameData().getParty().getHero(0);    // todo, fix index.
        stats.clear();

        stats.add("Intelligence");
        stats.add("?").row();
        stats.add("Willpower");
        stats.add("?").row();
        stats.add("Dexterity");
        stats.add("?").row();
        stats.add("Agility");
        stats.add("?").row();
        stats.add("Endurance");
        stats.add(String.valueOf(heroItem.getOwnEndurance())).row();
        stats.add("Strength");
        stats.add(String.valueOf(heroItem.getOwnStrength())).row();
        stats.add("Stamina");
        stats.add(String.valueOf(heroItem.getOwnStamina())).row();
        stats.add("").row();
        stats.add("XP Remaining");
        stats.add(String.valueOf(heroItem.getXpRemaining())).row();
        stats.add("Total XP");
        stats.add(String.valueOf(heroItem.getTotalXp())).row();
        stats.add("Next Level");
        stats.add(String.valueOf(heroItem.getNeededXpForNextLevel())).row(); // todo, is dit de juiste van de 2 methodes?
        stats.add("").row();
        stats.add("Weight");
        stats.add("?").row();
        stats.add("Movepoints");
        stats.add("?").row();
        stats.add("Base Hit");
        stats.add(String.valueOf(heroItem.getWeaponBaseHit()) + "%").row();
        stats.add("Damage");
        stats.add(String.valueOf(heroItem.getWeaponDamage())).row(); // todo, hover maken
        stats.add("Protection");
        stats.add("?").row();
        stats.add("Defense");
        stats.add(String.valueOf(heroItem.getShieldDefense()));
    }

}
