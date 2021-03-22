package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.SkillItem;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.inventory.tooltip.PersonalityTooltip;

import java.util.List;
import java.util.stream.IntStream;


class SkillsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 48f;
    private static final float SECOND_COLUMN_WIDTH = 136f;
    private static final float THIRD_COLUMN_WIDTH = 40f;
    private static final float FOURTH_COLUMN_WIDTH = 35f;
    private static final float CONTAINER_HEIGHT = 704f;
    private static final float ROW_HEIGHT = 48f;
    private static final float SECOND_COLUMN_PAD_LEFT = 15f;

    final ScrollPane scrollPane;

    SkillsTable(PersonalityTooltip tooltip) {
        super(tooltip);
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        this.table.columnDefaults(3).width(FOURTH_COLUMN_WIDTH);
        this.table.top();
        this.table.defaults().height(ROW_HEIGHT);

        this.scrollPane = new ScrollPane(this.table);
        this.container.add(this.scrollPane).height(CONTAINER_HEIGHT);
        this.container.setBackground(Utils.createTopBorder());
        this.container.addListener(new ListenerKeyVertical(this::updateIndex));
    }

    @Override
    public void selectCurrentSlot() {
        super.selectCurrentSlot();
        if (selectedIndex >= selectedHero.getAllSkillsAboveZero().size()) {
            selectedIndex = selectedHero.getAllSkillsAboveZero().size() - 1;
        }
    }

    @Override
    protected void fillRows() {
        List<SkillItem> skillItemList = selectedHero.getAllSkillsAboveZero();
        IntStream.range(0, skillItemList.size())
                 .forEach(i -> fillRow(skillItemList.get(i), i));
    }

    private void updateIndex(int deltaIndex) {
        selectedIndex += deltaIndex;
        if (selectedIndex < 0) {
            selectedIndex = selectedHero.getAllSkillsAboveZero().size() - 1;
        } else if (selectedIndex >= selectedHero.getAllSkillsAboveZero().size()) {
            selectedIndex = 0;
        }
        hasJustUpdated = true;
    }

    private void fillRow(SkillItem skillItem, int index) {
        table.add(createImageOf(skillItem.getId().name()));
        final var skillName = new Label(skillItem.getName(), new Label.LabelStyle(font, Color.BLACK));
        table.add(skillName).padLeft(SECOND_COLUMN_PAD_LEFT);
        if (table.hasKeyboardFocus() && index == selectedIndex) {
            setSelected(skillName, skillItem);
        }
        table.add(String.valueOf(skillItem.getRank()));
        final int totalExtra = selectedHero.getExtraSkillForVisualOf(skillItem);
        addToTable(totalExtra);
    }

    private void setSelected(Label skillName, SkillItem skillItem) {
        skillName.getStyle().fontColor = Constant.DARK_RED;
        if (hasJustUpdated) {
            hasJustUpdated = false;
            tooltip.setPosition(FIRST_COLUMN_WIDTH + SECOND_COLUMN_WIDTH, this::getTooltipY);
            tooltip.refresh(skillName, skillItem);
        }
    }

    private float getTooltipY() {
        return CONTAINER_HEIGHT - (ROW_HEIGHT * selectedIndex) - (ROW_HEIGHT * 0.5f);
    }

}
