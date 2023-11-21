/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.registries;

import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.aviar.common.entity.AviarBird;
import leaf.cosmere.common.registration.impl.EntityTypeDeferredRegister;
import leaf.cosmere.common.registration.impl.EntityTypeRegistryObject;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class AviarEntityTypes
{
	public static final EntityTypeDeferredRegister ENTITY_TYPES = new EntityTypeDeferredRegister(Aviar.MODID);

	public static final EntityTypeRegistryObject<AviarBird> AVIAR_ENTITY =
			ENTITY_TYPES.register(
					"aviar",
					EntityType.Builder.<AviarBird>of(AviarBird::new, MobCategory.CREATURE)
							.setShouldReceiveVelocityUpdates(false)
							.setUpdateInterval(1)
							.setTrackingRange(8)
							.sized(0.5F, 0.9F)
							.clientTrackingRange(8)
			);
}
