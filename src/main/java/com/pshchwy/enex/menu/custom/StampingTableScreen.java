package com.pshchwy.enex.menu.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pshchwy.enex.EnchantmentsEX;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class StampingTableScreen extends AbstractContainerScreen<StampingTableMenu> {
    public static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, "textures/gui/stamping_table/stamping_table.png");

    public StampingTableScreen(StampingTableMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(
                GUI_TEXTURE,
                x, y,             // Dest X, Y on the player's screen
                0, 0,             // Source U, V (top-left of your texture file)
                imageWidth,       // Width to draw on screen (176)
                imageHeight,      // Height to draw on screen (166)
                176,              // texture width
                166               // texture height
        );
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}
