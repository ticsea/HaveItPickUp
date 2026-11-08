package me.afk.haveitpickup.event;

import me.afk.haveitpickup.Config;
import me.afk.haveitpickup.HaveItPickup;
import me.afk.haveitpickup.gui.widget.AButton;
import me.afk.haveitpickup.gui.widget.WhiteListButton;
import me.afk.haveitpickup.util.WhiteListUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
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

        String screenKey = WhiteListUtil.getScreenKey(containerScreen);
        if (Config.isDebugPrintMode) {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            if (player != null && screenKey != null) {
                minecraft.player.displayClientMessage(Component.literal(screenKey).withStyle(style -> style.withClickEvent(
                                new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, screenKey)
                        ))
                        .withStyle(ChatFormatting.AQUA), false);
            }
        }

        // 创建按钮
        String key = screenKey;

        WhiteListButton whiteListButton = new WhiteListButton(0, 0, 16, 16, 16, 16, key, btn -> {});
        AButton aButton = new AButton(0, 0, 16, 16, key, btn -> {});

        if (Config.isInWhitelist(screenKey)) {
            event.addListener(aButton);
//            event.addListener(whiteListButton);
        } else {
            return;
        }

        // 获取容器界面的矩形信息
        int guiLeft = containerScreen.getGuiLeft();
        int guiTop = containerScreen.getGuiTop();
        int guiWidth = containerScreen.getXSize();
        int guiHeight = containerScreen.getYSize();

        // 1. X轴：绝对是容器的水平居中
        int buttonX = guiLeft + (guiWidth - aButton.getWidth()) / 2;

        aButton.setX(buttonX);
        aButton.setY(guiTop + guiHeight - 100);
        whiteListButton.setX(buttonX);
        whiteListButton.setY(guiTop);
    }
}
