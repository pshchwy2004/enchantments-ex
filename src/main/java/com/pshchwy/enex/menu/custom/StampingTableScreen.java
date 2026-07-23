package com.pshchwy.enex.menu.custom;

import com.pshchwy.enex.EnchantmentsEX;
import com.pshchwy.enex.enchantment.EXEnchantmentMap;
import com.pshchwy.enex.item.EXItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

/**
 * This class is the client-side logic of the Stamping Table GUI, handling the frontend, visual elements of the GUI.
 * Here, GUI elements, such as menu buttons, slots, the background, and text are drafted.
 */
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
    private static final int LIST_X = 60;
    private static final int LIST_Y = 14;
    private static final int BUTTON_WIDTH = 95;
    private static final int BUTTON_HEIGHT = 19;
    private static final int VISIBLE_ROWS = 3;
    private static final int SCROLL_X = 158;
    private static final int SCROLL_Y = 14;


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

    /**
     * Rendering the background, all buttons, etc.
     */
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {

        int x = this.leftPos;
        int y = this.topPos;

        // main GUI plate layout
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);

        // scrollbar
        int scrollbarX = x + SCROLL_X;
        int scrollbarYTop = y + SCROLL_Y;
        int scrollbarHeight = 57;
        int scrollTrackLength = scrollbarHeight - 15;
        int thumbY = scrollbarYTop + (int)(this.scrollOffs * (float)scrollTrackLength);

        ResourceLocation scrollSprite = this.isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, scrollSprite, scrollbarX, thumbY, 12, 15);

        // render text entries
        List<Holder<Enchantment>> available = this.menu.getAvailableEnchantments();
        this.startIndex = (int)((double)(this.scrollOffs * (float)this.getScrollRowLength()) + 0.5);

        int renderX = x + LIST_X;
        int renderY = y + LIST_Y;
        int maxIndexToRender = this.startIndex + VISIBLE_ROWS;

        // check for ink
        boolean hasInk = !this.menu.getSlot(1).getItem().isEmpty() && this.menu.getSlot(1).getItem().is(EXItems.MOLTEN_INK);

        for (int i = this.startIndex; i < maxIndexToRender && i < available.size(); i++) { // render buttons
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
                textColor = 0xe7d05e; // yellow
            } else {
                // standard sprite
                buttonSprite = ENCHANTMENT_SLOT_SPRITE;
                textColor = 0xb6b6b6; // grey-ish
            }

            // draw the button
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, buttonSprite, renderX, itemY, BUTTON_WIDTH, BUTTON_HEIGHT);

            // gather and format the text name
            Holder<Enchantment> currentEnchant = available.get(i);
            var storedEnchants = this.menu.getSlot(0).getItem().getOrDefault(net.minecraft.core.component.DataComponents.STORED_ENCHANTMENTS, net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY);
            int currentLevel = storedEnchants.getLevel(currentEnchant);

            // --- LINE 1: Original Enchantment + Level ---
            Component ogLine = (currentLevel != 0 && currentEnchant.value().getMaxLevel() > 1)
                    ? currentEnchant.value().description().copy()
                    .append(" ")
                    .append(Component.translatable("enchantment.level." + currentLevel))
                    .withStyle(ChatFormatting.ITALIC)
                    : currentEnchant.value().description().copy().withStyle(ChatFormatting.ITALIC);

            // --- LINE 2: EX Target Upgrade Name + Level ---
            Component exLine = Component.empty();
            if (this.minecraft != null && this.minecraft.level != null) {
                var optKey = currentEnchant.unwrapKey();
                if (optKey.isPresent()) {
                    ResourceKey<Enchantment> exKey = EXEnchantmentMap.getUpgrade(optKey.get());
                    var lookup = this.minecraft.level.registryAccess().lookup(Registries.ENCHANTMENT);
                    if (lookup.isPresent()) {
                        var exHolderOpt = lookup.get().get(exKey);
                        if (exHolderOpt.isPresent()) {
                            Enchantment exEnchant = exHolderOpt.get().value();
                            Component exDesc = exEnchant.description();

                            Component levelComponent = (exEnchant.getMaxLevel() > 1 && currentLevel != 0)
                                    ? Component.literal(" ").append(Component.translatable("enchantment.level." + currentLevel))
                                    : Component.empty();

                            exLine = Component.literal("➔ ").append(exDesc).append(levelComponent);
                        }
                    }
                }
            }
            // --- TWO-LINE CENTERING MATH ---
            float lineScale = 0.70F; // Scaled so two lines fit vertically within 19px button height
            float lineSpacing = 1.0F;
            float fontHeight = this.font.lineHeight; // Standard font height is 9px
            float totalBlockHeight = (fontHeight * 2 + lineSpacing) * lineScale; // ~13.3px

            // Vertical starting position centered on the button
            float startY = itemY + (BUTTON_HEIGHT - totalBlockHeight) / 2.0F;
            float line2Y = startY + (fontHeight + lineSpacing) * lineScale;

            // Render both lines centered horizontally
            renderCenteredTextLine(guiGraphics, ogLine, renderX, startY, BUTTON_WIDTH, lineScale, textColor);
            int exTextColor;
            if (!hasInk) {
                // disabled sprite
                exTextColor = 0xA0A0A0; // Grayed-out text
            } else if (isHovered) {
                // highlighted sprite
                exTextColor = 0xFFFF55; // Yellow text
            } else {
                // standard sprite
                exTextColor = 0xFFFFFF; // White text
            }
            renderCenteredTextLine(guiGraphics, exLine, renderX, line2Y, BUTTON_WIDTH, lineScale, exTextColor);
        }
    }

    /**
     * Executes when the mouse is clicked while the screen is open. Handles mouse scrolling.
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();
        int button = mouseButtonEvent.button();
        this.scrolling = false;
        int x = this.leftPos;
        int y = this.topPos;

        if (this.isScrollBarActive() && mouseX >= (double)(x + SCROLL_X) && mouseX < (double)(x + 169) && mouseY >= (double)(y + 14) && mouseY < (double)(y + 70)) {
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

        return super.mouseClicked(mouseButtonEvent, bl);
    }

    /**
     * Executes when the mouse is dragged.
     */
    @Override
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double dragX, double dragY) {
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();
        int button = mouseButtonEvent.button();
        if (this.scrolling && this.isScrollBarActive()) {
            int trackTop = this.topPos + SCROLL_Y;
            int trackBottom = trackTop + 57;

            this.scrollOffs = ((float)mouseY - (float)trackTop - 7.5F) / ((float)(trackBottom - trackTop) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            return true;
        }
        return super.mouseDragged(mouseButtonEvent, dragX, dragY);
    }

    /**
     * Executes when scrolling input is detected.
     */
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

    /**
     * Determines if the scroll bar can be used. Used to disable needless scrolling when Enchanted Books have 3 rows or less.
     */
    private boolean isScrollBarActive() {
        return this.menu.getAvailableEnchantments().size() > VISIBLE_ROWS;
    }

    /**
     * Returns the maximum scroll row length.
     */
    private int getScrollRowLength() {
        return Math.max(0, this.menu.getAvailableEnchantments().size() - VISIBLE_ROWS);
    }

    /**
     * automatically calculates center offsets and scale down any long enchantment names so they never overflow the button borders.
     * @param guiGraphics the graphics environment
     * @param text the text component
     * @param x the x-location
     * @param y the y-location
     * @param width the width of the text
     * @param maxScale the maximum scale of the text
     * @param color the color of the text
     */
    private void renderCenteredTextLine(GuiGraphics guiGraphics, Component text, float x, float y, int width, float maxScale, int color) {
        if (text.getString().isEmpty()) return;

        float textWidth = this.font.width(text);
        float padding = 6.0F; // 3px margin on each side
        float maxAllowedWidth = width - padding;

        // Apply scaling if the text is too wide for the button
        float finalScale = maxScale;
        if (textWidth * maxScale > maxAllowedWidth) {
            finalScale = maxAllowedWidth / textWidth;
        }

        // Calculate centered starting X position
        float scaledWidth = textWidth * finalScale;
        float startX = x + (width - scaledWidth) / 2.0F;

        int argbColor = (color & 0xFF000000) == 0 ? (color | 0xFF000000) : color;

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(startX, y);
        guiGraphics.pose().scale(finalScale, finalScale);
        guiGraphics.drawString(this.font, text, 0, 0, argbColor, true);
        guiGraphics.pose().popMatrix();
    }
}
