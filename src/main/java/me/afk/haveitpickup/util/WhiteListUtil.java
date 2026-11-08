package me.afk.haveitpickup.util;

import me.afk.haveitpickup.Config;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.Set;

public class WhiteListUtil {
    private static final Set<Class<?>> NON_STORAGE_SCREENS = Set.of(
            EnchantmentScreen.class,        // 附魔台
            AnvilScreen.class,              // 铁砧
            CraftingScreen.class,           // 工作台
            GrindstoneScreen.class,         // 砂轮
            LoomScreen.class,               // 织布机
            CartographyTableScreen.class,   // 制图台
            SmithingScreen.class,           // 锻造台
            StonecutterScreen.class,        // 切石机
            BrewingStandScreen.class,       // 酿造台
            BeaconScreen.class,             // 信标
            FurnaceScreen.class,
            BlastFurnaceScreen.class,
            DispenserScreen.class,
            CreativeModeInventoryScreen.class,
            InventoryScreen.class
    );



    public static String getScreenKey(AbstractContainerScreen<?> containerScreen) {
        // 1. 防御性判空
        if (containerScreen == null) {
            return null;
        }

        // 2. 提前拦截原版玩家背包（无需查注册表，且原版 InventoryMenu 会抛异常）
        if (containerScreen.getMenu() instanceof InventoryMenu) {
            return null;
        }

        // 3. 尝试获取 MenuType
        try {
            MenuType<?> menuType = containerScreen.getMenu().getType();
            ResourceLocation menuId = BuiltInRegistries.MENU.getKey(menuType);
            if (menuId != null) {
                return menuId.toString();
            }
        } catch (UnsupportedOperationException e) {
            // 兜底：某些特殊模组容器没有实现 getType()
            return null;
        }

        return null;
    }

    public static boolean isInBlackList(Class<?> container) {
        return NON_STORAGE_SCREENS.contains(container);
        //todo handle enderchest.
    }
}
