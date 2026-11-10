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

	public static final ModConfigSpec.IntValue SOULPOWER;
	public static final ModConfigSpec.BooleanValue FURIA;

    static {
        BUILDER.push("Supernatural");
        DAMAGE = BUILDER.comment("Extra damage that a Vampyre can do").defineInRange("Damage", 3, 0, Integer.MAX_VALUE);
        SPEED = BUILDER.comment("Extra speed that a Vampyre have in percentage").defineInRange("Speed", 15, 0, Integer.MAX_VALUE);
        RAIDERS = BUILDER.comment("Should Vampyres spawn within raids at night?").define("Raider", true);
        VAMPIRER = BUILDER.comment("Chance of a Vindicator being replaced with a Vampyre during a raid at night?").defineInRange("Vindicator", 0.25, 0.0, 1.0);
        LEECH = BUILDER.comment("How strong is the heal effect for Vampyres when they attack valid targets?").defineInRange("Leech", 1.0, 0.0, Double.MAX_VALUE);
        SUN = BUILDER.comment("Should Vampyres be immune to sun damage?").define("Sunlight", false);
        WOOD = BUILDER.comment("How much health does a Vampyre needs to be at in percentage to die instantly to a Wooden Sword?").defineInRange("Weakness", 0.6, 0.0, 1.0);
        DR = BUILDER.comment("How much damage does a Vampyre takes from all sources?").defineInRange("DR", 0.85, 0.0, 1.0);
        BUILDER.pop();
        BUILDER.push("Misc");
        SOULPOWER = BUILDER.comment("Base Power for soul enchanting").defineInRange("Ritual-Soulpower", 3, 0, Integer.MAX_VALUE);
        FURIA = BUILDER.comment("Should Angels have a holy aura that burns nearby undead?").define("Holy-Blessing", true);
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }
}