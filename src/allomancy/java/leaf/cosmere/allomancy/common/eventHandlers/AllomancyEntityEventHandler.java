/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.allomancy.common.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.items.CoinPouchItem;
import leaf.cosmere.allomancy.common.items.MistcloakItem;
import leaf.cosmere.allomancy.common.manifestation.AllomancyAtium;
import leaf.cosmere.allomancy.common.manifestation.AllomancyChromium;
import leaf.cosmere.allomancy.common.manifestation.AllomancyNicrosil;
import leaf.cosmere.allomancy.common.manifestation.AllomancyPewter;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.allomancy.common.utils.MiscHelper;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.CuriosHelper;
import leaf.cosmere.common.items.MetalNuggetItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Allomancy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllomancyEntityEventHandler
{


	@SubscribeEvent
	public static void onEntityItemPickUp(EntityItemPickupEvent event)
	{
		if (CoinPouchItem.onPickupItem(event.getItem(), event.getEntity()))
		{
			event.setCanceled(true);
		}
	}


	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (!(event.getTarget() instanceof LivingEntity target))
		{
			return;
		}

		ItemStack stack = event.getEntity().getMainHandItem();
		if (!stack.isEmpty())
		{
			if (stack.getItem() instanceof MetalNuggetItem beadItem)
			{
				Metals.MetalType metalType = beadItem.getMetalType();

				switch (metalType)
				{
					//only care about god metal when trying to give others powers
					case LERASIUM:
						MiscHelper.consumeNugget(target, metalType, 1);
						//need to shrink, because metal nugget only shrinks on item use finish from eating, which is not part of entity interact with item
						stack.shrink(1);
						break;
				}

			}
		}
		else
		{

		}
	}


	@SubscribeEvent
	public static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event)
	{
		if (event.isCanceled())
		{
			return;
		}

		final LivingEntity livingEntity = event.getEntity();
		if (event.getItem().getItem() instanceof MetalNuggetItem item)
		{
			//no need to shrink item count as it's already done as part of nugget use item finish
			MiscHelper.consumeNugget(livingEntity, item.getMetalType(), 1);
		}
	}


	//Attack event happens first
	@SubscribeEvent
	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		AllomancyAtium.onLivingAttackEvent(event);
	}

	//then living hurt event
	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		AllomancyNicrosil.onLivingHurtEvent(event);
		AllomancyPewter.onLivingHurtEvent(event);
		AllomancyChromium.onLivingHurtEvent(event);

		DamageSource source = event.getSource();

		LivingEntity entity = event.getEntity();

		// Skip if dead or no damage
		if (entity.level().isClientSide || event.getAmount() <= 0)
		{
			return;
		}
		// Only do damage types that the mistcloak can protect.
		for(ResourceKey<DamageType> type: protectedDamageTypes)
		{
			if(source.is(type))
			{
				Optional<ICuriosItemHandler> sub = CuriosApi.getCuriosInventory(entity).resolve();
				if (CuriosHelper.getCuriosHandler(entity) != null)
				{

					for (SlotResult slotResult : CuriosHelper.getSlotsWithItem(entity, AllomancyItems.MISTCLOAK.asItem()))
					{
						ItemStack stack = slotResult.stack();
						MistcloakItem item = (MistcloakItem) stack.getItem();
						if (item.getDamage(stack) == 0)
						{
							continue;
						}

						float original = event.getAmount();
						float absorbed = original * 0.2f;
						float remaining = original - absorbed;

						// Reduce damage taken by entity
						event.setAmount(remaining);

						stack.setDamageValue((int) (stack.getDamageValue() - absorbed));
					}
				}
			}
		}
		// Find the curio item that acts as armor

	}

	private static ArrayList<ResourceKey<DamageType>> protectedDamageTypes = new ArrayList<>(
			List.of(DamageTypes.FALL,
					DamageTypes.FLY_INTO_WALL,
					DamageTypes.FREEZE
			)
	);
}
