package me.afk.haveitpickup.util;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public class ScrrenUtils {


    /**
     * 屏幕槽位坐标计算工具类。
     * 提供将 {@link Slot} 的相对坐标转换为实际屏幕绝对坐标的方法。
     */
    public class GuiSlotHelper {

        /**
         * 通过玩家背包容器索引（Slot 内部的 index）查找对应的屏幕绝对 X 坐标。
         * <p>
         * <b>背景说明：</b><br>
         * 在 Minecraft 的 GUI 系统中，寻找某个槽位的屏幕位置分为两步：
         * <ol>
         *     <li><b>确定 Menu 索引：</b> 必须知道该槽位在当前 {@link AbstractContainerMenu}
         *         的 {@code slots} 列表中的位置（即 {@code addSlot} 的调用顺序）。</li>
         *     <li><b>计算绝对坐标：</b> 槽位自带 {@code slot.x} 和 {@code slot.y} 属性，
         *         这代表它相对于 GUI 背景左上角的相对坐标。通过加上 {@code guiLeft} 和 {@code guiTop}，
         *         即可得到最终的屏幕绝对坐标。</li>
         * </ol>
         * </p>
         *
         * <b>如何获取该槽位？</b><br>
         * 此方法通过遍历 {@code menu.slots}，并比对 {@link Slot#container}（是否为玩家背包）
         * 以及 {@link Slot#getContainerSlot()}（该槽在容器内的相对索引）来精准定位。
         * 玩家背包的主物品栏索引范围是 9-35，快捷栏是 0-8。
         *
         * @param screen         当前的容器 GUI 界面，用于获取 {@code guiLeft} 和 {@code guiTop}。
         * @param targetContainerSlotIndex 目标槽位在玩家背包（Inventory）内的索引。
         *                                 例如：主背包第一格传 {@code 9}，快捷栏第一格传 {@code 0}。
         * @return 目标槽位左上角在屏幕上的绝对 X 坐标。如果未找到该槽位，返回 {@code -1}。
         */
        public static int getPlayerSlotScreenX(AbstractContainerScreen<?> screen, int targetContainerSlotIndex) {
            Slot slot = findPlayerSlot(screen.getMenu(), targetContainerSlotIndex);
            if (slot == null) return -1;

            return screen.getGuiLeft() + slot.x;
        }

        /**
         * 通过玩家背包容器索引查找对应的屏幕绝对 Y 坐标。
         * <p>
         * 核心逻辑同 {@link #getPlayerSlotScreenX(AbstractContainerScreen, int)}。
         * </p>
         *
         * @param screen         当前的容器 GUI 界面。
         * @param targetContainerSlotIndex 目标槽位在玩家背包内的索引（如 9-35 为主背包，0-8 为快捷栏）。
         * @return 目标槽位左上角在屏幕上的绝对 Y 坐标。如果未找到，返回 {@code -1}。
         */
        public static int getPlayerSlotScreenY(AbstractContainerScreen<?> screen, int targetContainerSlotIndex) {
            Slot slot = findPlayerSlot(screen.getMenu(), targetContainerSlotIndex);
            if (slot == null) return -1;

            return screen.getGuiTop() + slot.y;
        }

        /**
         * 在菜单中查找属于玩家背包的特定槽位。
         *
         * @param menu 当前打开的容器菜单。
         * @param targetContainerSlotIndex 目标槽位在玩家背包内的索引。
         * @return 找到的 {@link Slot}，找不到则返回 {@code null}。
         */
        private static Slot findPlayerSlot(AbstractContainerMenu menu, int targetContainerSlotIndex) {
            for (Slot slot : menu.slots) {
                // 1. 过滤出属于玩家背包的槽位
                // 注意：InventoryMenu 是玩家裸背包界面，除此之外的容器界面，玩家背包槽位的 container 都是 Inventory
                if (slot.container instanceof Inventory) {
                    // 2. 比对槽位在其所属容器内的相对索引
                    if (slot.getContainerSlot() == targetContainerSlotIndex) {
                        return slot;
                    }
                }
            }
            return null;
        }
    }
}
