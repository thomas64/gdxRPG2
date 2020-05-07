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
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ConversationCommand;
import nl.t64.game.rpg.conversation.ConversationChoice;
import nl.t64.game.rpg.conversation.ConversationGraph;
import nl.t64.game.rpg.conversation.ConversationPhrase;

import java.util.function.Consumer;


public class ConversationDialog {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";
    private static final String CONVERSATION_FONT = "fonts/fff_tusj.ttf";
    private static final int FONT_SIZE = 30;
    private static final float DIALOG_WIDTH = 1000f;
    private static final float DIALOG_HEIGHT = 300f;
    private static final float PAD = 25f;
    private static final float ALL_PADS = (PAD * 2f) + Constant.FACE_SIZE + PAD + (PAD * 2f);

    private final Stage stage;
    private final BitmapFont font;
    private final Dialog dialog;
    private final Consumer<ConversationCommand> handleConversationCommand;

    private Label label;
    private List<ConversationChoice> answers;
    private ScrollPane scrollPane;

    private Image characterFace;
    private ConversationGraph graph;

    public ConversationDialog(Consumer<ConversationCommand> handleConversationCommand) {
        this.handleConversationCommand = handleConversationCommand;
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
    }

    public void hideWithFade() {
        dialog.hide();
    }

    public void hide() {
        dialog.hide(null);
    }

    public void update(float dt) {
        if (dialog.isVisible()) {
            stage.act(dt);
            stage.draw();
            stage.setKeyboardFocus(scrollPane);
        }
    }

    public void loadConversation(String conversationId, String characterId) {
        characterFace.setDrawable(Utils.getFaceImage(characterId).getDrawable());
        graph = Utils.getGameData().getConversations().getConversationById(conversationId);
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
        characterFace = new Image();
        label = new Label("No Conversation", labelStyle);
        label.setWrap(true);
        answers = new List<>(listStyle);
        scrollPane = new ScrollPane(answers);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(true, false);
        scrollPane.setScrollBarPositions(false, true);

        // table
        var mainTable = new Table();
        mainTable.left();
        mainTable.add(characterFace).width(Constant.FACE_SIZE).padLeft(PAD * 2f);

        var textTable = new Table();
        textTable.pad(PAD, PAD, PAD, PAD * 2f);
        textTable.add(label).width(DIALOG_WIDTH - ALL_PADS).row();
        textTable.add().height(PAD).row();
        textTable.add(scrollPane).left().padLeft(PAD);
        mainTable.add(textTable);

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
        newDialog.getContentTable().add(mainTable);
        newDialog.setPosition((Gdx.graphics.getWidth() / 2f) - (DIALOG_WIDTH / 2f), 0f);
        return newDialog;
    }

    private void applyListeners() {
        scrollPane.addListener(new ConversationDialogListener(answers, this::selectAnswer));
    }

    private void selectAnswer() {
        final ConversationChoice selectedAnswer = answers.getSelected();
        final String destinationId = selectedAnswer.getDestinationId();
        final ConversationCommand conversationCommand = selectedAnswer.getConversationCommand();
        if (conversationCommand.equals(ConversationCommand.NONE)) {
            populateConversationDialog(destinationId);
        } else {
            graph.setCurrentPhraseId(destinationId);
            handleConversationCommand.accept(conversationCommand);
        }
    }

    private void populateConversationDialog(String phraseId) {
        ConversationPhrase phrase = graph.getPhraseById(phraseId);
        graph.setCurrentPhraseId(phraseId);
        String text = String.join(System.lineSeparator(), phrase.getText());
        label.setText(text);
        ConversationChoice[] choices = graph.getAssociatedChoices().toArray(new ConversationChoice[0]);
        answers.setItems(choices);
        answers.setSelectedIndex(0);
    }

}
