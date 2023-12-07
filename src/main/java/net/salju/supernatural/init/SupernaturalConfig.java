package net.salju.supernatural.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class SupernaturalConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec CONFIG;

	public static final ForgeConfigSpec.DoubleValue WOOD;
	public static final ForgeConfigSpec.IntValue DAMAGE;
	public static final ForgeConfigSpec.IntValue SPEED;
	public static final ForgeConfigSpec.IntValue LEECH;
	public static final ForgeConfigSpec.BooleanValue SUN;
	public static final ForgeConfigSpec.BooleanValue RAIDERS;

	public static final ForgeConfigSpec.IntValue SOULPOWER;
	public static final ForgeConfigSpec.BooleanValue CONTRACT;
	public static final ForgeConfigSpec.BooleanValue VAMPIRISM;
	public static final ForgeConfigSpec.BooleanValue VEXATION;
	public static final ForgeConfigSpec.BooleanValue PUMPKIN;
	public static final ForgeConfigSpec.BooleanValue REANIMATE;
	public static final ForgeConfigSpec.BooleanValue KNOWLEDGE;
	public static final ForgeConfigSpec.BooleanValue FORTUNE;
	public static final ForgeConfigSpec.BooleanValue SACRIFICE;

	public static final ForgeConfigSpec.BooleanValue VEX;
	public static final ForgeConfigSpec.BooleanValue ARMOR;
	public static final ForgeConfigSpec.BooleanValue FURIA;
	
	static {
		BUILDER.push("Vampirism");
		RAIDERS = BUILDER.comment("Should vampires spawn within raids at night?").define("Vampiric Raiders", true);
		DAMAGE = BUILDER.comment("How much extra damage does a Vampire do?").defineInRange("Vampiric Damage", 3, 0, Integer.MAX_VALUE);
		SPEED = BUILDER.comment("How much extra speed does a Vampire have in percentage?").defineInRange("Vampiric Speed", 15, 0, Integer.MAX_VALUE);
		LEECH = BUILDER.comment("How strong is the heal effect for the Leeching Enchantment?").defineInRange("Vampiric Leech", 1, 0, Integer.MAX_VALUE);
		SUN = BUILDER.comment("Should vampires be immune to sun damage?").define("Vampiric Sunlight", false);
		WOOD = BUILDER.comment("How much health does a Vampire needs to be at in percentage to die instantly to a Wooden Sword?").defineInRange("Vampiric Weakness", 0.6, 0.0, 1.0);
		BUILDER.pop();
		BUILDER.push("Ritual Altar");
		SOULPOWER = BUILDER.comment("What should be the base power for soul enchanting?").defineInRange("Soulpower", 7, 0, Integer.MAX_VALUE);
		CONTRACT = BUILDER.comment("Should contracts have a full description instead of a short & simple one?").define("Contracts", true);
		VAMPIRISM = BUILDER.comment("Should the Vampirism Contract be Enabled? Note: This doesn't remove the item.").define("Vampirism", true);
		VEXATION = BUILDER.comment("Should the Vexation Contract be Enabled? Note: This doesn't remove the item.").define("Vexation", true);
		PUMPKIN = BUILDER.comment("Should the Pumpkin Head Contract be Enabled? Note: This doesn't remove the item.").define("Pumpkin", true);
		REANIMATE = BUILDER.comment("Should the Reanimation Contract be Enabled? Note: This doesn't remove the item.").define("Reanimation", true);
		KNOWLEDGE = BUILDER.comment("Should the Knowledge Contract be Enabled? Note: This doesn't remove the item.").define("Knowledge", true);
		FORTUNE = BUILDER.comment("Should the Fortune Contract be Enabled? Note: This doesn't remove the item.").define("Fortune", true);
		SACRIFICE = BUILDER.comment("Should the Reanimate Contract require a sacrifice?").define("Ritual Sacrifice", true);
		BUILDER.pop();
		BUILDER.push("Misc");
		VEX = BUILDER.comment("Should Vexes spawn from Grave Soil?").define("Gravy Vexes", true);
		ARMOR = BUILDER.comment("Should Spooks target armor stands?").define("Possessed Armor", true);
		FURIA = BUILDER.comment("Should Angels have a holy aura that burns nearby vampires and undead?").define("Holy Blessing", true);
		BUILDER.pop();
		CONFIG = BUILDER.build();
	}
}