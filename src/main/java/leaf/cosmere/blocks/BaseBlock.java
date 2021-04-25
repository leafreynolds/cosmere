/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;

public class BaseBlock extends Block
{

    public BaseBlock(Properties properties, SoundType sound, float hardness, float resistance)
    {

        super(properties.sound(sound).hardnessAndResistance(hardness, resistance));
    }

    public BaseBlock(Properties properties)
    {
        super(properties);

    }
}
