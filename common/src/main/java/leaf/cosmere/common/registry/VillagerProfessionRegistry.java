/*
 * File updated ~ 20 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.VillagerProfessionDeferredRegister;
import leaf.cosmere.common.registration.impl.VillagerProfessionRegistryObject;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.VillagerProfession;

public class VillagerProfessionRegistry
{
	public static final VillagerProfessionDeferredRegister VILLAGE_PROFESSIONS = new VillagerProfessionDeferredRegister(Cosmere.MODID);

	public static final VillagerProfessionRegistryObject<VillagerProfession> METAL_TRADER =
			VILLAGE_PROFESSIONS.register(
					"metal_trader",
					PoiTypesRegistry.METAL_TRADER_POI,
					SoundEvents.VILLAGER_WORK_TOOLSMITH);

}
