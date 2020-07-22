package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Engine;
import nl.t64.game.rpg.components.character.Character;
import nl.t64.game.rpg.constants.Constant;


class DebugBox {

    private static final Color TRANSPARENT = new Color(0f, 0f, 0f, 0.5f);
    private static final float TABLE_WIDTH = 200f;
    private static final float SECOND_COLUMN_WIDTH = 100f;

    private final Stage stage;
    private final Table table;
    private final Character player;


    DebugBox(Character player) {
        this.stage = new Stage();
        this.table = createTable();
        this.player = player;
        this.stage.addActor(this.table);
    }

    void dispose() {
        stage.clear();
        stage.dispose();
    }

    void update(float dt) {
        table.clear();

        table.add("FPS:");
        table.add(String.valueOf(Gdx.graphics.getFramesPerSecond()));
        table.row();
        table.add("dt:");
        table.add(String.valueOf(dt));
        table.row();
        table.add("runTime:");
        String runTime = String.valueOf(Engine.getRunTime());
        table.add(runTime.substring(0, runTime.length() - 4));
        table.row();
        table.add("").row();

//        table.add("timeUp:");
//        table.add(player.getInputComponent().getTimeUp());
//        table.row();
//        table.add("timeDown:");
//        table.add(player.getInputComponent().getTimeDown());
//        table.row();
//        table.add("timeLeft:");
//        table.add(player.getInputComponent().getTimeLeft());
//        table.row();
//        table.add("timeRight:");
//        table.add(player.getInputComponent().getTimeRight());
//        table.row();
//        table.add("timeDelay:");
//        table.add(player.getInputComponent().getTimeDelay());
//        table.row();
//        table.add("").row();
//
//        table.add("oldPositionX:");
//        table.add(String.valueOf(player.getOldPosition().x));
//        table.row();
//        table.add("oldPositionY:");
//        table.add(String.valueOf(player.getOldPosition().y));
//        table.row();
//        table.add("").row();

        table.add("currentPositionX:");
        table.add(String.valueOf(player.getPosition().x));
        table.row();
        table.add("currentPositionY:");
        table.add(String.valueOf(player.getPosition().y));
        table.row();
        table.add("").row();

        table.add("currentPositionGridX:");
        table.add(String.valueOf(player.getPositionInGrid().x));
        table.row();
        table.add("currentPositionGridY:");
        table.add(String.valueOf(player.getPositionInGrid().y));
        table.row();
        table.add("").row();

        table.add("currentPositionTiledX:");
        table.add(String.valueOf((int) (player.getPosition().x / Constant.TILE_SIZE)));
        table.row();
        table.add("currentPositionTiledY:");
        table.add(String.valueOf((int) (player.getPosition().y / Constant.TILE_SIZE)));
        table.row();
        table.add("").row();

        table.add("direction:");
        table.add(String.valueOf(player.getDirection()));
        table.row();
        table.add("state:");
        table.add(String.valueOf(player.getState()));
        table.row();
        table.add("").row();

        table.pack();
        table.setPosition(0, Gdx.graphics.getHeight() - table.getHeight());

        stage.act(dt);
        stage.draw();
    }

    private Table createTable() {
        var debugSkin = new Skin();
        debugSkin.add("default", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        var newTable = new Table(debugSkin);
        newTable.defaults().width(TABLE_WIDTH).align(Align.left);
        newTable.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        newTable.setBackground(createTransparency());

        return newTable;
    }

    private Drawable createTransparency() {
        var pixmap = new Pixmap(1, 1, Pixmap.Format.Alpha);
        pixmap.setColor(TRANSPARENT);
        pixmap.fill();
        return new Image(new Texture(pixmap)).getDrawable();
    }

}
