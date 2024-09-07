package com.fullskele.eruditetweaks.proxy;

import com.kreezcraft.terracartreloaded.EntityTerraCart;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy {
    public void preInit() {
        if (Loader.isModLoaded("terracart")) {
            RenderingRegistry.registerEntityRenderingHandler(EntityTerraCart.class, manager -> new RenderMinecart<>(manager));
        }
    }
}