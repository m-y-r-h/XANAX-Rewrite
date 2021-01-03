package cat.yoink.xanax.internal.feature.other;

import cat.yoink.xanax.internal.traits.interfaces.Configurable;
import cat.yoink.xanax.internal.traits.manager.impl.ListRegistry;
import cat.yoink.xanax.internal.util.FileUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.File;

/**
 * @author yoink
 */
public final class FriendManager extends ListRegistry<String> implements Configurable
{
    public static final FriendManager INSTANCE = new FriendManager();

    private FriendManager() { }

    public boolean isFriend(String name)
    {
        return getRegistry().stream().anyMatch(name::equalsIgnoreCase);
    }

    @Override
    public boolean save(String name)
    {
        JsonArray json = new JsonArray();
        getRegistry().forEach(json::add);
        return FileUtil.saveFile(new File(directory.getAbsolutePath() + File.separator + "main", "Friends.json"), json.toString());
    }

    @Override
    public boolean load(String name)
    {
        String contents = FileUtil.getContents(new File(directory.getAbsolutePath() + File.separator + "main", "Friends.json"));
        if (contents.equals("")) return false;
        JsonArray json = new JsonParser().parse(contents).getAsJsonArray();
        json.forEach(friend -> register(friend.getAsString()));
        return true;
    }
}
