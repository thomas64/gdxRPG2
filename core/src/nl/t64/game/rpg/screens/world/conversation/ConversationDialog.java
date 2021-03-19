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
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.conversation.ConversationChoice;
import nl.t64.game.rpg.components.conversation.ConversationCommand;
import nl.t64.game.rpg.components.conversation.ConversationGraph;
import nl.t64.game.rpg.components.conversation.NoteDatabase;
import nl.t64.game.rpg.components.party.HeroContainer;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.constants.Constant;


public class ConversationDialog {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";
    private static final String CONVERSATION_FONT = "fonts/spectral_regular_24.ttf";
    private static final int FONT_SIZE = 24;
    private static final float LINE_HEIGHT = 26f;
    private static final float SCROLL_PANE_LINE_HEIGHT = 32f;
    private static final float SCROLL_PANE_LINE_PAD = -4f;
    private static final float SCROLL_PANE_TOP_PAD = 10f;
    private static final float DIALOG_WIDTH = 1000f;
    private static final float DIALOG_HEIGHT = 300f;
    private static final float PAD = 25f;
    private static final float ALL_PADS = (PAD * 2f) + Constant.FACE_SIZE + PAD + (PAD * 3f);

    public final ConversationSubject conversationObservers;
    private final Stage stage;
    private final BitmapFont font;
    private final Dialog dialog;

    private Label label;
    private List<ConversationChoice> answers;
    private ScrollPane scrollPane;
    private Cell<ScrollPane> rowWithScrollPane;

    private String conversationId;
    private String faceId;
    private ConversationGraph graph;

    public ConversationDialog() {
        this.conversationObservers = new ConversationSubject();
        this.stage = new Stage();
        this.font = Utils.getResourceManager().getTrueTypeAsset(CONVERSATION_FONT, FONT_SIZE);
        this.font.getData().setLineHeight(LINE_HEIGHT);
        this.dialog = this.createDialog();
        this.applyListeners();
    }

    public void dispose() {
        stage.dispose();
        try {
            font.dispose();
        } catch (GdxRuntimeException e) {
            // font is already exposed.
        }
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
        Utils.setGamepadInputProcessor(stage);
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
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_START);
        populateConversationDialog(graph.getCurrentPhraseId());
    }

    public void loadNote(String noteId) {
        conversationId = null;
        faceId = null;
        graph = NoteDatabase.getInstance().getNoteById(noteId);
        fillDialogForNote();
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_START);
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
        var drawable = new SpriteDrawable(spriteTransparent);
        drawable.setTopHeight(SCROLL_PANE_LINE_PAD);
        drawable.setBottomHeight(SCROLL_PANE_LINE_PAD);
        listStyle.selection = drawable;

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
        textTable.add().height(SCROLL_PANE_TOP_PAD).row();
        textTable.add(scrollPane).left().padLeft(PAD);
        rowWithScrollPane = textTable.getCell(scrollPane);

        mainTable.add(textTable);
        dialog.getContentTable().clear();
        dialog.getContentTable().add(mainTable);
    }

    private void fillDialogForNote() {
        label.setAlignment(Align.center);
        var textTable = new Table();
        textTable.pad(PAD * 2f, PAD * 3f, PAD, PAD * 2f);
        textTable.add(label).width(DIALOG_WIDTH - (PAD * 5f)).row();
        textTable.add().height(SCROLL_PANE_TOP_PAD).row();
        textTable.add(scrollPane).left().padLeft(PAD);
        rowWithScrollPane = textTable.getCell(scrollPane);

        dialog.getContentTable().clear();
        dialog.getContentTable().add(textTable);
    }

    private void applyListeners() {
        scrollPane.addListener(new ConversationDialogListener(answers, this::selectAnswer));
    }

    private void continueConversation(String destinationId) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_NEXT);
        continueConversationWithoutSound(destinationId);
    }

    private void continueConversationWithoutSound(String destinationId) {
        populateConversationDialog(destinationId);
    }

    private void endConversation(String destinationId) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_END);
        endConversationWithoutSound(destinationId);
    }

    private void endConversationWithoutSound(String destinationId) {
        graph.setCurrentPhraseId(destinationId);
        conversationObservers.notifyExitConversation();
    }

    private void populateConversationDialog(String phraseId) {
        graph.setCurrentPhraseId(phraseId);
        populatePhrase();
        populateChoices();
    }

    private void populatePhrase() {
        String text = String.join(System.lineSeparator(), graph.getCurrentPhrase());
        label.setText(text);
    }

    private void populateChoices() {
        ConversationChoice[] choices = graph.getAssociatedChoices();
        answers.setItems(choices);
        setDefaultSelectedChoice(choices);
        setScrollPaneHeight(choices);
    }

    private void setDefaultSelectedChoice(ConversationChoice[] choices) {
        if (choices.length == 1) {
            answers.setSelectedIndex(0);
        } else {
            answers.setSelectedIndex(-1);
        }
    }

    private void setScrollPaneHeight(ConversationChoice[] choices) {
        if (choices.length % 2 == 0) {
            rowWithScrollPane.height(choices.length * SCROLL_PANE_LINE_HEIGHT);
        } else {
            rowWithScrollPane.height(choices.length * SCROLL_PANE_LINE_HEIGHT + 2f);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void selectAnswer() {
        final ConversationChoice selectedAnswer = answers.getSelected();
        if (!selectedAnswer.isMeetingCondition()) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR);
            return;
        }
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
            case KNOW_QUEST -> knowQuest(destinationId);
            case TOLERATE_QUEST -> tolerateQuest();
            case RECEIVE_ITEM -> receiveItem(destinationId);
            case CHECK_IF_LINKED_QUEST_KNOWN -> checkIfLinkedQuestKnown(destinationId);
            case CHECK_IF_QUEST_ACCEPTED -> checkIfQuestAccepted(destinationId);
            case CHECK_IF_IN_INVENTORY -> checkIfInInventory(destinationId);
            case COMPLETE_QUEST_TASK -> completeTask(destinationId);
            case RETURN_QUEST -> returnQuest();
            case REWARD_QUEST -> rewardQuest();
            case BONUS_REWARD_QUEST -> bonusRewardQuest();
            case FAIL_QUEST -> failQuest(destinationId);
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
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_JOIN);
            heroes.removeHero(faceId);
            party.addHero(hero);
            conversationObservers.notifyHeroJoined();
            endConversationWithoutSound(destinationId);
        }
    }

    private void dismissHero(String destinationId) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_END);
        graph.setCurrentPhraseId(destinationId);
        conversationObservers.notifyHeroDismiss();                                      // ends conversation
    }

    private void loadShop(String destinationId) {
        graph.setCurrentPhraseId(destinationId);
        conversationObservers.notifyLoadShop();                                         // ends conversation
    }

    private void saveGame(String destinationId) {
        Utils.getProfileManager().saveProfile();
        continueConversation(destinationId);
    }

    private void acceptQuest() {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleAccept(this::continueConversation, conversationObservers);          // sets new phraseId
    }

    private void knowQuest(String destinationId) {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.know();
        continueConversation(destinationId);
    }

    private void tolerateQuest() {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleTolerate(conversationObservers);
        continueConversation(Constant.PHRASE_ID_QUEST_TOLERATE);
    }

    private void receiveItem(String destinationId) {
        graph.setCurrentPhraseId(destinationId);
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleReceive(conversationObservers);                     // ends conversation, sets possible new phraseId
    }

    private void checkIfLinkedQuestKnown(String destinationId) {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleCheckIfLinkedIsKnown(destinationId, this::continueConversation);    // sets possible new phraseId
    }

    private void checkIfQuestAccepted(String destinationId) {
        String questId = conversationId.substring(0, conversationId.length() - 2);
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        quest.handleCheckIfAccepted(destinationId, this::continueConversation, this::endConversation);
    }

    private void checkIfInInventory(String destinationId) {
        String questId = conversationId.substring(0, conversationId.length() - 2);
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        String questTaskId = conversationId.substring(conversationId.length() - 1);
        quest.handleCheckIfAcceptedInventory(questTaskId, destinationId, this::continueConversation, this::endConversation);
    }

    private void completeTask(String destinationId) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_REWARD);
        String questId = conversationId.substring(0, conversationId.length() - 2);
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        String questTaskId = conversationId.substring(conversationId.length() - 1);
        quest.setTaskComplete(questTaskId);
        continueConversationWithoutSound(destinationId);
    }

    private void returnQuest() {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleReturn(this::continueConversation);                                 // sets new phraseId
    }

    private void bonusRewardQuest() {
        Utils.getGameData().getLoot().getLoot(conversationId).handleBonus();
        rewardQuest();
    }

    private void rewardQuest() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_REWARD);
        graph.setCurrentPhraseId(Constant.PHRASE_ID_QUEST_UNCLAIMED);
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleReward(this::endConversationWithoutSound, conversationObservers);   // ends conversation, sets possible new phraseId
    }

    private void failQuest(String destinationId) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_QUEST_FAIL);
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        quest.handleFail(conversationObservers);
        continueConversation(destinationId);
    }

}
