package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.components.quest.QuestState;
import nl.t64.game.rpg.components.quest.QuestTask;


class TaskListTable {

    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";
    private static final String TEXT_FONT = "fonts/spectral_extra_bold_20.ttf";
    private static final int TEXT_SIZE = 20;
    private static final float WIDTH = -102f;
    private static final float HEIGHT = 704f;
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

    void populateTaskList(QuestGraph quest) {
        taskList.clearItems();
        if (!quest.getCurrentState().equals(QuestState.KNOWN)) {
            taskList.setItems(quest.getAllTasks().toArray(QuestTask[]::new));
            taskList.setAlignment(Align.left);
        } else {
            taskList.setItems(new QuestTask("(No tasks visible until this quest is accepted)"));
            taskList.setAlignment(Align.center);
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
                    .height(HEIGHT);
        return newContainer;
    }

}
