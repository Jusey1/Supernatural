package net.salju.supernatural.init;

import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

public class SupernaturalTags {
	public static final TagKey KOBOLD = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge:kobolds"));
	public static final TagKey SPOOKY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("supernatural:spook_targets"));
	public static final TagKey MERRY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("supernatural:mer_targets"));
	public static final TagKey VAMPIRE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("supernatural:is_vampire"));
	public static final TagKey WEREWOLF = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("supernatural:is_werewolf"));
	public static final TagKey GRAND = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("supernatural:grand_soul"));
	public static final TagKey IMMUNITY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("supernatural:immunity"));
}