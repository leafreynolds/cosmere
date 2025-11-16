/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.surgebinding.client.render.renderer.ShardbladeItemRenderer;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardbladeData;
import leaf.cosmere.surgebinding.common.capabilities.IShardbladeDynamicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ShardbladeDynamicItem extends ShardbladeItem
{
	public static final Capability<IShardbladeDynamicData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});


	public ShardbladeDynamicItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
	}

	@Override
	public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
	{
		final DynamicShardbladeData dynamicShardbladeData = new DynamicShardbladeData();

		if (nbt != null)
		{
			dynamicShardbladeData.deserializeNBT(nbt); // todo check if this breaks things?
		}

		return dynamicShardbladeData;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		//the class instance hasn't been resolved yet at this point, so we can't check for field values

		consumer.accept(new IClientItemExtensions()
		{
			ShardbladeItemRenderer renderer = null;

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer()
			{
				Minecraft minecraft = Minecraft.getInstance();

				if (renderer == null)
				{
					renderer = new ShardbladeItemRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
				}

				return renderer;
			}
		});
	}

}