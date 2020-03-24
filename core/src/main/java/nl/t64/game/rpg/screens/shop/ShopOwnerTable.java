package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.inventory.BaseTable;


class ShopOwnerTable extends BaseTable {

    private static final float TABLE_WIDTH = 594f;
    private static final float TABLE_HEIGHT = 501f;
    private static final float PADDING = 20f;
    private static final String TEXT =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin fringilla sem id lacus pharetra, " +
            "et pretium lectus pretium. Vivamus iaculis lorem nisi, in suscipit orci varius sed. In gravida " +
            "est non lectus iaculis rhoncus. Cras placerat leo libero. Suspendisse rutrum nulla vel ultrices " +
            "dictum. Nullam ac eleifend augue, luctus gravida enim. Sed efficitur dolor ac sem scelerisque, " +
            "sed euismod velit imperdiet. Nunc et venenatis quam. Mauris posuere nibh ipsum, semper dapibus " +
            "magna luctus eleifend. Sed at congue sapien. Cras varius auctor dictum. Etiam posuere quam eu " +
            "leo aliquet venenatis. Proin rutrum orci dictum, faucibus sem vel, consequat nunc. Maecenas " +
            "scelerisque fringilla metus. Integer eleifend commodo lacinia.";

    ShopOwnerTable(String npcId) {
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
