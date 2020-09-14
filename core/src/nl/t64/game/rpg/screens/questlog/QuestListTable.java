package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.constants.Constant;


class QuestListTable {

    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";
    private static final String TEXT_FONT = "fonts/spectral.ttf";
    private static final int TEXT_SIZE = 20;
    private static final float WIDTH = -102f;
    private static final float HEIGHT = -152f;
    private static final float PAD_LEFT = 20f;

    private final BitmapFont font;
    final ScrollPane scrollPane;
    final Table container;
    final List<QuestGraph> questList;

    QuestListTable() {
        this.font = Utils.getResourceManager().getTrueTypeAsset(TEXT_FONT, TEXT_SIZE);
        this.questList = this.fillList();
        this.scrollPane = this.fillScrollPane();
        this.container = this.fillContainer();
    }

    private List<QuestGraph> fillList() {
        var spriteTransparent = new Sprite(Utils.getResourceManager().getTextureAsset(SPRITE_TRANSPARENT));
        var listStyle = new List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorSelected = Constant.DARK_RED;
        listStyle.fontColorUnselected = Color.BLACK;
        listStyle.background = new SpriteDrawable(spriteTransparent);
        listStyle.selection = new SpriteDrawable(spriteTransparent);

        List<QuestGraph> newList = new List<>(listStyle);
        newList.setItems(Utils.getGameData().getQuests().getAllKnownQuests().toArray(new QuestGraph[0]));
        newList.setSelectedIndex(-1);
        return newList;
    }

    private ScrollPane fillScrollPane() {
        var newScrollPane = new ScrollPane(questList);
        newScrollPane.setOverscroll(false, false);
        newScrollPane.setFadeScrollBars(false);
        newScrollPane.setScrollingDisabled(true, false);
        newScrollPane.setForceScroll(false, false);
        newScrollPane.setScrollBarPositions(false, false);
        return newScrollPane;
    }

    private Table fillContainer() {
        var newContainer = new Table();
        newContainer.setBackground(Utils.createTopBorder());
        newContainer.padLeft(PAD_LEFT);
        newContainer.add(scrollPane)
                    .width((Gdx.graphics.getWidth() / 2f) + WIDTH)
                    .height(Gdx.graphics.getHeight() + HEIGHT);
        return newContainer;
    }

}
