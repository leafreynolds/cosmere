/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.registry.AttributesRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.List;

import static leaf.cosmere.helpers.EntityHelper.getEntitiesInRange;

public class AllomancyIronSteel extends AllomancyBase
{
    private final boolean isPush;

    public AllomancyIronSteel(Metals.MetalType metalType)
    {
        super(metalType);
        this.isPush = metalType == Metals.MetalType.STEEL;
    }

    @Override
    protected void performEffect(ISpiritweb data)
    {
        //passive active ability, if any
        {
            //see metal. Probably wait for the render tick?
            //todo
        }

        //Pushes on Nearby Metals
        if (getKeyBinding().isPressed())
        {


            //get list of blocks that the user is pushing against

            //add any metal blocks that the user is looking at

            //push user away from it


            //get list of entities that the user is pushing against

            //add any other valid entities that the user is looking at

            //push user and entities towards each other
        }


    }

    public int getRange(ISpiritweb cap)
    {
        if (!cap.manifestationActive(Manifestations.ManifestationTypes.ALLOMANCY, getPowerID()))
            return 0;

        //get allomantic strength
        double allomanticStrength = getAllomanticStrength(cap);
        return MathHelper.floor(allomanticStrength * cap.getMode(Manifestations.ManifestationTypes.ALLOMANCY, getPowerID()));

    }


    @OnlyIn(Dist.CLIENT)
    public static List<Vector3d> getDrawLines(int range)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity playerEntity = mc.player;

        List<Vector3d> found = new ArrayList<>();

        //find all the things that we want to draw a line to from the player

        //todo stop aluminum showing up, check IHasMetalType.getMetalType != aluminum

        //metal blocks
        BlockPos.getProximitySortedBoxPositions(playerEntity.getPosition(), range, range, range)
                .filter(blockPos ->
                {
                    Block block = playerEntity.world.getBlockState(blockPos).getBlock();

                    if (block instanceof IHasMetalType)
                    {
                        return true;
                    }

                    return false;
                })
                .forEach(blockPos -> found.add(new Vector3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5)));


        //entities with metal armor/tools

        getEntitiesInRange(playerEntity, range, false).forEach(entity ->
        {
            if (entity instanceof LivingEntity)
            {
                //check for metal items on the entity

            }
            else if (entity instanceof ItemEntity)
            {
                ItemStack stack = ((ItemEntity) entity).getItem();
                Item item = stack.getItem();

                if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof IHasMetalType)
                {
                    found.add(entity.getPositionVec());
                }
                else if (item instanceof IHasMetalType)
                {
                    found.add(entity.getPositionVec());
                }
            }
        });

        return found;
    }


}
