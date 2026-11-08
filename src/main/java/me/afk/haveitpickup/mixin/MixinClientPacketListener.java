package me.afk.haveitpickup.mixin;

import me.afk.haveitpickup.Config;
import me.afk.haveitpickup.util.WhiteListUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    @Inject(method = "handleContainerContent", at = @At("TAIL"))
    private void moveItem(ClientboundContainerSetContentPacket pPacket, CallbackInfo ci) {
        var mc = Minecraft.getInstance();
        var items = pPacket.getItems();
        Player player = mc.player;
        MultiPlayerGameMode gameMode = mc.gameMode;

        if (mc.screen instanceof AbstractContainerScreen<?> containerScreen) {
            String screenKey = WhiteListUtil.getScreenKey(containerScreen);
            if (!Config.isInWhitelist(screenKey)) return;
        }

        if (!Config.isOn() || player == null || gameMode == null) return;


        Map<Item, CompoundTag> playerInventoryItems = new HashMap<>();
        player.getInventory().items.forEach(itemStack -> playerInventoryItems.put(itemStack.getItem(), itemStack.getTag()));

        for (int i = 0; i < items.size() - 36; ++i) {
            ItemStack itemStack = items.get(i);
            Item item = itemStack.getItem();

            if (item == Items.AIR || !playerInventoryItems.containsKey(item)) continue;

            if (itemStack.hasTag()) {
                if (!playerInventoryItems.get(item).equals(itemStack.getTag())) {
                    continue;
                }
            }

            int finalI = i;
            mc.execute(() -> gameMode.handleInventoryMouseClick(pPacket.getContainerId(), finalI,  0, ClickType.QUICK_MOVE, player));
        }
    }
}
