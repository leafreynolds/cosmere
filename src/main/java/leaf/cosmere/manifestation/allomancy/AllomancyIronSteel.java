/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.SyncPushPullMessage;
import leaf.cosmere.utils.helpers.CodecHelper;
import leaf.cosmere.utils.helpers.LogHelper;
import leaf.cosmere.utils.helpers.VectorHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

import static leaf.cosmere.utils.helpers.EntityHelper.getEntitiesInRange;

public class AllomancyIronSteel extends AllomancyBase
{
    private final boolean isPush;

    public AllomancyIronSteel(Metals.MetalType metalType)
    {
        super(metalType);
        this.isPush = metalType == Metals.MetalType.STEEL;
    }

    @Override
    public void performEffect(ISpiritweb data)
    {
        if (data.getLiving().level.isClientSide)
        {
            performEffectClient(data);
        }
        else
        {
            performEffectServer(data);
        }
    }

    private List<BlockPos> blocks;
    private List<Integer> entities;

    @OnlyIn(Dist.CLIENT)
    private void performEffectClient(ISpiritweb cap)
    {
        boolean hasChanged = false;
        SpiritwebCapability data = (SpiritwebCapability) cap;
        blocks = isPush ? data.pushBlocks : data.pullBlocks;
        entities = isPush ? data.pushEntities : data.pullEntities;

        //Pushes on Nearby Metals
        if (getKeyBinding().isDown())
        {
            Minecraft mc = Minecraft.getInstance();
            RayTraceResult ray = cap.getLiving().pick(getRange(cap), 0, false);

            if (ray.getType() == RayTraceResult.Type.BLOCK && !blocks.contains(((BlockRayTraceResult) ray).getBlockPos()))
            {
                BlockPos pos = ((BlockRayTraceResult) ray).getBlockPos();
                //todo check block is of ihasmetal type
                BlockState state = mc.level.getBlockState(pos);
                if (state.getBlock() instanceof IHasMetalType)
                {
                    blocks.add(pos.immutable());

                    if (blocks.size() > 5)
                    {
                        blocks.remove(0);
                    }
                    hasChanged = true;
                }
            }
            else if (ray.getType() == RayTraceResult.Type.ENTITY && !entities.contains(((EntityRayTraceResult) ray).getEntity().getId()))
            {
                //todo check for metal
                entities.add(((EntityRayTraceResult) ray).getEntity().getId());

                if (entities.size() > 5)
                {
                    entities.remove(0);
                }
                hasChanged = true;
            }
        }
        else
        {
            //clear list
            if (blocks.size() > 0)
            {
                blocks.clear();
                hasChanged = true;
            }
            if (entities.size() > 0)
            {
                entities.clear();
                hasChanged = true;
            }

        }

        //sync the move things.
        //we don't let the spirit web sync from client back to server, so this is needed.
        if (hasChanged)
        {
            CompoundNBT nbt = new CompoundNBT();

            final String pushBlocks = "pushBlocks";
            final String pullBlocks = "pullBlocks";
            String target = isPush ? pushBlocks : pullBlocks;

            CodecHelper.BlockPosListCodec.encodeStart(NBTDynamicOps.INSTANCE, blocks)
                    .resultOrPartial(LogHelper.LOGGER::error)
                    .ifPresent(inbt1 -> nbt.put(target, inbt1));

            Network.sendToServer(new SyncPushPullMessage(nbt));
        }
    }

    private void performEffectServer(ISpiritweb cap)
    {
        if (cap.getLiving().tickCount % 3 == 0)
        {
            return;
        }

        //perform the entity move thing.
        SpiritwebCapability data = (SpiritwebCapability) cap;
        blocks = isPush ? data.pushBlocks : data.pullBlocks;
        entities = isPush ? data.pushEntities : data.pullEntities;

        int blockListCount = blocks.size();

        if (blockListCount == 0)
        {
            return;
        }

        LivingEntity living = data.getLiving();
        Vector3d direction;
        float renderPartialTicks = Minecraft.getInstance().getFrameTime();

        double strength = getAllomanticStrength(cap);

        for (int i = blockListCount - 1; i >= 0; i--)
        {
            BlockPos blockPos = blocks.get(i);
            if (!isPush && blockPos.distManhattan(living.blockPosition()) < 2)
            {
                //stop shoving the user into the block
                continue;
            }


            //if the entity is in range of being able to push or pull from
            double maxDistance = (strength * data.getMode(Manifestations.ManifestationTypes.ALLOMANCY, getMetalType().getID()));// * 0.1f;
            if (blockPos.closerThan(living.position(), maxDistance))
            {
                Vector3d blockCenter = Vector3d.atCenterOf(blockPos);

                direction = VectorHelper.getDirection(
                        blockCenter,
                        living.position(),
                        (isPush ? -1f : 2f) * renderPartialTicks);

                //todo, clean up all the unnecessary calculations once we find what feels good at run time
                Vector3d normalize = direction.normalize();

                double shortenFactor = isPush ? 0.2 : 0.4;
                Vector3d add = living.getDeltaMovement().add(normalize.multiply(shortenFactor, shortenFactor, shortenFactor));

                //don't let the motion go crazy large
                living.setDeltaMovement(VectorHelper.ClampMagnitude(add, 1));

                //let people get damaged but not too much?
                //todo check what a good max fall distance would be
                //todo add to config
                if (living.fallDistance > 3)
                {
                    living.fallDistance = Math.min(living.fallDistance, 3);
                }
            }
            else
            {
                //remove blocks that are out of distance
                blocks.remove(i);
            }
        }
        living.hurtMarked = true;
    }

    private static List<Vector3d> found = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    public static List<Vector3d> getDrawLines(int range)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity playerEntity = mc.player;
        //only update box list every so often
        if (playerEntity.tickCount % 5 != 0 && found.size() > 0)
        {
            return found;
        }

        found.clear();

        //find all the things that we want to draw a line to from the player

        //todo stop aluminum showing up, check IHasMetalType.getMetalType != aluminum

        //metal blocks
        BlockPos.withinManhattanStream(playerEntity.blockPosition(), range, range, range)
                .filter(blockPos ->
                {
                    Block block = playerEntity.level.getBlockState(blockPos).getBlock();

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
                    found.add(entity.position());
                }
                else if (item instanceof IHasMetalType)
                {
                    found.add(entity.position());
                }
            }
        });

        return found;
    }


}
