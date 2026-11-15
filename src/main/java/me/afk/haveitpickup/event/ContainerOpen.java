package me.afk.haveitpickup.event;

import me.afk.haveitpickup.ClientConfig;
import me.afk.haveitpickup.HaveItPickup;
import me.afk.haveitpickup.gui.widget.AButton;
import me.afk.haveitpickup.gui.widget.WhiteListButton;
import me.afk.haveitpickup.util.ContainerSlotHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HaveItPickup.MODID, value = Dist.CLIENT)
public class ContainerOpen {

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        // 判断当前 Screen 是否是容器界面（箱子、木桶、熔炉等都继承自 AbstractContainerScreen）
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> containerScreen)) {
            return;
        }

        String screenKey = ContainerSlotHelper.getScreenKey(containerScreen);

        // 打印 ID
        if (ClientConfig.isDebugPrintMode) {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            if (player != null && screenKey != null) {
                minecraft.player.displayClientMessage(Component.literal(screenKey).withStyle(style -> style.withClickEvent(
                                new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, screenKey)
                        ))
                        .withStyle(ChatFormatting.AQUA), false);
            }
        }

        // 防御
        if (!ClientConfig.isInWhitelist(screenKey)) return;
        // --- 获取玩家实例 ---
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        // 创建按钮
        String key = screenKey;

//        WhiteListButton whiteListButton = new WhiteListButton(0, 0, 16, 16, 16, 16, key, btn -> {});
//        event.addListener(whiteListButton);
        AButton aButton = new AButton(0, 0, 16, 16, key, btn -> {});
        event.addListener(aButton);

        // 位置计算
        int[] xy = ClientConfig.getButtonOffset(screenKey);
        if (xy == null) {
            xy = ContainerSlotHelper.buildButtonAnchor(containerScreen, player);
            ClientConfig.setButtonOffset(screenKey, xy[0], xy[1]);
        }

        aButton.setX(xy[0]);
        aButton.setY(xy[1]);
//        whiteListButton.setX(buttonX);
//        whiteListButton.setY(guiTop);
    }
}
