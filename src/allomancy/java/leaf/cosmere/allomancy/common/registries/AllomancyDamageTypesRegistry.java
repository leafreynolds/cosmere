package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.api.text.IHasTranslationKey;
import leaf.cosmere.common.registry.CosmereDamageTypesRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AllomancyDamageTypesRegistry extends CosmereDamageTypesRegistry
{
	public static final Map<String, AllomancyDamageType> INTERNAL_DAMAGE_TYPES = new HashMap<>();
	public static final Map<String, AllomancyDamageType> DAMAGE_TYPES = Collections.unmodifiableMap(INTERNAL_DAMAGE_TYPES);

	public static final AllomancyDamageType PEWTER_DELAYED_DAMAGE = new AllomancyDamageType("pewter_delayed_damage", 0.1f);

	public record AllomancyDamageType(ResourceKey<DamageType> key, float exhaustion) implements IHasTranslationKey
	{
		public AllomancyDamageType
		{
			INTERNAL_DAMAGE_TYPES.put(key.location().toString(), this);
		}

		private AllomancyDamageType(String name)
		{
			this(name, 0);
		}

		private AllomancyDamageType(String name, float exhaustion)
		{
			this(ResourceKey.create(Registries.DAMAGE_TYPE, Allomancy.rl(name)), exhaustion);
		}

		public String getMsgId()
		{
			return registryName().getNamespace() + "." + registryName().getPath();
		}

		public ResourceLocation registryName()
		{
			return key.location();
		}

		@NotNull
		@Override
		public String getTranslationKey()
		{
			return "death.attack." + getMsgId();
		}

		public DamageSource source(Level level)
		{
			return source(level.registryAccess());
		}

		public DamageSource source(RegistryAccess registryAccess)
		{
			return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
		}
	}
}