package tactics.model;

public enum UnitTypes {
    //PARAMS: hitpoints, attack, range, movement, spritepacks/Characters folder name
    KNIGHT(30, 5, 1, 4, "Knight"),
    MAGE(20, 10, 4, 3, "Wizard"),
    RANGER(25, 7, 5, 4, "Archer"),

    ORC(30, 5, 1, 4, "Orc"),
    NECROMANCER(20, 10, 4, 3, "Necromancer"),
    SKELETON_ARCHER(25, 7, 5, 4, "Skeleton Archer"),

    ARMORED_AXEMAN(35, 6, 1, 3, "Armored Axeman"),
    BAT(12, 4, 1, 6, "Bat"),
    ARMORED_ORC(35, 6, 1, 3, "Armored Orc"),
    ARMORED_SKELETON(35, 5, 1, 3, "Armored Skeleton"),
    ELITE_ORC(35, 7, 1, 4, "Elite Orc"),
    GREATSWORD_SKELETON(30, 8, 1, 3, "Greatsword Skeleton"),
    KNIGHT_TEMPLAR(35, 6, 1, 4, "Knight Templar"),
    LANCER(28, 6, 2, 4, "Lancer"),
    ORC_RIDER(30, 6, 1, 6, "Orc rider"),
    PRIEST(18, 4, 3, 3, "Priest"),
    SKELETON(20, 4, 1, 4, "Skeleton"),
    SLIME(15, 3, 1, 2, "Slime"),
    SOLDIER(25, 5, 1, 4, "Soldier"),
    SWORDSMAN(28, 5, 1, 4, "Swordsman"),
    WEREBEAR(40, 8, 1, 4, "Werebear"),
    WEREWOLF(25, 7, 1, 5, "Werewolf");

    public final int hitpoints;
    public final int attack;
    public final int range;
    public final int movement;
    public final String spriteFolder;

    UnitTypes(int hitpoints, int attack, int range, int movement, String spriteFolder) {
        this.hitpoints = hitpoints;
        this.attack = attack;
        this.range = range;
        this.movement = movement;
        this.spriteFolder = spriteFolder;
    }
}
