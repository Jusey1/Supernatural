package net.salju.supernatural.events;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.Event;

public class GothicEvent extends Event {
    private final Item original;
    private Item gothic = Items.AIR;

	public GothicEvent(Item target) {
        this.original = target;
    }

    public Item getOriginal() {
        return this.original;
    }

    public Item getHelmet() {
        return this.gothic;
    }

    public void setHelmet(Item target) {
        this.gothic = target;
    }
}