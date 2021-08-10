package nl.t64.game.rpg.screens.world.conversation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.GdxRuntimeException
import ktx.collections.GdxArray
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.Utils.profileManager
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.components.conversation.ConversationChoice
import nl.t64.game.rpg.components.conversation.ConversationCommand
import nl.t64.game.rpg.components.conversation.ConversationGraph
import nl.t64.game.rpg.components.conversation.NoteDatabase.getNoteById
import nl.t64.game.rpg.constants.Constant


private const val SPRITE_PARCHMENT = "sprites/parchment.png"
private const val SPRITE_TRANSPARENT = "sprites/transparent.png"
private const val CONVERSATION_FONT = "fonts/spectral_regular_24.ttf"
private const val FONT_SIZE = 24
private const val LINE_HEIGHT = 26f
private const val SCROLL_PANE_LINE_HEIGHT = 32f
private const val SCROLL_PANE_LINE_PAD = -4f
private const val SCROLL_PANE_TOP_PAD = 10f
private const val DIALOG_WIDTH = 1000f
private const val DIALOG_HEIGHT = 300f
private const val PAD = 25f
private const val ALL_PADS = PAD * 2f + Constant.FACE_SIZE + PAD + PAD * 3f

class ConversationDialog {

    val conversationObservers: ConversationSubject = ConversationSubject()

    private val stage: Stage = Stage()
    private val font: BitmapFont = resourceManager.getTrueTypeAsset(CONVERSATION_FONT, FONT_SIZE).apply {
        data.setLineHeight(LINE_HEIGHT)
    }
    private val dialog: Dialog = createDialog()

    private lateinit var label: Label
    private lateinit var answers: List<ConversationChoice>
    private lateinit var scrollPane: ScrollPane
    private lateinit var rowWithScrollPane: Cell<ScrollPane>

    private var conversationId: String? = null
    private var faceId: String? = null
    private var faceImage: Image? = null
    private lateinit var graph: ConversationGraph

    init {
        applyListeners()
    }

    fun dispose() {
        stage.dispose()
        try {
            font.dispose()
        } catch (e: GdxRuntimeException) {
            // font is already exposed.
        }
    }

    fun show() {
        Gdx.input.inputProcessor = stage
        Utils.setGamepadInputProcessor(stage)
        dialog.show(stage, Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.4f, Interpolation.fade)))
        stage.keyboardFocus = scrollPane
    }

    fun hideWithFade() {
        dialog.hide()
    }

    fun hide() {
        dialog.hide(null)
    }

    fun update(dt: Float) {
        stage.act(dt)
        stage.draw()
    }

    fun loadConversation(conversationId: String, entityId: String? = null) {
        this.conversationId = conversationId
        faceId = entityId
        graph = gameData.conversations.getConversationById(conversationId)
        fillDialogForConversation()
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_START)
        populateConversationDialog(graph.currentPhraseId)
    }

    fun loadNote(noteId: String) {
        conversationId = null
        faceId = null
        graph = getNoteById(noteId)
        fillDialogForNote()
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_START)
        populateConversationDialog(graph.currentPhraseId)
    }

    private fun createDialog(): Dialog {
        val spriteTransparent = Sprite(resourceManager.getTextureAsset(SPRITE_TRANSPARENT))

        // styles
        val labelStyle = LabelStyle(font, Color.BLACK)
        val windowStyle = WindowStyle()
        windowStyle.titleFont = font
        windowStyle.titleFontColor = Color.BLACK
        val listStyle = ListStyle()
        listStyle.font = font
        listStyle.fontColorSelected = Constant.DARK_RED
        listStyle.fontColorUnselected = Color.BLACK
        val drawable = SpriteDrawable(spriteTransparent)
        drawable.topHeight = SCROLL_PANE_LINE_PAD
        drawable.bottomHeight = SCROLL_PANE_LINE_PAD
        listStyle.selection = drawable

        // actors
        label = Label("No Conversation", labelStyle)
        label.wrap = true
        answers = List(listStyle)
        scrollPane = ScrollPane(answers)
        scrollPane.setOverscroll(false, false)
        scrollPane.fadeScrollBars = false
        scrollPane.setScrollingDisabled(true, true)
        scrollPane.setForceScroll(false, false)
        scrollPane.setScrollBarPositions(false, false)

        // dialog
        val newDialog = Dialog("", windowStyle)
        newDialog.titleLabel.setAlignment(Align.center)
        newDialog.setKeepWithinStage(true)
        newDialog.isModal = true
        newDialog.isMovable = false
        newDialog.isResizable = false
        val sprite = Sprite(resourceManager.getTextureAsset(SPRITE_PARCHMENT))
        sprite.setSize(DIALOG_WIDTH, DIALOG_HEIGHT)
        newDialog.background = SpriteDrawable(sprite)
        newDialog.setPosition(Gdx.graphics.width / 2f - DIALOG_WIDTH / 2f, 0f)
        return newDialog
    }

    private fun fillDialogForConversation() {
        label.setAlignment(Align.left)
        val mainTable = Table()
        mainTable.left()
        faceImage = getFaceImage()
        mainTable.add<Actor>(faceImage).width(Constant.FACE_SIZE).padLeft(PAD * 2f)

        val textTable = Table()
        textTable.pad(PAD, PAD, PAD, PAD * 3f)
        textTable.add<Actor>(label).width(DIALOG_WIDTH - ALL_PADS).row()
        textTable.add().height(SCROLL_PANE_TOP_PAD).row()
        textTable.add<Actor>(scrollPane).left().padLeft(PAD)
        rowWithScrollPane = textTable.getCell(scrollPane)

        mainTable.add(textTable)
        dialog.contentTable.clear()
        dialog.contentTable.add(mainTable)
    }

    private fun getFaceImage(): Image {
        return faceId?.let { Utils.getFaceImage(it) } ?: Image()
    }

    private fun fillDialogForNote() {
        label.setAlignment(Align.center)
        val textTable = Table()
        textTable.pad(PAD * 2f, PAD * 3f, PAD, PAD * 2f)
        textTable.add<Actor>(label).width(DIALOG_WIDTH - PAD * 5f).row()
        textTable.add().height(SCROLL_PANE_TOP_PAD).row()
        textTable.add<Actor>(scrollPane).left().padLeft(PAD)
        rowWithScrollPane = textTable.getCell(scrollPane)

        dialog.contentTable.clear()
        dialog.contentTable.add(textTable)
    }

    private fun applyListeners() {
        scrollPane.addListener(ConversationDialogListener(answers) { selectAnswer() })
    }

    private fun continueConversation(destinationId: String) {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_NEXT)
        continueConversationWithoutSound(destinationId)
    }

    private fun continueConversationWithoutSound(destinationId: String) {
        populateConversationDialog(destinationId)
    }

    private fun endConversation(destinationId: String) {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_END)
        endConversationWithoutSound(destinationId)
    }

    private fun endConversationWithoutSound(destinationId: String) {
        graph.currentPhraseId = destinationId
        conversationObservers.notifyExitConversation()
    }

    private fun populateConversationDialog(phraseId: String) {
        graph.currentPhraseId = phraseId
        populateFace()
        populatePhrase()
        populateChoices()
    }

    private fun populateFace() {
        if (graph.getCurrentFace().isNotBlank()) {
            val mainTable = dialog.contentTable.getChild(0) as Table
            val faceCell = mainTable.getCell(faceImage)
            faceImage = Utils.getFaceImage(graph.getCurrentFace())
            faceCell.setActor<Actor>(faceImage)
        }
    }

    private fun populatePhrase() {
        val text = graph.getCurrentPhrase().joinToString(System.lineSeparator())
        label.setText(text)
    }

    private fun populateChoices() {
        val choices = GdxArray(graph.getAssociatedChoices())
        answers.setItems(choices)
        setDefaultSelectedChoice(choices)
        setScrollPaneHeight(choices)
    }

    private fun setDefaultSelectedChoice(choices: GdxArray<ConversationChoice>) {
        if (choices.size == 1) {
            answers.setSelectedIndex(0)
        } else {
            answers.setSelectedIndex(-1)
        }
    }

    private fun setScrollPaneHeight(choices: GdxArray<ConversationChoice>) {
        if (choices.size % 2 == 0) {
            rowWithScrollPane.height(choices.size * SCROLL_PANE_LINE_HEIGHT)
        } else {
            rowWithScrollPane.height(choices.size * SCROLL_PANE_LINE_HEIGHT + 2f)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun selectAnswer() {
        val selectedAnswer = answers.selected
        if (!selectedAnswer.isMeetingCondition()) {
            audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR)
            return
        }
        val destinationId = selectedAnswer.destinationId
        val conversationCommand = selectedAnswer.conversationCommand

        when (conversationCommand) {
            ConversationCommand.NONE -> continueConversation(destinationId)
            ConversationCommand.EXIT_CONVERSATION -> endConversation(destinationId)
            ConversationCommand.HERO_JOIN -> tryToAddHeroToParty(destinationId)
            ConversationCommand.HERO_DISMISS -> dismissHero(destinationId)
            ConversationCommand.LOAD_SHOP -> loadShop(destinationId)
            ConversationCommand.SAVE_GAME -> saveGame(destinationId)
            ConversationCommand.ACCEPT_QUEST -> acceptQuest()
            ConversationCommand.KNOW_QUEST -> knowQuest(destinationId)
            ConversationCommand.TOLERATE_QUEST -> tolerateQuest()
            ConversationCommand.RECEIVE_ITEM -> receiveItem(destinationId)
            ConversationCommand.CHECK_IF_LINKED_QUEST_KNOWN -> checkIfLinkedQuestKnown(destinationId)
            ConversationCommand.CHECK_IF_QUEST_ACCEPTED -> checkIfQuestAccepted(destinationId)
            ConversationCommand.CHECK_IF_IN_INVENTORY -> checkIfInInventory(destinationId)
            ConversationCommand.COMPLETE_QUEST_TASK -> completeTask(destinationId)
            ConversationCommand.RETURN_QUEST -> returnQuest()
            ConversationCommand.REWARD_QUEST -> rewardQuest()
            ConversationCommand.BONUS_REWARD_QUEST -> bonusRewardQuest()
            ConversationCommand.FAIL_QUEST -> failQuest(destinationId)
            else -> throw IllegalArgumentException("ConversationCommand '$conversationCommand' cannot be reached here.")
        }
    }

    private fun tryToAddHeroToParty(destinationId: String) {
        val heroes = gameData.heroes
        val party = gameData.party
        val hero = heroes.getCertainHero(faceId!!)

        if (party.isFull) {
            continueConversation(Constant.PHRASE_ID_PARTY_FULL)
        } else {
            audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_JOIN)
            heroes.removeHero(faceId!!)
            party.addHero(hero)
            conversationObservers.notifyHeroJoined()
            endConversationWithoutSound(destinationId)
        }
    }

    private fun dismissHero(destinationId: String) {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_END)
        graph.currentPhraseId = destinationId
        conversationObservers.notifyHeroDismiss()                                       // ends conversation
    }

    private fun loadShop(destinationId: String) {
        graph.currentPhraseId = destinationId
        conversationObservers.notifyLoadShop()                                          // ends conversation
    }

    private fun saveGame(destinationId: String) {
        profileManager.saveProfile()
        continueConversation(destinationId)
    }

    private fun acceptQuest() {
        val quest = gameData.quests.getQuestById(conversationId!!)
        quest.handleAccept({ continueConversation(it) }, conversationObservers)         // sets new phraseId
    }

    private fun knowQuest(destinationId: String) {
        val quest = gameData.quests.getQuestById(conversationId!!)
        quest.know()
        continueConversation(destinationId)
    }

    private fun tolerateQuest() {
        val quest = gameData.quests.getQuestById(conversationId!!)
        quest.handleTolerate(conversationObservers)
        continueConversation(Constant.PHRASE_ID_QUEST_TOLERATE)
    }

    private fun receiveItem(destinationId: String) {
        graph.currentPhraseId = destinationId
        val quest = gameData.quests.getQuestById(conversationId!!)
        quest.handleReceive(conversationObservers)                      // ends conversation, sets possible new phraseId
    }

    private fun checkIfLinkedQuestKnown(destinationId: String) {
        val quest = gameData.quests.getQuestById(conversationId!!)
        quest.handleCheckIfLinkedIsKnown(destinationId) { continueConversation(it) }    // sets possible new phraseId
    }

    private fun checkIfQuestAccepted(destinationId: String) {
        val questId = conversationId!!.substring(0, conversationId!!.length - 2)
        val quest = gameData.quests.getQuestById(questId)
        quest.handleCheckIfAccepted(destinationId, { continueConversation(it) }, { endConversation(it) })
    }

    private fun checkIfInInventory(destinationId: String) {
        val questId = conversationId!!.substring(0, conversationId!!.length - 2)
        val quest = gameData.quests.getQuestById(questId)
        val questTaskId = conversationId!!.substring(conversationId!!.length - 1)
        quest.handleCheckIfAcceptedInventory(
            questTaskId, destinationId, { continueConversation(it) }, { endConversation(it) })
    }

    private fun completeTask(destinationId: String) {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_REWARD)
        val questId = conversationId!!.substring(0, conversationId!!.length - 2)
        val quest = gameData.quests.getQuestById(questId)
        val questTaskId = conversationId!!.substring(conversationId!!.length - 1)
        quest.setTaskComplete(questTaskId)
        continueConversationWithoutSound(destinationId)
    }

    private fun returnQuest() {
        val quest = gameData.quests.getQuestById(conversationId!!)
        quest.handleReturn { continueConversation(it) }                                 // sets new phraseId
    }

    private fun bonusRewardQuest() {
        gameData.loot.getLoot(conversationId!!).handleBonus()
        rewardQuest()
    }

    private fun rewardQuest() {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_REWARD)
        graph.currentPhraseId = Constant.PHRASE_ID_QUEST_UNCLAIMED
        val quest = gameData.quests.getQuestById(conversationId!!)
        quest.handleReward({ endConversationWithoutSound(it) }, conversationObservers)  // ends conversation, sets possible new phraseId
    }

    private fun failQuest(destinationId: String) {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_QUEST_FAIL)
        val quest = gameData.quests.getQuestById(conversationId!!)
        quest.handleFail(conversationObservers)
        continueConversation(destinationId)
    }

}