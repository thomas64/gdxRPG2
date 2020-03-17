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
import com.badlogic.gdx.utils.Json;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.conversation.Conversation;
import nl.t64.game.rpg.conversation.ConversationChoice;
import nl.t64.game.rpg.conversation.ConversationGraph;


public class ConversationDialog {

    private static final String CONVERSATION_CONFIGS = "configs/conversations/";
    private static final String SUFFIX = ".json";
    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String SPRITE_TRANSPARENT = "sprites/transparent.png";
    private static final String CONVERSATION_FONT = "fonts/fff_tusj.ttf";
    private static final int FONT_SIZE = 30;
    private static final float DIALOG_WIDTH = 1000f;
    private static final float DIALOG_HEIGHT = 300f;
    private static final float PAD = 25f;
    private static final float FACE_SIZE = 144f;
    private static final float ALL_PADS = 50f + 144f + 25f + 25f;
    private static final String CLOSE_DIALOG = "999";

    private final Stage stage;
    private final BitmapFont font;
    private final Dialog dialog;
    private final Runnable closeConversationFunction;

    private Label label;
    private List<ConversationChoice> answers;
    private ScrollPane scrollPane;

    private Image characterFace;
    private ConversationGraph graph;

    public ConversationDialog(Runnable closeConversationFunction) {
        this.closeConversationFunction = closeConversationFunction;
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
        dialog.show(stage, Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.4f, Interpolation.fade)));
    }

    public void update(float dt) {
        if (dialog.isVisible()) {
            stage.act(dt);
            stage.draw();
            stage.setKeyboardFocus(scrollPane);
        }
    }

    public void loadConversation(String conversationJsonFile, String characterId) {
        String fullFilenamePath = CONVERSATION_CONFIGS + conversationJsonFile + SUFFIX;
        characterFace.setDrawable(Utils.getFaceImage(characterId).getDrawable());
        graph = new Json().fromJson(ConversationGraph.class, Gdx.files.local(fullFilenamePath));
        String conversationId = graph.getCurrentConversationId();
        populateConversationDialog(conversationId);
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
        mainTable.add(characterFace).width(FACE_SIZE).padLeft(PAD * 2f);

        var textTable = new Table();
        textTable.pad(PAD);
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
        ConversationChoice choice = answers.getSelected();
        if (choice.getDestinationId().equals(CLOSE_DIALOG)) {
            closeDialog();
        } else {
            populateConversationDialog(choice.getDestinationId());
        }
    }

    private void closeDialog() {
        closeConversationFunction.run();
        dialog.hide();
    }

    private void populateConversationDialog(String conversationId) {
        Conversation conversation = graph.getConversationByID(conversationId);
        graph.setCurrentConversation(conversationId);
        String text = String.join(" ", conversation.getText());
        label.setText(text);
        answers.setItems(graph.getCurrentChoices().toArray(new ConversationChoice[0]));
        answers.setSelectedIndex(0);
    }

}
