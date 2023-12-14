package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.EntityTypeDeferredRegister;

public class EntityTypeRegistry
{

	public static final EntityTypeDeferredRegister ENTITY_TYPES = new EntityTypeDeferredRegister(Cosmere.MODID);
}
