package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.quest.QuestGraph;


class QuestLogUI {

    private static final String TITLE_QUESTS = "   Quests";
    private static final String TITLE_SUMMARY = "   Summary";
    private static final String TITLE_TASKS = "   Tasks";

    final Window questListWindow;
    final Window summaryWindow;
    final Window taskListWindow;
    final QuestListTable questListTable;
    final SummaryTable summaryTable;
    final TaskListTable taskListTable;

    QuestLogUI() {
        this.questListTable = new QuestListTable();
        this.questListWindow = Utils.createDefaultWindow(TITLE_QUESTS, questListTable.container);

        this.summaryTable = new SummaryTable();
        this.summaryWindow = Utils.createDefaultWindow(TITLE_SUMMARY, summaryTable.container);

        this.taskListTable = new TaskListTable();
        this.taskListWindow = Utils.createDefaultWindow(TITLE_TASKS, taskListTable.getContainer());
    }

    void addToStage(Stage stage) {
        stage.addActor(questListWindow);
        stage.addActor(summaryWindow);
        stage.addActor(taskListWindow);
    }

    void applyListeners() {
        questListWindow.addListener(new QuestListListener(questListTable.questList, this::populateQuestSpecifics));
    }

    private void populateQuestSpecifics(QuestGraph quest) {
        taskListTable.populateTaskList(quest);
        summaryTable.populateSummary(quest);
    }

}
