package components;

import jade.Window;
import util.AssetPool;

public class BreakableBrick extends Block {
    @Override
    void playerHit(PlayerController playerController) {
        if (playerController.isSmall()) {
            AssetPool.getSound("assets/sounds/bump.ogg").play();
        } else {
            AssetPool.getSound("assets/sounds/break_block.ogg").play();
            gameObject.destroy();
        }
    }
}
