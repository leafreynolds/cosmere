/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;

import net.minecraft.block.AbstractBlock.Properties;

public class BaseBlock extends Block
{

    public BaseBlock(Properties properties, SoundType sound, float hardness, float resistance)
    {
        super(properties.sound(sound).strength(hardness, resistance));
    }

    public BaseBlock(Properties properties)
    {
        super(properties);

    }
}
