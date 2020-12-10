package cat.yoink.xanax.internal.util;

import cat.yoink.xanax.internal.traits.Minecraft;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public final class InventoryUtil implements Minecraft
{
    public static int getHotbarSlot(Item item)
    {
        for (int i = 0; i < 9; i++)
        {
            Item item1 = mc.player.inventory.getStackInSlot(i).getItem();

            if (item.equals(item1)) return i;
        }
        return -1;
    }

    public static int getHotbarSlot(Block block)
    {
        for (int i = 0; i < 9; i++)
        {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();

            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block)) return i;
        }

        return -1;
    }
}
