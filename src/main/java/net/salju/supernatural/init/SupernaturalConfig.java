package net.salju.supernatural.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class SupernaturalConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec CONFIG;

	public static final ForgeConfigSpec.DoubleValue WOOD;
	public static final ForgeConfigSpec.BooleanValue SPEED;
	public static final ForgeConfigSpec.BooleanValue STRENGTH;
	public static final ForgeConfigSpec.BooleanValue HASTE;
	public static final ForgeConfigSpec.BooleanValue SUN;
	public static final ForgeConfigSpec.BooleanValue RAIDERS;

	public static final ForgeConfigSpec.BooleanValue VEX;
	public static final ForgeConfigSpec.BooleanValue ARMOR;

	public static final ForgeConfigSpec.BooleanValue FURIA;
	
	static {
		BUILDER.push("Vampirism");
		WOOD = BUILDER.comment("How much health does a Vampire needs to be finished off by a Wooden Sword?").defineInRange("Wooden Stake Health", 0.6, 0.0, 1.0);
		SPEED = BUILDER.comment("Should vampires have the Speed effect? default is true.").define("Vampire's Speed", true);
		STRENGTH = BUILDER.comment("Should vampires have the Strength effect? default is true.").define("Vampire's Strength", true);
		HASTE = BUILDER.comment("Should vampires have the Haste effect? default is true.").define("Vampire's Haste", true);
		SUN = BUILDER.comment("Should vampires be immune to sun damage? default is false.").define("Twilight Vampires", false);
		RAIDERS = BUILDER.comment("Should vampires be removed from raids at night? default is false.").define("No Raiding Vampires", false);
		BUILDER.pop();
		BUILDER.push("Spirits");
		VEX = BUILDER.comment("Should Vexes spawn from Grave Soil blocks? default is true.").define("Vexation Graves", true);
		ARMOR = BUILDER.comment("Should Spooks target armor stands? default is true.").define("Possessed Armor", true);
		BUILDER.pop();
		BUILDER.push("Angels");
		FURIA = BUILDER.comment("Should Angels have a holy aura that burns nearby vampires and undead? default is true.").define("Holy Blessing", true);
		BUILDER.pop();
		CONFIG = BUILDER.build();
	}
}