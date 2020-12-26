package cat.yoink.xanax.internal.friend;

import cat.yoink.xanax.internal.traits.interfaces.Configurable;
import cat.yoink.xanax.internal.traits.manager.Register;
import cat.yoink.xanax.internal.util.FileUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.File;

/**
 * @author yoink
 */
public final class FriendManager extends Register<String> implements Configurable
{
    public static final FriendManager INSTANCE = new FriendManager();

    private FriendManager() { }

    public void addFriend(String name)
    {
        add(name);
    }

    @Override
    public boolean save(String name)
    {
        JsonArray json = new JsonArray();
        getRegistry().forEach(json::add);
        return FileUtil.saveFile(new File(directory.getAbsolutePath() + File.separator + name, "Friends.json"), json.toString());
    }

    @Override
    public boolean load(String name)
    {
        String contents = FileUtil.getContents(new File(directory.getAbsolutePath() + File.separator + name, "Friends.json"));
        if (contents.equals("")) return false;
        JsonArray json = new JsonParser().parse(contents).getAsJsonArray();
        json.forEach(friend -> add(friend.getAsString()));
        return true;
    }
}
