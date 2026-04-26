package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.surgebinding.client.render.renderer.ShardbladeItemRenderer;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardbladeData;
import leaf.cosmere.surgebinding.common.capabilities.IShardbladeDynamicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class ShardbladeDynamicItem extends ShardbladeItem
{
	public ShardbladeDynamicItem(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
	}

	public static IShardbladeDynamicData getData(ItemStack stack)
	{
		return DynamicShardbladeData.fromStack(stack);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
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
