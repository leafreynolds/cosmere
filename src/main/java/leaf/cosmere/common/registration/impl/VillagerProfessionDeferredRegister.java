package leaf.cosmere.common.registration.impl;

import com.google.common.collect.ImmutableSet;
import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class VillagerProfessionDeferredRegister extends WrappedDeferredRegister<VillagerProfession>
{
	public VillagerProfessionDeferredRegister(String modid)
	{
		super(modid, Registries.VILLAGER_PROFESSION);
	}

	private static VillagerProfession registerProfession(String name, POITypeRegistryObject<PoiType> poi, SoundEvent workSound)
	{
		Predicate<Holder<PoiType>> heldJobSite = (poiType) -> poiType == poi.getRegistryObject().getHolder().get();
		Predicate<Holder<PoiType>> acquirableJobSite = (poiType) -> poiType == poi.getRegistryObject().getHolder().get();

		return new VillagerProfession(
				name,
				heldJobSite,
				acquirableJobSite,
				ImmutableSet.of(),
				ImmutableSet.of(),
				workSound);
	}

	public VillagerProfessionRegistryObject<VillagerProfession> register(String name, POITypeRegistryObject<PoiType> poi, SoundEvent soundEvent)
	{
		return register(name, () -> registerProfession(name, poi, soundEvent));
	}


	public VillagerProfessionRegistryObject<VillagerProfession> register(String name, Supplier<VillagerProfession> sup)
	{
		return register(name, sup, VillagerProfessionRegistryObject::new);
	}
}