/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.eventHandlers;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.helpers.CuriosHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.commands.SurgebindingCommands;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.items.ShardplateCurioItem;
import leaf.cosmere.surgebinding.common.manifestation.SurgeGravitation;
import leaf.cosmere.surgebinding.common.manifestation.SurgeProgression;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Surgebinding.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SurgebindingForgeEventsHandler
{
/*
	//todo fix roshar not respecting sleep ??
	@SubscribeEvent
	public static void onSleepFinished(final SleepFinishedTimeEvent evt)
	{
		LevelAccessor levelAccessor = evt.getLevel();

		if (levelAccessor instanceof ServerLevel serverLevel)
		{
			long newTime = evt.getNewTime();

			ServerLevel level = serverLevel.getServer().getLevel(SurgebindingDimensions.ROSHAR_DIM_KEY);
			if (level != null)
			{
				evt.setTimeAddition(600);
			}
		}
	}*/

	@SubscribeEvent
	public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event)
	{
		if (event.isCanceled())
		{
			return;
		}

		SurgeProgression.onBlockInteract(event);
	}

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event)
	{
		SurgebindingCommands.register(event.getDispatcher());
	}

	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (!(event.getTarget() instanceof LivingEntity target) || event.isCanceled())
		{
			return;
		}

		SurgeProgression.onEntityInteract(event);
	}


	//Attack event happens first
	@SubscribeEvent
	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		if (event.isCanceled())
		{
			return;
		}

		SurgeGravitation.onLivingAttackEvent(event);
	}


	@SubscribeEvent
	public static void onServerChatEvent(ServerChatEvent event)
	{
		SpiritwebCapability.get(event.getPlayer()).ifPresent(spiritweb ->
		{
			final ISpiritwebSubmodule submodule = spiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
			if (submodule instanceof SurgebindingSpiritwebSubmodule surgebinding)
			{
				surgebinding.onChatMessageReceived(event);
			}
		});
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		DamageSource source = event.getSource();

		LivingEntity entity = event.getEntity();

		// Skip if dead or no damage
		if (entity.level().isClientSide || event.getAmount() <= 0)
		{
			return;
		}
		// Skip if a damage type that bypasses Shardplate
		for(ResourceKey<DamageType> type: unprotectedDamageTypes)
		{
			if(source.is(type))
			{
				return;
			}
		}
		// Find the curio item that acts as armor
		Optional<ICuriosItemHandler> sub = CuriosApi.getCuriosInventory(entity).resolve();


		if (CuriosHelper.getCuriosHandler(entity) != null)
		{

			for (SlotResult slotResult : CuriosHelper.getSlotsWithItem(entity, ItemRegistryObject.asItemList(SurgebindingItems.SHARDPLATE_SUITS.values())))
			{
				ItemStack stack = slotResult.stack();
				ShardplateCurioItem item = (ShardplateCurioItem) stack.getItem();
				if (item.getCharge(stack) == 0)
				{
					continue;
				}

				float original = event.getAmount();
				float absorbed = original * 0.4f;
				float remaining = original - absorbed;

				// Reduce damage taken by entity
				event.setAmount(remaining);

				item.adjustCharge(stack,
						-Math.min((int) (absorbed * SurgebindingConfigs.SERVER.SHARDPLATE_PROTECTION_INVESTITURE_COST.get()),
								item.getCharge(stack)
						)
				);
			}
		}
	}


	private static ArrayList<ResourceKey<DamageType>> unprotectedDamageTypes = new ArrayList<>(
			List.of(DamageTypes.DROWN,
					DamageTypes.STARVE,
					DamageTypes.BAD_RESPAWN_POINT,
					DamageTypes.FELL_OUT_OF_WORLD,
					DamageTypes.CRAMMING,
					DamageTypes.IN_WALL,
					DamageTypes.OUTSIDE_BORDER
			)
	);
}
