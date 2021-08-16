package nl.t64.game.rpg.screens.inventory

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.components.party.HeroItem
import nl.t64.game.rpg.components.party.PartyContainer


private const val SPRITE_BORDER = "sprites/border.png"
private const val SPRITE_RIGHT_BORDER = "sprites/right_border.png"
private const val SPRITE_GRAY = "sprites/gray.png"
private const val FONT_PATH = "fonts/spectral_extra_bold_20.ttf"
private const val FONT_BIG_PATH = "fonts/spectral_extra_bold_28.ttf"
private const val FONT_BIG_SIZE = 28
private const val FONT_SIZE = 20
private const val STATS_COLUMN_WIDTH = 134f
private const val STATS_COLUMN_PAD = 10f
private const val BAR_ROW_HEIGHT = 32f
private const val BAR_HEIGHT = 18f
private const val BAR_PAD_TOP = 7f

class HeroesTable {

    private val nameStyle: LabelStyle
    private val levelStyle: LabelStyle
    private val party: PartyContainer = gameData.party
    private val colorsOfHpBars: Array<Texture?> = arrayOfNulls(PartyContainer.MAXIMUM)
    val heroes = Table().apply {
        background = Utils.createTopBorder()
    }

    init {
        val font = resourceManager.getTrueTypeAsset(FONT_PATH, FONT_SIZE)
        val fontBig = resourceManager.getTrueTypeAsset(FONT_BIG_PATH, FONT_BIG_SIZE)
        nameStyle = LabelStyle(fontBig, Color.BLACK)
        levelStyle = LabelStyle(font, Color.BLACK)
    }

    fun update() {
        heroes.clear()
        party.getAllHeroes().forEach {
            createFace(it)
            createStats(it)
        }
    }

    private fun createFace(hero: HeroItem) {
        val stack = Stack()
        addPossibleGrayBackgroundTo(stack, hero)
        addFaceImageTo(stack, hero)
        heroes.add(stack)
    }

    private fun addFaceImageTo(stack: Stack, hero: HeroItem) {
        val faceImage = Utils.getFaceImage(hero.id)
        if (!hero.isAlive) faceImage.color = Color.DARK_GRAY
        stack.add(faceImage)
    }

    private fun createStats(hero: HeroItem) {
        val statsTable = createStatsTable(hero)
        addNameLabelTo(statsTable, hero)
        addLevelLabelTo(statsTable, hero)
        addHpLabelTo(statsTable, hero)
        addHpBarTo(statsTable, hero)

        val stack = Stack()
        addPossibleGrayBackgroundTo(stack, hero)
        stack.add(statsTable)
        heroes.add(stack)
    }

    private fun createStatsTable(hero: HeroItem): Table {
        return Table().apply {
            defaults().align(Align.left).top()
            columnDefaults(0).width(STATS_COLUMN_PAD)
            columnDefaults(1).width(STATS_COLUMN_WIDTH)
            columnDefaults(2).width(STATS_COLUMN_PAD)
            if (!party.isHeroLast(hero)) setRightBorder(this)
        }
    }

    private fun addPossibleGrayBackgroundTo(stack: Stack, hero: HeroItem) {
        if (hero.hasSameIdAs(InventoryUtils.getSelectedHero())) {
            val textureGray = resourceManager.getTextureAsset(SPRITE_GRAY)
            val imageGray = Image(textureGray)
            stack.add(imageGray)
        }
    }

    private fun addNameLabelTo(statsTable: Table, hero: HeroItem) {
        statsTable.add(Label("", nameStyle))
        val nameLabel = Label(hero.name, nameStyle)
        statsTable.add(nameLabel)
        statsTable.add(Label("", nameStyle)).row()
    }

    private fun addLevelLabelTo(statsTable: Table, hero: HeroItem) {
        statsTable.add(Label("", levelStyle))
        val levelLabel = Label("Level:   " + hero.getLevel(), levelStyle)
        statsTable.add(levelLabel)
        statsTable.add(Label("", levelStyle)).row()
    }

    private fun addHpLabelTo(statsTable: Table, hero: HeroItem) {
        statsTable.add(Label("", levelStyle))
        val hpLabel = Label("HP:  " + hero.getCurrentHp() + "/ " + hero.getMaximumHp(), levelStyle)
        statsTable.add(hpLabel)
        statsTable.add(Label("", levelStyle))
    }

    private fun addHpBarTo(statsTable: Table, hero: HeroItem) {
        statsTable.row().height(BAR_ROW_HEIGHT)
        statsTable.add(Label("", levelStyle))
        statsTable.add(createHpBar(hero)).height(BAR_HEIGHT).padTop(BAR_PAD_TOP)
        statsTable.add(Label("", levelStyle))
    }

    private fun setRightBorder(statsTable: Table) {
        val texture = resourceManager.getTextureAsset(SPRITE_RIGHT_BORDER)
        val ninepatch = NinePatch(texture, 0, 1, 0, 0)
        val drawable = NinePatchDrawable(ninepatch)
        statsTable.background = drawable
    }

    private fun createHpBar(hero: HeroItem): Stack {
        val outline = createOutline()
        val fill = createFill(hero)
        return Stack().apply {
            add(fill)
            add(outline)
        }
    }

    private fun createOutline(): Image {
        val texture = resourceManager.getTextureAsset(SPRITE_BORDER)
        val ninepatch = NinePatch(texture, 1, 1, 1, 1)
        return Image(ninepatch)
    }

    private fun createFill(hero: HeroItem): Image {
        val index = party.getIndex(hero)
        colorsOfHpBars[index]?.dispose()

        val pixmap = Pixmap(1, 1, Pixmap.Format.RGB888)
        val hpStats = hero.getAllHpStats()
        val color = Utils.getHpColor(hpStats)
        pixmap.setColor(color)
        pixmap.fill()
        colorsOfHpBars[index] = Texture(pixmap)
        pixmap.dispose()
        val drawable = TextureRegionDrawable(colorsOfHpBars[index])
        val fillWidth = (STATS_COLUMN_WIDTH / hero.getMaximumHp()) * hero.getCurrentHp()
        drawable.minWidth = fillWidth
        return Image(drawable).apply {
            setScaling(Scaling.stretchY)
            align = Align.left
        }
    }

    fun disposePixmapTextures() {
        colorsOfHpBars.filterNotNull().forEach { it.dispose() }
    }

}
