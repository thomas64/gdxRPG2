package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.SpellItem;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.inventory.tooltip.PersonalityTooltip;

import java.util.List;
import java.util.stream.IntStream;


class SpellsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 48f;
    private static final float SECOND_COLUMN_WIDTH = 210;
    private static final float THIRD_COLUMN_WIDTH = 40f;
    private static final float FOURTH_COLUMN_WIDTH = 30f;
    private static final float CONTAINER_HEIGHT = 704f;
    private static final float ROW_HEIGHT = 48f;
    private static final float SECOND_COLUMN_PAD_LEFT = 15f;

    final ScrollPane scrollPane;

    SpellsTable(PersonalityTooltip tooltip) {
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
        if (selectedIndex >= selectedHero.getAllSpells().size()) {
            selectedIndex = selectedHero.getAllSpells().size() - 1;
        } else if (selectedIndex == -1) {
            selectedIndex = 0;
        }
    }

    @Override
    protected void fillRows() {
        fillSchoolRow();
        fillSpellRows();
    }

    private void updateIndex(int deltaIndex) {
        selectedIndex += deltaIndex;
        if (selectedIndex < 0) {
            selectedIndex = selectedHero.getAllSpells().size() - 1;
        } else if (selectedIndex >= selectedHero.getAllSpells().size()) {
            selectedIndex = 0;
        }
        hasJustUpdated = true;
    }

    private void fillSchoolRow() {
        table.add(createImageOf(selectedHero.getSchool().name()));
        table.add(selectedHero.getSchool().getTitle() + " School").padLeft(SECOND_COLUMN_PAD_LEFT);
        table.add("");
        table.add("").row();
        table.add("").row();
    }

    private void fillSpellRows() {
        List<SpellItem> spellList = selectedHero.getAllSpells();
        IntStream.range(0, spellList.size())
                 .forEach(i -> fillSpellRow(spellList.get(i), i));
    }

    private void fillSpellRow(SpellItem spellItem, int index) {
        table.add(createImageOf(spellItem.getId()));
        final var spellName = new Label(spellItem.getName(), new Label.LabelStyle(font, Color.BLACK));
        table.add(spellName).padLeft(SECOND_COLUMN_PAD_LEFT);
        if (table.hasKeyboardFocus() && index == selectedIndex) {
            setSelected(spellName, spellItem);
        }
        table.add(String.valueOf(spellItem.getRank()));
        table.add("").row();
    }

    private void setSelected(Label spellName, SpellItem spellItem) {
        spellName.getStyle().fontColor = Constant.DARK_RED;
        if (hasJustUpdated) {
            hasJustUpdated = false;
            tooltip.setPosition(FIRST_COLUMN_WIDTH + SECOND_COLUMN_WIDTH, this::getTooltipY);
            tooltip.refresh(spellName, spellItem);
        }
    }

    private float getTooltipY() {
        return CONTAINER_HEIGHT - (ROW_HEIGHT * selectedIndex) - (ROW_HEIGHT * 2f) - (ROW_HEIGHT * 0.4f);
    }

}
