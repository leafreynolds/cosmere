/*
 * File updated ~ 15 - 11 - 2025 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.items;

import com.google.common.collect.Multimap;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.capabilities.HemalurgyItemCapabilities;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

//Other ideas?
//Spike Guns?
//https://wob.coppermind.net/events/390-stuttgart-signing/#e12677

@EventBusSubscriber(modid = Hemalurgy.MODID)
public class HemalurgicSpikeItem extends ChargeableMetalCurioItem implements IHemalurgicInfo
{
	/**
	 * Modifiers applied when the item is in the mainhand of a user. copied from sword item
	 */
	private static final ItemAttributeModifiers SPIKE_ATTRIBUTE_MODIFIERS = ItemAttributeModifiers.builder()
			//todo decide on damage
			.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 2f + 1f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
			.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
			.build();

	public HemalurgicSpikeItem(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public boolean canUnequip(SlotContext context, ItemStack stack)
	{
		return SpikeCurioLogic.canUnequip(context, stack);
	}


	/**
	 * generate new map of attributes for when used as a curio item.
	 */
	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation location, ItemStack stack)
	{
		return SpikeCurioLogic.getAttributeModifiers(this, slotContext, stack);
	}

	@Override
	public float getMaxChargeModifier()
	{
		//spikes are only about a quarter as effective at holding charges.
		return (0.5f / 9f);
	}

	//stolen powers shrink feruchemical capacity
	@Override
	public int getMaxCharge(ItemStack stack)
	{
		return scaleMaxChargeByInvestiture(stack, getBaseMaxCharge(stack));
	}

	@Override
	public void addFilled(CreativeModeTab.Output output)
	{
		super.addFilled(output);

		if (getMetalType().hasFeruchemicalEffect())
		{
			//what powers can this metal type contain

			if (this.getMetalType() == Metals.MetalType.IRON)
			{
				ItemStack filledIronSpike = new ItemStack(this);
				//steals physical strength
				//don't steal modified values, only base value
				//todo decide how much strength is reasonable to steal and how much goes to waste
				//currently will try 70%
				double strengthToAdd = 15 * 0.7D;// Iron golems have the most base attack damage of normal mods (giants have 50??). Ravagers have


				Invest(filledIronSpike, this.getMetalType(), strengthToAdd, UUID.randomUUID());

				output.accept(filledIronSpike);
			}
			else if (this.getMetalType() == Metals.MetalType.TIN)
			{
				ItemStack filledSpike = new ItemStack(this);
				Invest(filledSpike, this.getMetalType(), 0.25f, UUID.randomUUID());
				output.accept(filledSpike);
			}
			else if (this.getMetalType() == Metals.MetalType.COPPER)
			{
				ItemStack filledSpike = new ItemStack(this);
				Invest(filledSpike, this.getMetalType(), 0.5f, UUID.randomUUID());
				output.accept(filledSpike);
			}


			Collection<Metals.MetalType> hemalurgyStealWhitelist = getMetalType().getHemalurgyStealWhitelist();
			if (hemalurgyStealWhitelist != null)
			{
				for (Metals.MetalType stealType : hemalurgyStealWhitelist)
				{
					if (!stealType.hasAssociatedManifestation())
					{
						continue;
					}
					try
					{

						//then we've found something to steal!
						switch (this.getMetalType())
						{
							//steals allomantic abilities
							case STEEL, BRONZE, CADMIUM, ELECTRUM ->
							{
								ItemStack allomancySpike = new ItemStack(this);
								Manifestation allomancyMani = CosmereAPI.manifestationRegistry().get(ResourceLocation.fromNamespaceAndPath("allomancy", stealType.getName()));
								if (allomancyMani != null)
								{
									Invest(allomancySpike, allomancyMani, 7, UUID.randomUUID());
									output.accept(allomancySpike);
								}
							}
							//steals feruchemical abilities
							case PEWTER, BRASS, BENDALLOY, GOLD ->
							{
								ItemStack feruchemySpike = new ItemStack(this);
								Manifestation feruchemyMani = CosmereAPI.manifestationRegistry().get(ResourceLocation.fromNamespaceAndPath("feruchemy", stealType.getName()));
								if (feruchemyMani != null)
								{
									Invest(feruchemySpike, feruchemyMani, 7, UUID.randomUUID());
									output.accept(feruchemySpike);
								}
							}
						}


					}
					catch (Exception e)
					{
						CosmereAPI.logger.info(String.format("remove %s from whitelist for %s spikes", stealType, getMetalType()));
					}
				}
			}
		}

		if (this.getMetalType() == Metals.MetalType.LERASATIUM)
		{
			ItemStack bound = new ItemStack(this);
			final UUID identity = UUID.randomUUID();
			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				//lerasatium creative mode hide surge powers for now
				if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.SURGEBINDING)
				{
					continue;
				}

				Invest(bound, manifestation, 5, identity);
			}

			output.accept(bound);
		}
	}

	//todo hemalurgic decay
	//https://wob.coppermind.net/events/332/#e9534
	//https://wob.coppermind.net/events/121/#e5060
	private void addDecay(ItemStack stack)
	{

	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

		//todo //add decay

		//add decay if not equipped
		addDecay(stack);
		{
			// unless its in a jar?
		}
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity)
	{
		//todo //add decay


		return false;
	}


	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn)
	{
		super.appendHoverText(stack, context, tooltip, flagIn);

		// no extra info if there isn't any
		if (getHemalurgicIdentity(stack) == null)
		{
			return;
		}
		//stolen identities listed?

        //don't need to do the attributes, since thats covered by curio

    }

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event)
	{
		if (event.getEntity().level().isClientSide())
		{
			return;
		}

		if (event.getSource().getEntity() instanceof Player playerEntity)
		{
			SpiritwebCapability.get(playerEntity).ifPresent(iSpiritweb ->
			{
				ItemStack itemstack = playerEntity.getMainHandItem();
				//any spike-capable item counts, not just spike items
				IHemalurgicInfo spike = HemalurgyItemCapabilities.getSpike(itemstack);
				if (spike != null)
				{
					//entity was killed by a spike
					//pass in killed entity for the item to figure out what to do
					spike.killedEntity(itemstack, playerEntity, event.getEntity());
				}

			});
		}
	}

	@Override
	public boolean isFoil(@Nonnull ItemStack stack)
	{
		return super.isFoil(stack) || hemalurgicIdentityExists(stack);
	}

	/**
	 * Gets a map of item attribute modifiers, used by damage when used as melee weapon.
	 */
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack)
	{
		return SPIKE_ATTRIBUTE_MODIFIERS;
	}

	@Override
	public boolean canEquip(SlotContext slotContext, ItemStack stack)
	{
		return SpikeCurioLogic.canEquip(this, slotContext, stack);
	}


	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack)
	{
		SpikeCurioLogic.onEquip(slotContext, prevStack, stack);
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack)
	{
		super.onUnequip(slotContext, newStack, stack);

		SpikeCurioLogic.onUnequip(slotContext, newStack, stack);
	}
}
