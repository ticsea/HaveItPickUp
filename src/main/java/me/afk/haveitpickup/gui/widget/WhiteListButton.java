package me.afk.haveitpickup.gui.widget;

import me.afk.haveitpickup.Config;
import me.afk.haveitpickup.HaveItPickup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import org.jetbrains.annotations.NotNull;

public class WhiteListButton extends ExtendedButton {

    private static final ResourceLocation TEXTURE_ALLOWED = ResourceLocation.fromNamespaceAndPath(HaveItPickup.MODID, "textures/button/button_allowed.png");
    private static final ResourceLocation TEXTURE_NOT_ALLOWED = ResourceLocation.fromNamespaceAndPath(HaveItPickup.MODID, "textures/button/button_not_allowed.png");
    private final int texWidth;
    private final int texHeight;
    private final String key;

    public WhiteListButton(int x, int y, int width, int height, int texWidth, int texHeight, String key, OnPress onPress) {
        super(x, y, width, height, Component.empty(), btn -> {
            if (Config.isInWhitelist(key)) {
                Config.removeFromWhitelist(key);
            } else {
                Config.addToWhitelist(key);
            }
        }, DEFAULT_NARRATION);
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.key = key;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // 每次渲染时，向外部询问当前状态，决定用哪张贴图
        ResourceLocation currentTexture = Config.isInWhitelist(key) ? TEXTURE_ALLOWED : TEXTURE_NOT_ALLOWED;

        // 画出贴图
        guiGraphics.blit(
                currentTexture,
                this.getX(), this.getY(),
                0, 0,
                this.width, this.height,
                this.texWidth, this.texHeight
        );

        // 悬停高亮
        if (this.isHoveredOrFocused()) {
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x33FFFFFF);
        }
    }
}
