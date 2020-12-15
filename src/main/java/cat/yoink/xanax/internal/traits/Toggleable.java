package cat.yoink.xanax.internal.traits;

/**
 * @author yoink
 */
public interface Toggleable
{
    void toggle();

    void setState(boolean state);

    boolean getState();
}
