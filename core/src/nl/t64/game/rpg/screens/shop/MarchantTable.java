package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.inventory.BaseTable;


class MarchantTable extends BaseTable {

    private static final float TABLE_WIDTH = 594f;
    private static final float TABLE_HEIGHT = 501f;
    private static final float PADDING = 20f;
    private static final String TEXT =
            "Good day sir, and welcome to my shop. " +
            "On the left you will find my wares, and on the right is your own current inventory." +
            System.lineSeparator() + System.lineSeparator() +
            "To buy something, simply drag an item from the left box to the right box. " +
            "Payment will happen immediately." +
            System.lineSeparator() +
            "Press Shift while dragging, and you will purchase the whole stack. " +
            "Press Ctrl while dragging, and you will purchase only half of the stack. " +
            "When you press nothing, you will purchase only 1 item from the stack." +
            System.lineSeparator() +
            "You can also double-click on an item in the left box. That way you'll also purchase 1 item. " +
            "Combine a double-click with Shift or Ctrl, and the same will happen as with dragging." +
            System.lineSeparator() + System.lineSeparator() +
            "But perhaps you are here to sell some of your items. That is also possible at my shop. " +
            "You'll just have to drag from the right box to the left box. " +
            "Of course Shift and Ctrl also work for selling. " +
            "Remember, payment will happen immediately.";

    MarchantTable(String npcId) {
        this.table.defaults().reset();
        this.table.defaults().size(TABLE_WIDTH, TABLE_HEIGHT);
        this.table.pad(PADDING);
        this.table.add(Utils.getFaceImage(npcId)).left().size(Constant.FACE_SIZE);
        this.table.row();
        var text = new Label(TEXT, this.table.getSkin());
        text.setWrap(true);
        text.setAlignment(Align.topLeft);
        this.table.add(text).padTop(PADDING);
        setTopBorder(this.table);
    }

    @Override
    protected void update() {
        // empty
    }

}
