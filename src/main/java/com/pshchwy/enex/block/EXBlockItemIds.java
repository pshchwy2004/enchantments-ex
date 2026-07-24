package com.pshchwy.enex.block;

import com.pshchwy.enex.EnchantmentsEX;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public class EXBlockItemIds {
    private static BlockItemId create(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(EnchantmentsEX.MOD_ID, name);
        return BlockItemId.create(id, id);
    }
}
