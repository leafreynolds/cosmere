/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.surgebinding.client.render.model.ShardbladeModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class DynamicShardbladeData extends BondableRadiantShardData implements ICapabilityProvider, INBTSerializable<CompoundTag>, IShardbladeDynamicData
{
	private String bladeID;
	private String handleID;
	private String pommelID;
	private String crossGuardID;

	public DynamicShardbladeData(ItemStack stack)
	{
		super(stack);

		this.bladeID = "blade_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_BLADE_IDS);
		this.handleID = "handle_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_HANDLE_IDS);
		this.pommelID = "pommel_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_POMMEL_IDS);
		this.crossGuardID = "crossguard_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_CROSS_GUARD_IDS);
	}

	@Override
	public CompoundTag serializeNBT()
	{
		if (super.nbt == null)
		{
			super.nbt = new CompoundTag();
		}

		super.serializeNBT();

		super.nbt.putString("bladeID", this.bladeID);
		super.nbt.putString("handleID", this.handleID);
		super.nbt.putString("pommelID", this.pommelID);
		super.nbt.putString("crossguardID", this.crossGuardID);

		return super.nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		super.deserializeNBT(nbt);

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
