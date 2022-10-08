/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.registries;

import com.mojang.serialization.Codec;
import leaf.cosmere.common.registration.impl.GlobalLootModifierDeferredRegister;
import leaf.cosmere.common.registration.impl.GlobalLootModifierRegistryObject;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.loot.FortuneBonusModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;

public class FeruchemyLootModifiers
{
	public static final GlobalLootModifierDeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = new GlobalLootModifierDeferredRegister<>(Feruchemy.MODID);
	public static final GlobalLootModifierRegistryObject<Codec<? extends IGlobalLootModifier>> FORTUNE_BONUS = LOOT_MODIFIERS.register("fortune_bonus", FortuneBonusModifier.CODEC);
}
