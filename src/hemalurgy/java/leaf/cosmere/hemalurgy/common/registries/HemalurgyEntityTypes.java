/*
 * File updated ~ 19 - 7 - 2023 ~ Leaf
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

	public static final EntityTypeRegistryObject<Koloss> KOLOSS_LARGE =
			ENTITY_TYPES.register(
					"koloss_large",
					EntityType.Builder.<Koloss>of(Koloss::new, MobCategory.MISC)
							.setShouldReceiveVelocityUpdates(false)
							.updateInterval(1)
							.setTrackingRange(8)
							.clientTrackingRange(10)
							.sized(2.5F, 5.5F)
			);
	public static final EntityTypeRegistryObject<Koloss> KOLOSS_MEDIUM =
			ENTITY_TYPES.register(
					"koloss_medium",
					EntityType.Builder.<Koloss>of(Koloss::new, MobCategory.MISC)
							.setShouldReceiveVelocityUpdates(false)
							.updateInterval(1)
							.setTrackingRange(8)
							.clientTrackingRange(10)
							.sized(1.5f, 4)
			);
	public static final EntityTypeRegistryObject<Koloss> KOLOSS_SMALL =
			ENTITY_TYPES.register(
					"koloss_small",
					EntityType.Builder.<Koloss>of(Koloss::new, MobCategory.MISC)
							.setShouldReceiveVelocityUpdates(false)
							.updateInterval(1)
							.setTrackingRange(8)
							.clientTrackingRange(10)
							.sized(1, 2)
			);

}
