/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributesRegistry
{
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Cosmere.MODID);
    //this isn't how you do it chief
    public static final RegistryObject<Attribute> REDUCED_HEALTH = ATTRIBUTES.register("generic.reduced_health",
            () -> (new RangedAttribute(
                    "attribute.name.generic.reduced_health",
                    -20.0D,
                    -1024.0D,
                    -1.0D)).setShouldWatch(true));


}
