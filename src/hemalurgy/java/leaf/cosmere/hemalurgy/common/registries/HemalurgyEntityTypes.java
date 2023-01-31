/*
 * File updated ~ 30 - 1 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.registries;

import leaf.cosmere.common.registration.impl.EntityTypeDeferredRegister;
import leaf.cosmere.common.registration.impl.EntityTypeRegistryObject;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.entity.Koloss;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class HemalurgyEntityTypes
{
	public static final EntityTypeDeferredRegister ENTITY_TYPES = new EntityTypeDeferredRegister(Hemalurgy.MODID);

	public static final EntityTypeRegistryObject<Koloss> KOLOSS =
			ENTITY_TYPES.register(
					"koloss",
					EntityType.Builder.<Koloss>of(Koloss::new, MobCategory.MISC)
							.sized(1, 1)
							.clientTrackingRange(4)
							.updateInterval(20));

}
