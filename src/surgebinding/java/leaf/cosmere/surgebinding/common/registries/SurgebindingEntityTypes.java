/*
 * File updated ~ 26 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.common.registration.impl.EntityTypeDeferredRegister;
import leaf.cosmere.common.registration.impl.EntityTypeRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.entity.Chull;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class SurgebindingEntityTypes
{
	public static final EntityTypeDeferredRegister ENTITY_TYPES = new EntityTypeDeferredRegister(Surgebinding.MODID);

	public static final EntityTypeRegistryObject<Chull> CHULL =
			ENTITY_TYPES.register(
					"chull",
					EntityType.Builder.<Chull>of(Chull::new, MobCategory.MISC)
							.sized(1, 1)
							.clientTrackingRange(4)
							.updateInterval(20));

}
