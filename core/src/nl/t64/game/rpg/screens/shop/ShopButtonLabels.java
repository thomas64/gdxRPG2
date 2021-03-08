package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.scenes.scene2d.Stage;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.inventory.ButtonLabels;


class ShopButtonLabels extends ButtonLabels {

    ShopButtonLabels(Stage stage) {
        super(stage);
    }

    @Override
    public String createBottomLeftText() {
        if (Utils.isGamepadConnected()) {
            return "Buy / Sell:      [A] One      [X] Half      [Y] Full      |      [Start] De/Equip";
        } else {
            return "Buy / Sell:      [A] One      [S] Half      [D] Full      |      [E] De/Equip";
        }
    }

    @Override
    public String createBottomRightText() {
        if (Utils.isGamepadConnected()) {
            return "[Select] Toggle tooltip      [L3] Toggle compare      [B] Back";
        } else {
            return "[T] Toggle tooltip      [C] Toggle compare      [Esc] Back";
        }
    }

}
