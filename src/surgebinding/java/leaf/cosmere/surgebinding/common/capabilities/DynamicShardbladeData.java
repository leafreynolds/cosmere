/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.surgebinding.client.render.model.ShardbladeModel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class DynamicShardbladeData implements IShardbladeDynamicData
{
	private String bladeID;
	private String handleID;
	private String pommelID;
	private String crossGuardID;

	public DynamicShardbladeData()
	{
		this.bladeID = "blade_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_BLADE_IDS);
		this.handleID = "handle_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_HANDLE_IDS);
		this.pommelID = "pommel_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_POMMEL_IDS);
		this.crossGuardID = "crossguard_" + MathHelper.randomInt(1, ShardbladeModel.TOTAL_CROSS_GUARD_IDS);
	}

	private DynamicShardbladeData(CompoundTag nbt)
	{
		this.bladeID = nbt.getString("bladeID");
		this.handleID = nbt.getString("handleID");
		this.pommelID = nbt.getString("pommelID");
		this.crossGuardID = nbt.getString("crossguardID");
	}

	public static IShardbladeDynamicData fromStack(ItemStack stack)
	{
		CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		if (tag.contains("bladeID"))
		{
			return new DynamicShardbladeData(tag);
		}
		// Generate and persist new data
		DynamicShardbladeData data = new DynamicShardbladeData();
		data.saveToStack(stack);
		return data;
	}

	public void saveToStack(ItemStack stack)
	{
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag ->
		{
			tag.putString("bladeID", bladeID);
			tag.putString("handleID", handleID);
			tag.putString("pommelID", pommelID);
			tag.putString("crossguardID", crossGuardID);
		});
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
