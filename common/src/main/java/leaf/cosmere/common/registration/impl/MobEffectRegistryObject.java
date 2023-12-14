package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.providers.IMobEffectProvider;
import leaf.cosmere.common.registration.WrappedRegistryObject;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class MobEffectRegistryObject<MOB_EFFECT extends MobEffect> extends WrappedRegistryObject<MOB_EFFECT> implements IMobEffectProvider
{
	public MobEffectRegistryObject(RegistryObject<MOB_EFFECT> registryObject)
	{
		super(registryObject);
	}

	@NotNull
	@Override
	public MOB_EFFECT getMobEffect()
	{
		return get();
	}

}