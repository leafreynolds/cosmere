/*
 * File updated ~ 22 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.example.client;

import leaf.cosmere.example.common.Example;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Example.MODID, bus = Bus.GAME, value = Dist.CLIENT)
public class ExampleForgeClientEvents
{

}
