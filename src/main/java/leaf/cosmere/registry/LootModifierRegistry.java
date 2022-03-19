/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to the curio mod for providing the example of how to set up fortune bonus for non-tool related things.
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.effects.FortuneBonusModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(
        modid = Cosmere.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class LootModifierRegistry
{
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Cosmere.MODID);
    public static final RegistryObject<GlobalLootModifierSerializer<?>> FORTUNE_BONUS = LOOT_MODIFIERS.register("fortune_bonus", FortuneBonusModifier.Serializer::new);

}
