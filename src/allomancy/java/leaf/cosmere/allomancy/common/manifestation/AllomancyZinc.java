/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllomancyZinc extends AllomancyManifestation
{
    private static final HashMap<String, ZincThread> playerThreadMap = new HashMap<>();
    public AllomancyZinc(Metals.MetalType metalType) {
        super(metalType);
    }

    //Inflames Emotions
    //make hostiles target you but also make non-hostiles target hostiles?
    @Override
    protected void applyEffectTick(ISpiritweb data)
	{
        int mode = getMode(data);

        String uuid = data.getLiving().getStringUUID();
        if (mode > 0 && !playerThreadMap.containsKey(uuid))
        {
            playerThreadMap.put(uuid, new ZincThread(data));
        }

        playerThreadMap.entrySet().removeIf(entry -> !entry.getValue().isRunning);
    }

    class ZincThread implements Runnable
    {
        private final ISpiritweb data;
        public boolean isRunning = true;

		public ZincThread(ISpiritweb data)
		{
			this.data = data;

            Thread t = new Thread(this, "zinc_thread_" + data.getLiving().getDisplayName().getString());
            t.start();
		}

        @Override
        public void run()
		{
            while (true)
            {
                try
                {
                    int mode = getMode(data);
                    int range = getRange(data);

                    // check if zinc is off or compounding
                    if (mode <= 0)
                    {
                        break;
                    }

                    // this is the only way to check if the player is still online, thanks forge devs
                    if (data.getLiving().level.getServer().getPlayerList().getPlayer(data.getLiving().getUUID()) == null)
                    {
                        break;
                    }

                    List<LivingEntity> entitiesToAffect = EntityHelper.getLivingEntitiesInRange(data.getLiving(), range, true);

                    for (LivingEntity e : entitiesToAffect)
                    {
                        if (e instanceof Mob mob)
                        {

                            //mob.targetSelector.enableFlag(Goal.Flag.TARGET);
                            mob.setNoAi(false);

                            switch (mode)
                            {
                                case 3:
                                    if (mob.getTarget() == null)
                                    {
                                        LivingEntity attackTarget = entitiesToAffect.get(MathHelper.RANDOM.nextInt(entitiesToAffect.size()));
                                        mob.setTarget(attackTarget);
                                    }
                                case 2:
                                    if (mob.getLastHurtByMob() == null)
                                    {
                                        mob.setLastHurtByMob(mob.getTarget() != null
                                                ? mob.getTarget()
                                                : entitiesToAffect.get(MathHelper.RANDOM.nextInt(entitiesToAffect.size())));
                                    }

                                case 1:
                                default:
                                    mob.setAggressive(true);
                            }
                        }
                    }

                    // sleep thread for 1 tick (50ms)
                    Thread.sleep(50);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            isRunning = false;
        }
    }
}
