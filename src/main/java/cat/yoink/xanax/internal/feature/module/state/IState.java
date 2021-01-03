package cat.yoink.xanax.internal.feature.module.state;

/**
 * @author yoink
 */
public interface IState
{
    default void onEnable() {}

    default void onDisable() {}
}
