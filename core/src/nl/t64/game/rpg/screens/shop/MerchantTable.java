package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.inventory.BaseTable;


class MerchantTable extends BaseTable {

    private static final float TABLE_WIDTH = 390f;
    private static final float TABLE_HEIGHT = 501f;
    private static final float PADDING = 20f;
    private static final String TEXT = """
            Good day sir, and welcome to my shop. 
            
            On the left you will find my wares, and on the right is your inventory.
            
            You can buy or sell 1 item, half a stack, or the full stack at once. 
            
            Payment will happen immediately.""";

    MerchantTable(String npcId) {
        this.table.defaults().reset();
        this.table.defaults().size(TABLE_WIDTH, TABLE_HEIGHT);
        this.table.pad(PADDING);
        this.table.add(Utils.getFaceImage(npcId)).left().size(Constant.FACE_SIZE);
        this.table.row();
        var text = new Label(TEXT, this.table.getSkin());
        text.setWrap(true);
        text.setAlignment(Align.topLeft);
        this.table.add(text).padTop(PADDING);
        this.table.setBackground(Utils.createTopBorder());
    }

    @Override
    protected void update() {
        // empty
    }

}
