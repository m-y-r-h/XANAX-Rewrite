package cat.yoink.xanax.internal.feature.other;

import cat.yoink.xanax.internal.traits.manager.impl.MapRegistry;
import cat.yoink.xanax.internal.util.WebUtil;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

/**
 * @author yoink
 */
public final class CapeManager extends MapRegistry<String, ResourceLocation>
{
    public static final CapeManager INSTANCE = new CapeManager("https://raw.githubusercontent.com/Katatje/XANAX-Rewrite/master/capes.txt");

    private CapeManager(String url)
    {
        String content = WebUtil.getContent(url);
        Arrays.stream(content.split("\n")).map(s -> s.split(":")).forEach(strings -> register(strings[0], new ResourceLocation(strings[1])));
    }
}
