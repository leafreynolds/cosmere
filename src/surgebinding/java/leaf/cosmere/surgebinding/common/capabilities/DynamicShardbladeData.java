/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.surgebinding.client.render.model.ShardbladeModel;
import leaf.cosmere.surgebinding.common.items.ShardbladeDynamicItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DynamicShardbladeData implements ICapabilityProvider, INBTSerializable<CompoundTag>, IShardbladeDynamicData
{
	private final LazyOptional<IShardbladeDynamicData> opt = LazyOptional.of(() -> this);

	private CompoundTag nbt;

	private String bladeID;
	private String handleID;
	private String pommelID;
	private String crossGuardID;

	public DynamicShardbladeData()
	{
		this.nbt = new CompoundTag();

		this.bladeID = "blade_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_BLADE_IDS);
		this.handleID = "handle_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_HANDLE_IDS);
		this.pommelID = "pommel_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_POMMEL_IDS);
		this.crossGuardID = "crossguard_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_CROSS_GUARD_IDS);
	}


	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		return ShardbladeDynamicItem.CAPABILITY.orEmpty(capability, opt);
	}

	@Override
	public CompoundTag serializeNBT()
	{
		if (this.nbt == null)
		{
			this.nbt = new CompoundTag();
		}

		this.nbt.putString("bladeID", this.bladeID);
		this.nbt.putString("handleID", this.handleID);
		this.nbt.putString("pommelID", this.pommelID);
		this.nbt.putString("crossguardID", this.crossGuardID);

		return this.nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.nbt = nbt;

		this.bladeID = nbt.getString("bladeID");
		this.handleID = nbt.getString("handleID");
		this.pommelID = nbt.getString("pommelID");
		this.crossGuardID = nbt.getString("crossguardID");
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