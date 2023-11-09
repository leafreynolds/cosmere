package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.spiritweb.ISpiritweb;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AllomancyEntityThread implements Runnable
{
    public static boolean serverShutdown = false;
    public static final Lock lock = new ReentrantLock();
    public final ISpiritweb data;
    public boolean isRunning = true;

    public AllomancyEntityThread(ISpiritweb data)
    {
        this.data = data;
    }

    @Override
    public void run(){  }
}
