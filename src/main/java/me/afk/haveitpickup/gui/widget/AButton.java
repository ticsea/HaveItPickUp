package me.afk.haveitpickup.gui.widget;

import me.afk.haveitpickup.Config;
import me.afk.haveitpickup.HaveItPickup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import org.jetbrains.annotations.NotNull;

public class AButton extends ExtendedButton {
    // 1. 背景图（没有眼睛的纯黑底图）
    public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(HaveItPickup.MODID, "textures/button/button_background.png");
    // 2. 眼睛图层（透明背景，只有紫色眼睛）
    public static final ResourceLocation EYES = ResourceLocation.fromNamespaceAndPath(HaveItPickup.MODID, "textures/button/button_eyes.png");
    public static final ResourceLocation BUTTON_OFF = ResourceLocation.fromNamespaceAndPath(HaveItPickup.MODID, "textures/button/button_off.png");
    private final int texWidth;
    private final int texHeight;
    private final String key;

    public AButton(int x, int y, int width, int height, String key , OnPress onPress) {
        super(x, y, width, height, Component.empty(), btn ->
        {
            // 点击时，改变全局状态
            Config.toggle();
            // 然后执行外部传进来的逻辑
            onPress.onPress(btn);
        }, DEFAULT_NARRATION);
        this.texWidth = width;
        this.texHeight = height;
        this.key = key;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (Config.isOn()) {
            // 1. 画出底图
            guiGraphics.blit(BACKGROUND, this.getX(), this.getY(), 0, 0, this.width, this.height, texWidth, texHeight);

            // 2. 计算鼠标偏移量
            float centerX = this.getX() + this.width / 2.0F;
            float centerY = this.getY() + this.height / 2.0F;

            float deltaX = (mouseX - centerX) / (this.width / 2.0F);
            float deltaY = (mouseY - centerY) / (this.height / 2.0F);

            // 3. 设置移动幅度（比如最多移动 2 个像素，因为眼睛是独立图层，可以稍微大胆一点）
            int maxOffset = 2;
            int offsetX = Mth.clamp((int) (deltaX * maxOffset), -maxOffset, maxOffset);
            int offsetY = Mth.clamp((int) (deltaY * maxOffset), -maxOffset, maxOffset);

            // 如果鼠标移开，让眼睛回到中间
        /*if (!this.isHovered()) {
            offsetX = 0;
            offsetY = 0;
        }*/

            // 4. 将整个“眼睛图层”按偏移量画上去
            // 因为我们把眼睛单独放在了一张图上，直接整体偏移即可，不需要裁剪（Scissor）！
            guiGraphics.blit(EYES, this.getX() + offsetX, this.getY() + offsetY, // 屏幕坐标加上偏移
                    0, 0,                                         // 从图层的 (0,0) 开始画
                    this.width, this.height,                      // 画多大
                    texWidth, texHeight);                     // 贴图总大小

            // 5. 悬停高亮（可选）
            if (this.isHovered) {
                guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x33FFFFFF);
            }
        } else {
            guiGraphics.blit(BUTTON_OFF, this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), texWidth, texHeight);
        }
    }

}
