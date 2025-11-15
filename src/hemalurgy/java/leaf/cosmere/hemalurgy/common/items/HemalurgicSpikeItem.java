/*
 * File updated ~ 15 - 11 - 2025 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.config.HemalurgyConfigs;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static leaf.cosmere.common.registry.CosmereDamageTypesRegistry.SPIKED;

//Other ideas?
//Spike Guns?
//https://wob.coppermind.net/events/390-stuttgart-signing/#e12677

@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HemalurgicSpikeItem extends ChargeableMetalCurioItem implements IHemalurgicInfo
{
	/**
	 * Modifiers applied when the item is in the mainhand of a user. copied from sword item
	 */
	private final Multimap<Attribute, AttributeModifier> attributeModifiers;

	public HemalurgicSpikeItem(Metals.MetalType metalType)
	{
		super(metalType);

		//todo decide on damage
		float attackDamage = 2f + 1f;//tier.getAttackDamage();
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4f, AttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public boolean canUnequip(SlotContext context, ItemStack stack)
	{
		boolean hasBindingCurse = EnchantmentHelper.hasBindingCurse(stack);
		return (!hasBindingCurse || (context.entity() instanceof Player player && player.isCreative()));
	}


	/**
	 * generate new map of attributes for when used as a curio item.
	 */
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack)
	{
		Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();

		Metals.MetalType metalType = getMetalType();
		if (stack.getItem() instanceof IHemalurgicInfo hemalurgicInfo)
		{
			//add hemalurgic attributes, if any.
			hemalurgicInfo.getHemalurgicAttributes(attributeModifiers, stack, metalType);

            // add spiritweb buffs, if any
            UUID hemalurgicIdentity = getHemalurgicIdentity(stack);
            if (hemalurgicIdentity != null)
            {
                int spiritwebIntegrity = -1;
                if (slotContext.identifier().equals("linchpin"))
                {
                    // linchpin always gives at least +3
                    spiritwebIntegrity += 3;
                    Manifestation aPewter = CosmereAPI.manifestationRegistry().getValue(new ResourceLocation("allomancy", Metals.MetalType.PEWTER.getName()));
                    if (aPewter != null && attributeModifiers.containsKey(aPewter.getAttribute()))
                    {
                        // pewter gives an additional +3
                        spiritwebIntegrity += HemalurgyConfigs.SERVER.ALLOMANTIC_PEWTER_SPIRITWEB_BONUS.get();
                    }
                    Manifestation fGold = CosmereAPI.manifestationRegistry().getValue((new ResourceLocation("feruchemy", Metals.MetalType.GOLD.getName())));
                    if (fGold != null && attributeModifiers.containsKey(fGold.getAttribute()))
                    {
                        // f-gold gives an extra +6
                        spiritwebIntegrity += HemalurgyConfigs.SERVER.FERUCHEMICAL_GOLD_SPIRITWEB_BONUS.get();
                    }
                }

                attributeModifiers.put(HemalurgyAttributes.SPIRITWEB_INTEGRITY.getAttribute(),
                        new AttributeModifier(
                                hemalurgicIdentity,
                                "Spiritweb Integrity",
                                spiritwebIntegrity,
                                AttributeModifier.Operation.ADDITION
                        ));
            }
		}


		return attributeModifiers;
	}

	@Override
	public float getMaxChargeModifier()
	{
		//spikes are only about a quarter as effective at holding charges.
		//really we should change how much power a spike can steal as well,
		//todo power stolen is adjusted by attribute stored in metal.
		return (0.5f / 9f);
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
								Manifestation allomancyMani = CosmereAPI.manifestationRegistry().getValue(new ResourceLocation("allomancy", stealType.getName()));
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
								Manifestation feruchemyMani = CosmereAPI.manifestationRegistry().getValue(new ResourceLocation("feruchemy", stealType.getName()));
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
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		super.appendHoverText(stack, worldIn, tooltip, flagIn);

		// no extra info if there isn't any
		if (getHemalurgicIdentity(stack) == null)
		{
			return;
		}


		//stolen identities listed?

		//extra investiture powers added
		addInvestitureInformation(stack, tooltip);

		//etc?

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
				if (itemstack.getItem() instanceof HemalurgicSpikeItem spikeItem)
				{
					//entity was killed by a spike
					//pass in killed entity for the item to figure out what to do
					spikeItem.killedEntity(itemstack, playerEntity, event.getEntity());
				}

			});
		}
	}

	public void killedEntity(ItemStack stack, Player playerEntity, LivingEntity entityKilled)
	{
		//https://wob.coppermind.net/events/332/#e9569

		// do nothing if an identity exists and doesn't match
		if (!matchHemalurgicIdentity(stack, entityKilled.getUUID()))
		{
			return;
		}

		// ensure we set the stolen identity
		stealFromSpiritweb(stack, getMetalType(), playerEntity, entityKilled);
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
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack)
	{
		switch (equipmentSlot)
		{
			case MAINHAND:
			case OFFHAND:
				return this.attributeModifiers;
			case FEET:
			case LEGS:
			case CHEST:
			case HEAD:
				break;
		}


		return super.getAttributeModifiers(equipmentSlot, stack);
	}

	@Override
	public boolean canEquip(SlotContext slotContext, ItemStack stack)
	{
		//do not allow players to wear two spikes of the same metal empowered by the same killed entity UUID
		if (slotContext.entity() instanceof Player player)
		{
			final UUID stackWeWantToEquipUUID = getHemalurgicIdentity(stack);

			HemalurgicSpikeItem item = (HemalurgicSpikeItem) stack.getItem();
			Metals.MetalType stackMetal = item.getMetalType();

			if (stackWeWantToEquipUUID != null)
			{
				Predicate<ItemStack> spikePredicate = stackToFind ->
				{
					final boolean isSpike = stackToFind.getItem() instanceof HemalurgicSpikeItem;

					if (!isSpike)
					{
						return false;
					}

					final HemalurgicSpikeItem foundSpikeItem = (HemalurgicSpikeItem) stackToFind.getItem();
					final UUID foundSpikeUUID = getHemalurgicIdentity(stackToFind);

					final boolean matchingSpikeIdentity = foundSpikeItem.getMetalType() == getMetalType()
							&& foundSpikeUUID != null
							&& foundSpikeUUID.compareTo(stackWeWantToEquipUUID) == 0;

					Metals.MetalType testStackMetal = foundSpikeItem.getMetalType();
					final boolean onlyOneIronAllowed = stackMetal == Metals.MetalType.IRON && stackMetal == testStackMetal;

					return matchingSpikeIdentity || onlyOneIronAllowed;
				};


				final LazyOptional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);

				if (!curiosInventory.isPresent())
				{
					return false;
				}

				ICuriosItemHandler curiosInv = curiosInventory.resolve().get();

				return curiosInv.findFirstCurio(spikePredicate).isEmpty();
			}
		}
		return true;
	}


	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack)
	{
		//todo better logic.
		boolean isEquipping = prevStack == null || stack.getItem() != prevStack.getItem();

		if (isEquipping)
		{
			//then do hemalurgy spike logic
			//hurt the user
			//spiritweb attributes are handled in metalmind
			slotContext.entity().hurt(SPIKED.source(slotContext.entity().level()), 4);
		}

	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack)
	{
		super.onUnequip(slotContext, newStack, stack);

		//only damage if removing the spike. We can ignore replacing the spike with another spike.
		boolean isUnequipping = newStack.isEmpty() || !newStack.is(stack.getItem());
		if (isUnequipping)
		{
			slotContext.entity().hurt(SPIKED.source(slotContext.entity().level()), 4);
		}
	}
}
