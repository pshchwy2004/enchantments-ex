package com.pshchwy.enex.menu.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.enchantment.EXEnchantmentMap;
import com.pshchwy.enex.item.EXItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.Objects;

public class StampingTableScreen extends AbstractContainerScreen<StampingTableMenu> {
    public static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "textures/gui/container/stamping_table/stamping_table.png");
    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/scroller");
    private static final ResourceLocation SCROLLER_DISABLED_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/scroller_disabled");
    private static final ResourceLocation ENCHANTMENT_SLOT_DISABLED_SPRITE = ResourceLocation.fromNamespaceAndPath(
            EnchantmentsEX.MOD_ID,
            "container/stamping_table/enchantment_slot_disabled"
    );
    private static final ResourceLocation ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE = ResourceLocation.fromNamespaceAndPath(
            EnchantmentsEX.MOD_ID,
            "container/stamping_table/enchantment_slot_highlighted"
    );
    private static final ResourceLocation ENCHANTMENT_SLOT_SPRITE = ResourceLocation.fromNamespaceAndPath(
            EnchantmentsEX.MOD_ID,
            "container/stamping_table/enchantment_slot"
    );
    private static final int LIST_X = 59;
    private static final int LIST_Y = 14;
    private static final int BUTTON_WIDTH = 96;
    private static final int BUTTON_HEIGHT = 19;
    private static final int VISIBLE_ROWS = 3;

    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;


    public StampingTableScreen(StampingTableMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = this.leftPos;
        int y = this.topPos;

        // main GUI plate layout
        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        // scrollbar
        int scrollbarX = x + 158;
        int scrollbarYTop = y + 14;
        int scrollbarHeight = 57;
        int scrollTrackLength = scrollbarHeight - 15;
        int thumbY = scrollbarYTop + (int)(this.scrollOffs * (float)scrollTrackLength);

        ResourceLocation scrollSprite = this.isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
        guiGraphics.blitSprite(scrollSprite, scrollbarX, thumbY, 12, 15);

        // render text entries
        List<Holder<Enchantment>> available = this.menu.getAvailableEnchantments();
        this.startIndex = (int)((double)(this.scrollOffs * (float)this.getScrollRowLength()) + 0.5);

        int renderX = x + LIST_X;
        int renderY = y + LIST_Y;
        int maxIndexToRender = this.startIndex + VISIBLE_ROWS;

        // check for ink
        boolean hasInk = !this.menu.getSlot(1).getItem().isEmpty() && this.menu.getSlot(1).getItem().is(EXItems.MOLTEN_INK);

        for (int i = this.startIndex; i < maxIndexToRender && i < available.size(); i++) {
            int currentRenderRow = i - this.startIndex;
            int itemY = renderY + currentRenderRow * BUTTON_HEIGHT;

            boolean isHovered = mouseX >= renderX && mouseX < renderX + BUTTON_WIDTH && mouseY >= itemY && mouseY < itemY + BUTTON_HEIGHT;

            // button sprite
            ResourceLocation buttonSprite;
            int textColor;

            if (!hasInk) {
                // disabled sprite
                buttonSprite = ENCHANTMENT_SLOT_DISABLED_SPRITE;
                textColor = 0xA0A0A0; // Grayed-out text
            } else if (isHovered) {
                // highlighted sprite
                buttonSprite = ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE;
                textColor = 0xFFFF55; // Yellow text
            } else {
                // standard sprite
                buttonSprite = ENCHANTMENT_SLOT_SPRITE;
                textColor = 0xFFFFFF; // White text
            }

            // draw the button
            guiGraphics.blitSprite(buttonSprite, renderX, itemY, BUTTON_WIDTH, BUTTON_HEIGHT);

            // 3. Gather and format the text name
            Holder<Enchantment> currentEnchant = available.get(i);
            var storedEnchants = this.menu.getSlot(0).getItem().getOrDefault(net.minecraft.core.component.DataComponents.STORED_ENCHANTMENTS, net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY);
            int currentLevel = storedEnchants.getLevel(currentEnchant);

            Component displayName = currentEnchant.value().description();
            if (this.minecraft != null && this.minecraft.level != null) {
                var optKey = currentEnchant.unwrapKey();
                if (optKey.isPresent()) {
                    ResourceKey<Enchantment> exKey = EXEnchantmentMap.getUpgrade(optKey.get());
                    var registry = this.minecraft.level.registryAccess().registry(Registries.ENCHANTMENT);
                    if (registry.isPresent() && registry.get().get(exKey) != null) {
                        displayName = Objects.requireNonNull(registry.get().get(exKey)).description();
                    }
                }
            }

            // Correctly format the Roman numerals dynamically using vanilla's localization system
            Component finalRenderText;

            // A cleaner, foolproof way if currentLevel is guaranteed to be >= 1:
            if (currentLevel > 0) {
                finalRenderText = Component.translatable("enchantment.level." + currentLevel, displayName);
            } else {
                // Fallback display if level reading failed or equals 0
                finalRenderText = Component.empty();
            }

            // 4. draw text
            guiGraphics.drawString(this.font, displayName.getString() + " " + finalRenderText.getString(), renderX + 5, itemY + 5, textColor, true);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;
        int x = this.leftPos;
        int y = this.topPos;

        if (this.isScrollBarActive() && mouseX >= (double)(x + 158) && mouseX < (double)(x + 169) && mouseY >= (double)(y + 14) && mouseY < (double)(y + 70)) {
            this.scrolling = true;
            return true;
        }

        int listLeft = x + LIST_X;
        int listTop = y + LIST_Y;
        List<Holder<Enchantment>> available = this.menu.getAvailableEnchantments();

        for (int i = 0; i < VISIBLE_ROWS; i++) {
            double relativeMouseX = mouseX - (double)(listLeft);
            double relativeMouseY = mouseY - (double)(listTop + i * BUTTON_HEIGHT);

            if (relativeMouseX >= 0.0 && relativeMouseX < (double)BUTTON_WIDTH && relativeMouseY >= 0.0 && relativeMouseY < (double)BUTTON_HEIGHT) {
                int clickedIndex = this.startIndex + i;
                if (clickedIndex >= 0 && clickedIndex < available.size()) {
                    if (this.minecraft != null && this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, clickedIndex);
                    }
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int trackTop = this.topPos + 14;
            int trackBottom = trackTop + 57;

            this.scrollOffs = ((float)mouseY - (float)trackTop - 7.5F) / ((float)(trackBottom - trackTop) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.isScrollBarActive()) {
            int rowLength = this.getScrollRowLength();
            float step = (float)scrollY / (float)rowLength;
            this.scrollOffs = Mth.clamp(this.scrollOffs - step, 0.0F, 1.0F);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private boolean isScrollBarActive() {
        return this.menu.getAvailableEnchantments().size() > VISIBLE_ROWS;
    }

    private int getScrollRowLength() {
        return this.menu.getAvailableEnchantments().size() - VISIBLE_ROWS;
    }
}
