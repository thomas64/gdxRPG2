package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.components.party.SkillType;


class SkillsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 64f;
    private static final float SECOND_COLUMN_WIDTH = 140f;
    private static final float THIRD_COLUMN_WIDTH = 40f;
    private static final float FOURTH_COLUMN_WIDTH = 30f;
    private static final float FIFTH_COLUMN_WIDTH = 30f;
    private static final float CONTAINER_HEIGHT = 704f;
    private static final float ROW_HEIGHT = 64f;
    private static final float SECOND_COLUMN_PAD_LEFT = 20f;

    final Table container;
    final ScrollPane scrollPane;

    private HeroItem selectedHero;
    private InventoryItem hoveredItem;
    private int ownSkill;
    private int totalBonus;
    private int difference;

    SkillsTable() {
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        this.table.columnDefaults(3).width(FOURTH_COLUMN_WIDTH);
        this.table.columnDefaults(4).width(FIFTH_COLUMN_WIDTH);
        this.table.top();
        this.table.defaults().height(ROW_HEIGHT);

        this.scrollPane = new ScrollPane(this.table);
        this.container = new Table();
        this.container.add(this.scrollPane).height(CONTAINER_HEIGHT);
        setTopBorder(this.container);
    }

    void render() {
        selectedHero = InventoryUtils.selectedHero;
        hoveredItem = InventoryUtils.hoveredItem;
        table.clear();
        fillRows();
    }

    private void fillRows() {
        for (SkillType skillType : selectedHero.getAllSkillsAboveZero()) {
            ownSkill = selectedHero.getOwnSkillOf(skillType);
            totalBonus = selectedHero.getExtraSkillForVisualOf(skillType);
            difference = selectedHero.getPreviewSkillForVisualOf(skillType, hoveredItem);
            fillRow(skillType);
        }
    }

    private void fillRow(SkillType skillType) {
        table.add(createImageOf(skillType));
        table.add(skillType.getTitle()).padLeft(SECOND_COLUMN_PAD_LEFT);
        table.add(String.valueOf(ownSkill));
        createBonusFromInventory(totalBonus);
        createPreview(difference);
    }

    private Image createImageOf(SkillType skillType) {
        var textureRegion = Utils.getResourceManager().getAtlasTexture(skillType.name().toLowerCase());
        var image = new Image(textureRegion);
        image.setScaling(Scaling.none);
        return image;
    }

}
