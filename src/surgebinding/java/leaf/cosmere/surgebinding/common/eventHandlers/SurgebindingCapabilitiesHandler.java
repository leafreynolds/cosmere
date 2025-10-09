/*
 * File updated ~ 30 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.eventHandlers;

import leaf.cosmere.api.Constants;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.*;
import leaf.cosmere.surgebinding.common.capabilities.world.IRoshar;
import leaf.cosmere.surgebinding.common.capabilities.world.RosharCapability;
import leaf.cosmere.surgebinding.common.items.IShardItem;
import leaf.cosmere.surgebinding.common.items.ShardbladeDynamicItem;
import leaf.cosmere.surgebinding.common.items.ShardplateCurioItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Surgebinding.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SurgebindingCapabilitiesHandler
{

	@SubscribeEvent
	public static void attachWorldCapabilities(AttachCapabilitiesEvent<Level> event)
	{
		Level level = event.getObject();

		if (event.getObject().dimension().location().toString().contains("roshar"))
		{
			event.addCapability(Constants.Resources.ROSHAR_CAP, new ICapabilitySerializable<CompoundTag>()
			{
				final RosharCapability roshar = new RosharCapability(level);
				final LazyOptional<IRoshar> rosharInstance = LazyOptional.of(() -> roshar);

				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side)
				{
					return cap == RosharCapability.CAPABILITY ? (LazyOptional<T>) rosharInstance
					                                          : LazyOptional.empty();
				}

				@Override
				public CompoundTag serializeNBT()
				{
					return roshar.serializeNBT();
				}

				@Override
				public void deserializeNBT(CompoundTag nbt)
				{
					roshar.deserializeNBT(nbt);
				}
			});
		}

	}

	@SubscribeEvent
	public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event)
	{
		ItemStack stack = event.getObject();
		Item item = stack.getItem();

		if (item instanceof IShardItem)
		{
			if(item instanceof ShardplateCurioItem)
			{
				event.addCapability(new ResourceLocation(Surgebinding.MODID, "shard_cap"),
						new ShardCapProvider(() -> new DynamicShardplateData(stack)));
			}
			else if(item instanceof ShardbladeDynamicItem)
			{
				event.addCapability(new ResourceLocation(Surgebinding.MODID, "shard_cap"),
						new ShardCapProvider(() -> new DynamicShardbladeData(stack)));
			}
			else
			{
				event.addCapability(new ResourceLocation(Surgebinding.MODID,"shard_cap"),
						new ShardCapProvider(() -> new DynamicShardplateData(stack)));
			}
		}
	}
}
