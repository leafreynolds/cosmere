package leaf.cosmere.surgebinding.common.network.packets;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.items.HonorbladeItem;
import leaf.cosmere.surgebinding.common.items.ShardbladeItem;
import leaf.cosmere.surgebinding.common.registries.SurgebindingAttributes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record SummonShardblade() implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<SummonShardblade> TYPE =
			new CustomPacketPayload.Type<>(Surgebinding.rl("summon_shardblade"));

	public static final StreamCodec<ByteBuf, SummonShardblade> STREAM_CODEC =
			StreamCodec.unit(new SummonShardblade());

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	@Override
	public void handle(IPayloadContext context)
	{
		if (!(context.player() instanceof ServerPlayer sender))
		{
			return;
		}
		context.enqueueWork(() -> SpiritwebCapability.get(sender).ifPresent(cap ->
		{
			var spiritwebTags = cap.getCompoundTag();
			var shardblades = CompoundNBTHelper.getOrCreate(spiritwebTags, "shardblades");

			final LivingEntity livingEntity = cap.getLiving();
			final ItemStack itemInHand = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
			final int maxShardblades = SurgebindingConfigs.SERVER.MAX_SHARDBLADES.get();

			if (itemInHand.isEmpty())
			{
				for (int i = 0; i < maxShardblades; i++)
				{
					final String pKey = String.valueOf(i);
					if (shardblades.contains(pKey))
					{
						final CompoundTag test = shardblades.getCompound(pKey);
						ItemStack stack = ItemStack.parseOptional(sender.level().registryAccess(), test);
						livingEntity.setItemInHand(InteractionHand.MAIN_HAND, stack);
						shardblades.remove(pKey);

						if (stack.getItem() instanceof HonorbladeItem honorbladeItem)
						{
							updateIntrinsicPowers(honorbladeItem, livingEntity, false);
						}
						break;
					}
				}
			}
			else if (itemInHand.getItem() instanceof ShardbladeItem shardbladeItem)
			{
				if (shardbladeItem.canSummonDismiss(sender))
				{
					for (int i = 0; i < maxShardblades; i++)
					{
						final String pKey = String.valueOf(i);
						if (!shardblades.contains(pKey))
						{
							shardblades.put(pKey, itemInHand.save(sender.level().registryAccess()));
							livingEntity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

							if (shardbladeItem instanceof HonorbladeItem honorbladeItem)
							{
								updateIntrinsicPowers(honorbladeItem, livingEntity, true);
							}
							break;
						}
					}
				}
			}
		}));
	}

	private static void updateIntrinsicPowers(HonorbladeItem honorbladeItem, LivingEntity livingEntity, boolean isStoringHonorblade)
	{
		final Roshar.RadiantOrder radiantOrder = honorbladeItem.radiantOrder;
		Attribute firstSurge = SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(radiantOrder.getFirstSurge()).getAttribute();
		Attribute secondSurge = SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(radiantOrder.getSecondSurge()).getAttribute();

		ResourceLocation primaryId = Surgebinding.rl(radiantOrder.getName() + "_primary_honorblade_surge");
		ResourceLocation secondaryId = Surgebinding.rl(radiantOrder.getName() + "_secondary_honorblade_surge");

		if (isStoringHonorblade)
		{
			var firstHolder = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(firstSurge);
			var secondHolder = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(secondSurge);
			livingEntity.getAttribute(firstHolder).addPermanentModifier(new AttributeModifier(primaryId, 5, AttributeModifier.Operation.ADD_VALUE));
			livingEntity.getAttribute(secondHolder).addPermanentModifier(new AttributeModifier(secondaryId, 5, AttributeModifier.Operation.ADD_VALUE));
		}
		else
		{
			var firstHolder = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(firstSurge);
			var secondHolder = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(secondSurge);
			livingEntity.getAttribute(firstHolder).removeModifier(primaryId);
			livingEntity.getAttribute(secondHolder).removeModifier(secondaryId);
		}
	}
}
