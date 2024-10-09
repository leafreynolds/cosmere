/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.api.text.IHasTranslationKey;
import leaf.cosmere.common.Cosmere;
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

public class CosmereDamageTypesRegistry
{
	private static final Map<String, CosmereDamageType> INTERNAL_DAMAGE_TYPES = new HashMap<>();
	public static final Map<String, CosmereDamageType> DAMAGE_TYPES = Collections.unmodifiableMap(INTERNAL_DAMAGE_TYPES);

	public static final CosmereDamageType EAT_METAL = new CosmereDamageType("eat_metal", 0.1f);
	public static final CosmereDamageType SPIKED = new CosmereDamageType("spiked", 0.1f);

	public record CosmereDamageType(ResourceKey<DamageType> key, float exhaustion) implements IHasTranslationKey
	{
		public CosmereDamageType
		{
			INTERNAL_DAMAGE_TYPES.put(key.location().toString(), this);
		}

		private CosmereDamageType(String name)
		{
			this(name, 0);
		}

		private CosmereDamageType(String name, float exhaustion)
		{
			this(ResourceKey.create(Registries.DAMAGE_TYPE, Cosmere.rl(name)), exhaustion);
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
			return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key()));
		}
	}
}
