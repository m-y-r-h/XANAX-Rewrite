package cat.yoink.xanax.internal.traits;

public final class Timer
{
    private int ticks;

    public Timer() { }

    public Timer(int ticks)
    {
        this.ticks = ticks;
    }

    public void tick()
    {
        ticks++;
    }

    public boolean hasPassed(int ticks)
    {
        return this.ticks > ticks;
    }

    public int getTicks()
    {
        return ticks;
    }
}
