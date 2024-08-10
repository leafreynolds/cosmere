/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.registry;

import com.mojang.serialization.Codec;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.loot.FortuneBonusModifier;
import leaf.cosmere.common.registration.impl.GlobalLootModifierDeferredRegister;
import leaf.cosmere.common.registration.impl.GlobalLootModifierRegistryObject;
import net.minecraftforge.common.loot.IGlobalLootModifier;

public class LootModifiersRegistry
{
	public static final GlobalLootModifierDeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = new GlobalLootModifierDeferredRegister<>(Cosmere.MODID);
	public static final GlobalLootModifierRegistryObject<Codec<? extends IGlobalLootModifier>> FORTUNE_BONUS = LOOT_MODIFIERS.register("fortune_bonus", FortuneBonusModifier.CODEC);
}
