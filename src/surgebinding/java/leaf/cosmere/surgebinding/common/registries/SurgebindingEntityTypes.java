/*
 * File updated ~ 7 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.common.registration.impl.EntityTypeDeferredRegister;
import leaf.cosmere.common.registration.impl.EntityTypeRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.entity.Chull;
import leaf.cosmere.surgebinding.common.entity.Cryptic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class SurgebindingEntityTypes
{
	public static final EntityTypeDeferredRegister ENTITY_TYPES = new EntityTypeDeferredRegister(Surgebinding.MODID);

	public static final EntityTypeRegistryObject<Chull> CHULL =
			ENTITY_TYPES.register(
					"chull",
					EntityType.Builder.<Chull>of(Chull::new, MobCategory.CREATURE)
							.setShouldReceiveVelocityUpdates(false)
							.setUpdateInterval(1)
							.setTrackingRange(8)
							.clientTrackingRange(10)
							.sized(4.5f, 5.2f)
			);

	public static final EntityTypeRegistryObject<Cryptic> CRYPTIC =
			ENTITY_TYPES.register(
					"cryptic",
					EntityType.Builder.<Cryptic>of(Cryptic::new, MobCategory.CREATURE)
							.setShouldReceiveVelocityUpdates(false)
							.setUpdateInterval(1)
							.setTrackingRange(8)
							.clientTrackingRange(10)
							.sized(0.75f, 0.75f)
			);

}
