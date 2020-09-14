package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.inventory.ListenerMouseScrollPane;


class QuestLogUI {

    private static final String TITLE_QUESTS = "   Quests";
    private static final String TITLE_TASKS = "   Tasks";

    final Window questListWindow;
    final Window taskListWindow;
    final QuestListTable questListTable;
    final TaskListTable taskListTable;

    QuestLogUI() {
        this.questListTable = new QuestListTable();
        this.questListWindow = Utils.createDefaultWindow(TITLE_QUESTS, questListTable.container);
        this.questListWindow.setMovable(false);
        this.taskListTable = new TaskListTable();
        this.taskListWindow = Utils.createDefaultWindow(TITLE_TASKS, taskListTable.container);
        this.taskListWindow.setMovable(false);
    }

    void addToStage(Stage stage) {
        stage.addActor(questListWindow);
        stage.addActor(taskListWindow);
    }

    void applyListeners(Stage stage) {
        questListWindow.addListener(new QuestListListener(questListTable.questList, taskListTable::populateTaskList));
        questListWindow.addListener(new ListenerMouseScrollPane(stage, questListTable.scrollPane));
        taskListWindow.addListener(new ListenerMouseScrollPane(stage, taskListTable.scrollPane));
    }

    void update() {
        // empty
    }

    void unloadAssets() {
        // empty
    }

}
