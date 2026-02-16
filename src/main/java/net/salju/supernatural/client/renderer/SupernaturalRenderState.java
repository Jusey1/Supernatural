package net.salju.supernatural.client.renderer;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class SupernaturalRenderState extends HumanoidRenderState {
    public String type;
	public boolean isAggressive;
    public boolean isCastingSpell;
    public boolean isCharging;
    public boolean isInvisible;
	public boolean isLeftHanded;
    public boolean isSwimming;
    public boolean onGround;
    public int pose;
}