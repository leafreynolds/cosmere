/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.registry;

import com.mojang.serialization.MapCodec;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.loot.FortuneBonusModifier;
import leaf.cosmere.common.registration.impl.GlobalLootModifierDeferredRegister;
import leaf.cosmere.common.registration.impl.GlobalLootModifierRegistryObject;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;

public class LootModifiersRegistry
{
	public static final GlobalLootModifierDeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = new GlobalLootModifierDeferredRegister<>(Cosmere.MODID);
	public static final GlobalLootModifierRegistryObject<MapCodec<? extends IGlobalLootModifier>> FORTUNE_BONUS = LOOT_MODIFIERS.register("fortune_bonus", FortuneBonusModifier.CODEC);
}
