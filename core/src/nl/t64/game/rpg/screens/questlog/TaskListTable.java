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
import nl.t64.game.rpg.components.quest.QuestTask;
import nl.t64.game.rpg.constants.QuestState;


class TaskListTable {

    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";
    private static final String TEXT_FONT = "fonts/spectral.ttf";
    private static final int TEXT_SIZE = 20;
    private static final float WIDTH = -102f;
    private static final float HEIGHT = -152f;
    private static final float PAD_LEFT = 20f;

    private final BitmapFont font;
    final ScrollPane scrollPane;
    final Table container;
    private final List<QuestTask> taskList;

    TaskListTable() {
        this.font = Utils.getResourceManager().getTrueTypeAsset(TEXT_FONT, TEXT_SIZE);
        this.taskList = this.createList();
        this.scrollPane = this.fillScrollPane();
        this.container = this.fillContainer();
    }

    void populateTaskList(String questId) {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        if (!quest.getCurrentState().equals(QuestState.KNOWN)) {
            taskList.setItems(quest.getAllTasks().toArray(new QuestTask[0]));
        } else {
            taskList.clearItems();
        }
    }

    private List<QuestTask> createList() {
        var spriteTransparent = new Sprite(Utils.getResourceManager().getTextureAsset(SPRITE_TRANSPARENT));
        var listStyle = new List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorSelected = Color.BLACK;
        listStyle.fontColorUnselected = Color.BLACK;
        listStyle.background = new SpriteDrawable(spriteTransparent);
        listStyle.selection = new SpriteDrawable(spriteTransparent);
        return new List<>(listStyle);
    }

    private ScrollPane fillScrollPane() {
        var newScrollPane = new ScrollPane(taskList);
        newScrollPane.setOverscroll(false, false);
        newScrollPane.setFadeScrollBars(false);
        newScrollPane.setScrollingDisabled(true, true);
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
