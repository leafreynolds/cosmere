/*
 * File created ~ 20 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.registry;

import com.google.common.collect.ImmutableSet;
import leaf.cosmere.Cosmere;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class VillagerProfessionRegistry
{
	public static DeferredRegister<VillagerProfession> VILLAGE_PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, Cosmere.MODID);

	public static final RegistryObject<VillagerProfession> METAL_TRADER = VILLAGE_PROFESSIONS.register(
			"metal_trader",
			() -> registerProfession(
					"metal_trader",
					PointOfInterestRegistry.METAL_TRADER_POI,
					SoundEvents.VILLAGER_WORK_TOOLSMITH));

	public static VillagerProfession registerProfession(String name, RegistryObject<PoiType> poi, SoundEvent workSound)
	{
		Predicate<Holder<PoiType>> heldJobSite = (poiType) -> {
			return poiType == poi.getHolder().get();
		};
		Predicate<Holder<PoiType>> acquirableJobSite = (poiType) -> {
			return poiType == poi.getHolder().get();
		};


		return new VillagerProfession(
				name,
				heldJobSite,
				acquirableJobSite,
				ImmutableSet.of(),
				ImmutableSet.of(),
				workSound);
	}
}
