<<<<<<<< HEAD:v1_20_R3/src/main/java/be/isach/ultracosmetics/v1_20_R3/customentities/CustomSlimeJumpGoal.java
package be.isach.ultracosmetics.v1_20_R3.customentities;
========
package be.isach.ultracosmetics.v1_20_R4.customentities;
>>>>>>>> upstream/master:v1_20_R4/src/main/java/be/isach/ultracosmetics/v1_20_R4/customentities/CustomSlimeJumpGoal.java

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CustomSlimeJumpGoal extends Goal {
    public CustomSlimeJumpGoal() {
        ((Goal) this).setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // TODO: does this class even do anything?
        return false;
    }
}
