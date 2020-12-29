package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.constants.Constant;


class SummaryTable {

    private static final String TEXT_FONT = "fonts/spectral_extra_bold_20.ttf";
    private static final int TEXT_SIZE = 20;
    private static final float WIDTH = -102f;
    private static final float PAD = 20f;

    final Table container;
    private final Label summary;

    SummaryTable() {
        this.summary = this.createLabel();
        this.container = this.createContainer();
    }

    void populateSummary(QuestGraph quest) {
        summary.setText(quest.getSummary());
        container.getCells().peek().setActor(Utils.getFaceImage(quest.getCharacter()));
    }

    private Label createLabel() {
        var font = Utils.getResourceManager().getTrueTypeAsset(TEXT_FONT, TEXT_SIZE);
        var labelStyle = new Label.LabelStyle(font, Color.BLACK);
        var label = new Label("", labelStyle);
        label.setWrap(true);
        return label;
    }

    private Table createContainer() {
        var table = new Table();
        table.defaults().align(Align.topLeft);
        table.columnDefaults(0).width((Gdx.graphics.getWidth() / 2f) + WIDTH - Constant.FACE_SIZE - PAD).pad(PAD);
        table.columnDefaults(1).size(Constant.FACE_SIZE);
        table.setBackground(Utils.createTopBorder());

        table.add(summary);
        table.add();
        return table;
    }

}
