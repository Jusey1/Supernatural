package net.salju.supernatural.init;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

public class SupernaturalConfig {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final IConfigSpec CONFIG;

	public static final ModConfigSpec.IntValue DAMAGE;
	public static final ModConfigSpec.IntValue SPEED;
	public static final ModConfigSpec.BooleanValue RAIDERS;
	public static final ModConfigSpec.DoubleValue VAMPIRER;
	public static final ModConfigSpec.DoubleValue LEECH;
	public static final ModConfigSpec.BooleanValue SUN;
	public static final ModConfigSpec.DoubleValue WOOD;
	public static final ModConfigSpec.DoubleValue DR;
	public static final ModConfigSpec.BooleanValue BLOOD;
	public static final ModConfigSpec.DoubleValue ATTACKED;
	public static final ModConfigSpec.DoubleValue BITE;

	public static final ModConfigSpec.IntValue SOULPOWER;
	public static final ModConfigSpec.BooleanValue SACRIFICE;
	public static final ModConfigSpec.DoubleValue ALTARRANGE;

	public static final ModConfigSpec.BooleanValue VEX;
	public static final ModConfigSpec.BooleanValue ARMOR;
	public static final ModConfigSpec.BooleanValue FURIA;

	static {
		BUILDER.push("Supernatural");
		DAMAGE = BUILDER.comment("How much extra damage does a vampire do?").defineInRange("Vampiric Damage", 3, 0, Integer.MAX_VALUE);
		SPEED = BUILDER.comment("How much extra speed does a vampire have in percentage?").defineInRange("Vampiric Speed", 15, 0, Integer.MAX_VALUE);
		RAIDERS = BUILDER.comment("Should vampires spawn within raids at night?").define("Vampiric Raiders", true);
		VAMPIRER = BUILDER.comment("Chance of a Vindicator being replaced with a Vampire during a raid at night?").defineInRange("Vampiric Vindicator", 0.25, 0.0, 1.0);
		LEECH = BUILDER.comment("How strong is the heal effect for vampires when they attack valid targets?").defineInRange("Vampiric Leech", 1.0, 0.0, Double.MAX_VALUE);
		SUN = BUILDER.comment("Should vampires be immune to sun damage?").define("Vampiric Sunlight", false);
		WOOD = BUILDER.comment("How much health does a Vampire needs to be at in percentage to die instantly to a Wooden Sword?").defineInRange("Vampiric Weakness", 0.6, 0.0, 1.0);
		DR = BUILDER.comment("How much damage does a Vampire takes from all sources?").defineInRange("Vampiric Damage Reduction", 0.85, 0.0, 1.0);
		BLOOD = BUILDER.comment("Should blood bottles be obtainable by attacking Tagged Mobs & Players?").define("Blood Bottles", true);
		ATTACKED = BUILDER.comment("Chance of being infected by a Vampire Illager's attack?").defineInRange("Vampiric Attack Infection", 0.15, 0.0, 1.0);
		BITE = BUILDER.comment("Chance of being infected by a Vampire Player's bite?").defineInRange("Vampiric Bite Infection", 0.35, 0.0, 1.0);
		BUILDER.pop();
		BUILDER.push("Ritual Altar");
		SOULPOWER = BUILDER.comment("What should be the base power for soul enchanting?").defineInRange("Ritual Soulpower", 3, 0, Integer.MAX_VALUE);
		SACRIFICE = BUILDER.comment("Should Rituals require sacrifices?").define("Ritual Sacrifice", true);
		ALTARRANGE = BUILDER.comment("What is the Ritual Altar's range for rituals?").defineInRange("Ritual Range", 12.85, 0.0, Double.MAX_VALUE);
		BUILDER.pop();
		BUILDER.push("Misc");
		VEX = BUILDER.comment("Should Vexes spawn from Grave Soil?").define("Gravy Vexes", true);
		ARMOR = BUILDER.comment("Should Spooks target armor stands?").define("Possessed Armor", true);
		FURIA = BUILDER.comment("Should Angels have a holy aura that burns nearby vampires and undead?").define("Holy Blessing", true);
		BUILDER.pop();
		CONFIG = BUILDER.build();
	}
}