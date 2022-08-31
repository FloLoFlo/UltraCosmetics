package be.isach.ultracosmetics.abstraction.worldguard;

import com.sk89q.worldguard.protection.flags.StateFlag;

public enum UCFlag {
    COSMETICS(new StateFlag("uc-treasurechest", true)),
    TREASURE(new StateFlag("uc-cosmetics", true)),
    AFFECT_PLAYERS(new StateFlag("uc-affect-players", true)),
    ;

    private final StateFlag flag;

    private UCFlag(StateFlag flag) {
        this.flag = flag;
    }

    public StateFlag getFlag() {
        return flag;
    }
}
