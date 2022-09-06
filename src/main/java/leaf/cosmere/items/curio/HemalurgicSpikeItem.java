/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items.curio;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.items.MetalmindItem;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.utils.helpers.LogHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

//Other ideas?
//Spike Guns?
//https://wob.coppermind.net/events/390-stuttgart-signing/#e12677

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HemalurgicSpikeItem extends MetalmindItem implements IHemalurgicInfo
{
	private final float attackDamage;
	/**
	 * Modifiers applied when the item is in the mainhand of a user. copied from sword item
	 */
	private final Multimap<Attribute, AttributeModifier> attributeModifiers;


	public static final DamageSource SPIKED = (new DamageSource("spiked")).bypassArmor().bypassMagic();

	//todo move
	private static final ResourceLocation SPIKE_TEXTURE = new ResourceLocation("cosmere", "textures/block/metal_block.png");
	private Object model;

	public HemalurgicSpikeItem(Metals.MetalType metalType)
	{
		super(metalType, CosmereItemGroups.HEMALURGIC_SPIKES);

		//todo decide on damage
		this.attackDamage = 2f + 1f;//tier.getAttackDamage();
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4f, AttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public boolean canUnequip(String identifier, LivingEntity livingEntity, ItemStack stack)
	{
		boolean hasBindingCurse = EnchantmentHelper.hasBindingCurse(stack);
		boolean isPlayer = livingEntity instanceof Player;

		return (!hasBindingCurse || (isPlayer && ((Player) livingEntity).isCreative()));
	}

	@Override
	public float getMaxChargeModifier()
	{
		return (2f / 9f);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks)
	{
		super.fillItemCategory(tab, stacks);
		if (allowedIn(tab))
		{
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

					stacks.add(filledIronSpike);
				}
				else if (this.getMetalType() == Metals.MetalType.TIN)
				{
					ItemStack filledIronSpike = new ItemStack(this);
					Invest(filledIronSpike, this.getMetalType(), 0.25f, UUID.randomUUID());
					stacks.add(filledIronSpike);
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
								case STEEL:
								case BRONZE:
								case CADMIUM:
								case ELECTRUM:
									ItemStack allomancySpike = new ItemStack(this);
									AManifestation allomancyMani = ManifestationRegistry.ALLOMANCY_POWERS.get(stealType).get();
									Invest(allomancySpike, allomancyMani, 7, UUID.randomUUID());
									stacks.add(allomancySpike);
									break;
								//steals feruchemical abilities
								case PEWTER:
								case BRASS:
								case BENDALLOY:
								case GOLD:
									ItemStack feruchemySpike = new ItemStack(this);
									AManifestation feruchemyMani = ManifestationRegistry.FERUCHEMY_POWERS.get(stealType).get();
									Invest(feruchemySpike, feruchemyMani, 7, UUID.randomUUID());
									stacks.add(feruchemySpike);
									break;
							}


						}
						catch (Exception e)
						{
							LogHelper.info(String.format("remove %s from whitelist for %s spikes", stealType.toString(), getMetalType()));
						}
					}
				}
			}

			if (this.getMetalType() == Metals.MetalType.LERASATIUM)
			{
				ItemStack bound = new ItemStack(this);
				final UUID identity = UUID.randomUUID();
				for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
				{
					//lerasatium creative mode hide surge powers for now
					if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.SURGEBINDING)
					{
						continue;
					}

					Invest(bound, manifestation, 5, identity);
				}

				stacks.add(bound);
			}
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
		if (event.getEntity().level.isClientSide())
			return;

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
				break;
			case LEGS:
				break;
			case CHEST:
				break;
			case HEAD:
				break;
		}


		return super.getAttributeModifiers(equipmentSlot, stack);
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity livingEntity, ItemStack stack)
	{
		//do not allow players to wear two spikes of the same metal empowered by the same killed entity UUID
		if (livingEntity instanceof Player player)
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
				final Optional<ImmutableTriple<String, Integer, ItemStack>> curioSpike = CuriosApi.getCuriosHelper().findEquippedCurio(spikePredicate, player);
				return curioSpike.isEmpty();
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
			final LivingEntity entity = slotContext.getWearer();
			entity.hurt(SPIKED, 4);
		}

	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack)
	{
		//only damage if removing the spike. We can ignore replacing the spike with another spike.
		boolean isUnequipping = newStack.isEmpty() || !newStack.is(stack.getItem());
		if (isUnequipping)
		{
			final LivingEntity entity = slotContext.getWearer();
			entity.hurt(SPIKED, 4);
		}
	}
}
