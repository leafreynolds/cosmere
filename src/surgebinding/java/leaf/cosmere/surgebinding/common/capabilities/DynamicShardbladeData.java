/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.surgebinding.client.render.model.ShardbladeModel;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class DynamicShardbladeData extends BondableRadiantShardData implements INBTSerializable<CompoundTag>, IShardbladeDynamicData
{
	private String bladeID;
	private String handleID;
	private String pommelID;
	private String crossGuardID;

	public DynamicShardbladeData(ItemStack stack)
	{
		super(stack);

		//deterministic until the server rolls an appearance, so both sides read the same unseeded blade
		setAppearance(1, 1, 1, 1);
	}

	@Override
	protected void randomiseAppearance()
	{
		setAppearance(
				MathHelper.randomInt(1, ShardbladeModel.TOTAL_BLADE_IDS),
				MathHelper.randomInt(1, ShardbladeModel.TOTAL_HANDLE_IDS),
				MathHelper.randomInt(1, ShardbladeModel.TOTAL_POMMEL_IDS),
				MathHelper.randomInt(1, ShardbladeModel.TOTAL_CROSS_GUARD_IDS));
	}

	private void setAppearance(int blade, int handle, int pommel, int crossGuard)
	{
		this.bladeID = "blade_" + blade;
		this.handleID = "handle_" + handle;
		this.pommelID = "pommel_" + pommel;
		this.crossGuardID = "crossguard_" + crossGuard;
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		if (super.nbt == null)
		{
			super.nbt = new CompoundTag();
		}

		super.serializeNBT(provider);

		super.nbt.putString("bladeID", this.bladeID);
		super.nbt.putString("handleID", this.handleID);
		super.nbt.putString("pommelID", this.pommelID);
		super.nbt.putString("crossguardID", this.crossGuardID);

		return super.nbt;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
	{
		super.deserializeNBT(provider, nbt);

		this.bladeID = super.nbt.getString("bladeID");
		this.handleID = super.nbt.getString("handleID");
		this.pommelID = super.nbt.getString("pommelID");
		this.crossGuardID = super.nbt.getString("crossguardID");
	}

	@Override
	public String getBladeID()
	{
		return bladeID;
	}

	@Override
	public String getHandleID()
	{
		return handleID;
	}

	@Override
	public String getPommelID()
	{
		return pommelID;
	}

	@Override
	public String getCrossGuardID()
	{
		return crossGuardID;
	}
}
