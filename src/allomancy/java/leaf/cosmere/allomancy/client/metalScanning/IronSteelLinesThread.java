package leaf.cosmere.allomancy.client.metalScanning;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static leaf.cosmere.allomancy.common.manifestation.AllomancyIronSteel.*;

public class IronSteelLinesThread implements Runnable {
    private static IronSteelLinesThread INSTANCE;
    private static Thread t;
    private static final Lock lock = new ReentrantLock();
    private static ScanResult scanResult = new ScanResult();
    private static int scanRange = 0;
    private static boolean isStopping = false;

    public static IronSteelLinesThread getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new IronSteelLinesThread();
        }

        return INSTANCE;
    }

    public ScanResult requestScanResult()
    {
        lock.lock();
        try
        {
            return scanResult;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            lock.unlock();
            return new ScanResult();	// empty ScanResult so it doesn't crash
        }
    }

    public void releaseScanResult()
    {
        lock.unlock();
    }

    public void setScanRange(int range)
    {
        scanRange = range;
    }

    public void start()
    {
        if (t == null || isStopping)
        {
            CosmereAPI.logger.info("Starting lines thread");
            t = new Thread(this, "lines_thread");
            isStopping = false;
            t.start();
        }
    }

    public void stop()
    {
        if (t != null && !isStopping)
        {
            isStopping = true;
        }
    }

    private void setScanResult(ScanResult result)
    {
        scanResult = result;
    }

    // this should be threaded to avoid lag spikes on the render thread when flaring metals
    @Override
    @OnlyIn(Dist.CLIENT)
    public void run()
    {
        final Minecraft mc = Minecraft.getInstance();
        TagKey<Block> aluminumOre = CosmereTags.Blocks.METAL_ORE_BLOCK_TAGS.get(Metals.MetalType.ALUMINUM);
        TagKey<Block> aluminumStorage = CosmereTags.Blocks.METAL_BLOCK_TAGS.get(Metals.MetalType.ALUMINUM);
        // these two below may or may not work, better to keep them than remove
        TagKey<Block> aluminumSheet = BlockTags.create(new ResourceLocation("sheetmetals/aluminum"));
        TagKey<Block> aluminumWire = BlockTags.create(new ResourceLocation("wires/aluminum"));
        while (!isStopping) {
            try
            {

                ScanResult nextScan;
                LocalPlayer playerEntity = mc.player;
                nextScan = new ScanResult();
                // todo: add configurable tick rate for this thread

                //find all the things that we want to draw a line to from the player
                //metal blocks
                {
                    BlockPos.withinManhattanStream(playerEntity.blockPosition(), scanRange, scanRange, scanRange)
                            .filter(blockPos ->
                            {
                                Block block = playerEntity.level.getBlockState(blockPos).getBlock();
                                final boolean validMetalBlock = block instanceof IHasMetalType iHasMetalType && iHasMetalType.getMetalType() != Metals.MetalType.ALUMINUM;
                                boolean isGood = validMetalBlock || containsMetal(block);

                                if (isGood)
                                {
                                    try
                                    {
                                        Player player = Minecraft.getInstance().player;
                                        Level level = Minecraft.getInstance().level;
                                        // if level is null, the player has no world loaded, so stop
                                        if (mc.level == null)
                                        {
                                            stop();
                                            return false;
                                        }
                                        Vec3 currVec = player.getEyePosition();
                                        Vec3 endPos = new Vec3(blockPos.getX() + 0.5F, blockPos.getY() + 0.5F, blockPos.getZ() + 0.5F);
                                        Vec3 endFloorVec = new Vec3(Math.floor(endPos.x), Math.floor(endPos.y), Math.floor(endPos.z));
                                        double resistance = 0.0F;

                                        // linear interpolation to see if the block is obscured by blocks
                                        int loopTimes = (int) Math.ceil(currVec.distanceTo(endPos));
                                        for (int i = 0; i < loopTimes; i++)
                                        {
                                            BlockState bState = Objects.requireNonNull(level.getBlockState(new BlockPos(currVec)));
                                            Vec3 currFloorVec = new Vec3(Math.floor(currVec.x), Math.floor(currVec.y), Math.floor(currVec.z));

                                            if (currFloorVec.equals(endFloorVec) || resistance >= 1.0F)
                                            {
                                                break;
                                            }

                                            Block currBlock = level.getBlockState(new BlockPos(currVec)).getBlock();

                                            if (bState.is(aluminumOre) || bState.is(aluminumStorage) || bState.is(aluminumSheet) || bState.is(aluminumWire) || (currBlock instanceof IHasMetalType iHasMetalType && iHasMetalType.getMetalType() == Metals.MetalType.DURALUMIN))
                                            {
                                                // aluminum completely blocks steelsight
                                                resistance += 1.0F;
                                            }
                                            else
                                            {
                                                resistance += (materialResistanceMap.containsKey(bState.getMaterial())) ? materialResistanceMap.get(bState.getMaterial()) : 0.0F;
                                            }

                                            double distance = currVec.distanceTo(endPos);
                                            currVec = currVec.lerp(endPos, 1.0F / distance);
                                        }

                                        isGood = resistance < 1.0F;
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();

                                        isGood = false;
                                    }
                                }

                                return isGood;
                            })
                            .forEach(blockPos -> nextScan.addBlock(blockPos.immutable()));

                    nextScan.finalizeClusters();
                }

                //entities with metal armor/tools
                {
                    EntityHelper.getEntitiesInRange(playerEntity, scanRange, false).forEach(entity ->
                    {
                        if (entityContainsMetal(entity)) {
                            double resistance = 0.0F;
                            try
                            {
                                Player player = Minecraft.getInstance().player;
                                Level level = Minecraft.getInstance().level;
                                // if level is null, the player has no world loaded, so stop
                                if (mc.level == null)
                                {
                                    stop();
                                    return;
                                }
                                Vec3 currVec = player.getEyePosition();
                                Vec3 endPos = new Vec3(entity.getX(), entity.getY(), entity.getZ());

                                // linear interpolation to see if the entity is obscured by blocks
                                int loopTimes = (int) Math.ceil(currVec.distanceTo(endPos));
                                for (int i = 0; i < loopTimes; i++)
                                {
                                    BlockState bState = Objects.requireNonNull(level.getBlockState(new BlockPos(currVec)));

                                    final boolean pastEntity = (player.getEyePosition().distanceTo(currVec) >= player.getEyePosition().distanceTo(endPos));

                                    if (pastEntity || resistance >= 1.0F)
                                    {
                                        break;
                                    }

                                    Block currBlock = level.getBlockState(new BlockPos(currVec)).getBlock();

                                    if (bState.is(aluminumOre) || bState.is(aluminumStorage) || bState.is(aluminumSheet) || bState.is(aluminumWire) || (currBlock instanceof IHasMetalType iHasMetalType && iHasMetalType.getMetalType() == Metals.MetalType.DURALUMIN))
                                    {
                                        // aluminum completely blocks steelsight
                                        resistance += 1.0F;
                                    }
                                    else
                                    {
                                        resistance += (materialResistanceMap.containsKey(bState.getMaterial())) ? materialResistanceMap.get(bState.getMaterial()) : 0.0F;
                                    }

                                    double distance = currVec.distanceTo(endPos);
                                    currVec = currVec.lerp(endPos, 1.0F / distance);

                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                resistance = 100.0F;    // just to be sure :)
                            }

                            if (resistance < 1.0F)
                            {
                                nextScan.foundEntities.add(
                                        entity.position().add(
                                                0,
                                                entity.getBoundingBox().getYsize() / 2,
                                                0));
                            }
                        }
                    });
                }

                lock.lock();
                try
                {
                    setScanResult(nextScan);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    lock.unlock();
                }
            }
            catch (Exception e)
            {
                CosmereAPI.logger.info("Issue with lines thread");
                e.printStackTrace();
            }
        }
    }
}
