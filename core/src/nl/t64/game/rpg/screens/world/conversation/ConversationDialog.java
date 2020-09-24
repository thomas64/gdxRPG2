package nl.t64.game.rpg.screens.world.conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.conversation.*;
import nl.t64.game.rpg.components.party.HeroContainer;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.constants.Constant;


public class ConversationDialog extends ConversationSubject {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";
    private static final String CONVERSATION_FONT = "fonts/fff_tusj.ttf";
    private static final int FONT_SIZE = 30;
    private static final float LINE_HEIGHT = 40f;
    private static final float DIALOG_WIDTH = 1000f;
    private static final float DIALOG_HEIGHT = 300f;
    private static final float PAD = 25f;
    private static final float ALL_PADS = (PAD * 2f) + Constant.FACE_SIZE + PAD + (PAD * 3f);

    private final Stage stage;
    private final BitmapFont font;
    private final Dialog dialog;

    private Label label;
    private List<ConversationChoice> answers;
    private ScrollPane scrollPane;
    private Cell<?> scrollPaneRow;

    private String conversationId;
    private String faceId;
    private ConversationGraph graph;

    public ConversationDialog() {
        this.stage = new Stage();
        this.font = Utils.getResourceManager().getTrueTypeAsset(CONVERSATION_FONT, FONT_SIZE);
        this.dialog = this.createDialog();
        this.applyListeners();
    }

    public void dispose() {
        stage.clear();
        stage.dispose();
        try {
            font.dispose();
        } catch (GdxRuntimeException e) {
            // font is already exposed.
        }
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
        dialog.show(stage, Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.4f, Interpolation.fade)));
        stage.setKeyboardFocus(scrollPane);
    }

    public void hideWithFade() {
        dialog.hide();
    }

    public void hide() {
        dialog.hide(null);
    }

    public void update(float dt) {
        stage.act(dt);
        stage.draw();
    }

    public void loadConversation(String conversationId, String characterId) {
        this.conversationId = conversationId;
        this.faceId = characterId;
        this.graph = Utils.getGameData().getConversations().getConversationById(conversationId);
        fillDialogForConversation();
        populateConversationDialog(graph.getCurrentPhraseId());
    }

    public void loadNote(String noteId) {
        conversationId = null;
        faceId = null;
        graph = NoteDatabase.getInstance().getNoteById(noteId);
        fillDialogForNote();
        populateConversationDialog(graph.getCurrentPhraseId());
    }

    private Dialog createDialog() {
        var spriteTransparent = new Sprite(Utils.getResourceManager().getTextureAsset(SPRITE_TRANSPARENT));

        // styles
        var labelStyle = new Label.LabelStyle(font, Color.BLACK);
        var windowStyle = new Dialog.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.titleFontColor = Color.BLACK;
        var listStyle = new List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorSelected = Constant.DARK_RED;
        listStyle.fontColorUnselected = Color.BLACK;
        listStyle.background = new SpriteDrawable(spriteTransparent);
        listStyle.selection = new SpriteDrawable(spriteTransparent);

        // actors
        label = new Label("No Conversation", labelStyle);
        label.setWrap(true);
        answers = new List<>(listStyle);
        scrollPane = new ScrollPane(answers);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, true);
        scrollPane.setForceScroll(false, false);
        scrollPane.setScrollBarPositions(false, false);

        // dialog
        var newDialog = new Dialog("", windowStyle);
        newDialog.getTitleLabel().setAlignment(Align.center);
        newDialog.setKeepWithinStage(true);
        newDialog.setModal(true);
        newDialog.setMovable(false);
        newDialog.setResizable(false);
        var sprite = new Sprite(Utils.getResourceManager().getTextureAsset(SPRITE_PARCHMENT));
        sprite.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        newDialog.setBackground(new SpriteDrawable(sprite));
        newDialog.setPosition((Gdx.graphics.getWidth() / 2f) - (DIALOG_WIDTH / 2f), 0f);
        return newDialog;
    }

    private void fillDialogForConversation() {
        label.setAlignment(Align.left);
        var mainTable = new Table();
        mainTable.left();
        Image faceImage = Utils.getFaceImage(faceId);
        mainTable.add(faceImage).width(Constant.FACE_SIZE).padLeft(PAD * 2f);

        var textTable = new Table();
        textTable.pad(PAD, PAD, PAD, PAD * 3f);
        textTable.add(label).width(DIALOG_WIDTH - ALL_PADS).row();
        textTable.add().height(PAD).row();
        textTable.add(scrollPane).left().padLeft(PAD);
        scrollPaneRow = textTable.getCells().peek();

        mainTable.add(textTable);
        dialog.getContentTable().clear();
        dialog.getContentTable().add(mainTable);
    }

    private void fillDialogForNote() {
        label.setAlignment(Align.center);
        var textTable = new Table();
        textTable.pad(PAD * 2f, PAD * 3f, PAD, PAD * 2f);
        textTable.add(label).width(DIALOG_WIDTH - (PAD * 5f)).row();
        textTable.add().height(PAD).row();
        textTable.add(scrollPane).left().padLeft(PAD);
        scrollPaneRow = textTable.getCells().peek();

        dialog.getContentTable().clear();
        dialog.getContentTable().add(textTable);
    }

    private void applyListeners() {
        scrollPane.addListener(new ConversationDialogListener(answers, this::selectAnswer));
    }

    private void continueConversation(String destinationId) {
        populateConversationDialog(destinationId);
    }

    private void endConversation(String destinationId) {
        graph.setCurrentPhraseId(destinationId);
        notifyExitConversation();
    }

    private void populateConversationDialog(String phraseId) {
        ConversationPhrase phrase = graph.getPhraseById(phraseId);
        graph.setCurrentPhraseId(phraseId);
        String text = String.join(System.lineSeparator(), phrase.getText());
        label.setText(text);
        ConversationChoice[] choices = graph.getAssociatedChoices().toArray(new ConversationChoice[0]);
        answers.setItems(choices);
        answers.setSelectedIndex(0);

        scrollPaneRow.height(choices.length * LINE_HEIGHT);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void selectAnswer() {
        final ConversationChoice selectedAnswer = answers.getSelected();
        final String destinationId = selectedAnswer.getDestinationId();
        final ConversationCommand conversationCommand = selectedAnswer.getConversationCommand();

        switch (conversationCommand) {
            case NONE -> continueConversation(destinationId);
            case EXIT_CONVERSATION -> endConversation(destinationId);
            case HERO_JOIN -> tryToAddHeroToParty(destinationId);
            case HERO_DISMISS -> dismissHero(destinationId);
            case LOAD_SHOP -> loadShop(destinationId);
            case SAVE_GAME -> saveGame(destinationId);
            case ACCEPT_QUEST -> acceptQuest();
            case REJECT_QUEST -> rejectQuest(destinationId);
            case TOLERATE_QUEST -> tolerateQuest();
            case RECEIVE_ITEM -> receiveItem(destinationId);
            case CHECK_IF_QUEST_ACCEPTED -> checkQuest(destinationId);
            case CHECK_IF_IN_INVENTORY -> checkInventory(destinationId);
            case COMPLETE_QUEST_TASK -> completeTask(destinationId);
            case RETURN_QUEST -> returnQuest();
            case REWARD_QUEST -> rewardQuest();
            default -> throw new IllegalStateException(
                    String.format("ConversationCommand '%s' cannot be reached here.", conversationCommand));
        }
    }

    private void tryToAddHeroToParty(String destinationId) {
        HeroContainer heroes = Utils.getGameData().getHeroes();
        PartyContainer party = Utils.getGameData().getParty();
        HeroItem hero = heroes.getHero(faceId);

        if (party.isFull()) {
            continueConversation(Constant.PHRASE_ID_PARTY_FULL);
        } else {
            heroes.removeHero(faceId);
            party.addHero(hero);
            notifyHeroJoined();
            endConversation(destinationId);
        }
    }

    private void dismissHero(String destinationId) {
        graph.setCurrentPhraseId(destinationId);
        notifyHeroDismiss();                                        // ends conversation
    }

    private void loadShop(String destinationId) {
        graph.setCurrentPhraseId(destinationId);
        notifyLoadShop();                                           // ends conversation
    }

    private void saveGame(String destinationId) {
        Utils.getProfileManager().saveProfile();
        continueConversation(destinationId);
    }

    private void acceptQuest() {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleAccept(this::continueConversation);             // sets new phraseId
    }

    private void rejectQuest(String destinationId) {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.know();
        continueConversation(destinationId);
    }

    private void tolerateQuest() {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleTolerate(this::continueConversation);           // sets new phraseId
    }

    private void receiveItem(String destinationId) {
        graph.setCurrentPhraseId(destinationId);
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleReceive(super::notifyShowReceiveDialog);        // ends conversation, sets possible new phraseId
    }

    private void checkQuest(String destinationId) {
        String questId = conversationId.substring(0, conversationId.length() - 2);
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        quest.handleCheck(destinationId, this::continueConversation, this::endConversation);
    }

    private void checkInventory(String destinationId) {
        String questId = conversationId.substring(0, conversationId.length() - 2);
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        String questTaskId = conversationId.substring(conversationId.length() - 1);
        quest.handleInventory(questTaskId, destinationId, this::continueConversation, this::endConversation);
    }

    private void completeTask(String destinationId) {
        String questId = conversationId.substring(0, conversationId.length() - 2);
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        String questTaskId = conversationId.substring(conversationId.length() - 1);
        quest.setTaskComplete(questTaskId);
        continueConversation(destinationId);
    }

    private void returnQuest() {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleReturn(this::continueConversation);             // sets new phraseId
    }

    private void rewardQuest() {
        graph.setCurrentPhraseId(Constant.PHRASE_ID_QUEST_UNCLAIMED);
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleReward(super::notifyShowMessageDialog,
                           super::notifyShowRewardDialog,           // ends conversation, sets possible new phraseId
                           this::endConversation);                  // sets new phraseId
    }

}
