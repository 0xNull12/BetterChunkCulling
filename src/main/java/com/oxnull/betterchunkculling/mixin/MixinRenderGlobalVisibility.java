/*
 *   Copyright (c) 2026 0xNull
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.oxnull.betterchunkculling.mixin;

import com.oxnull.betterchunkculling.BCCConfig;
import com.oxnull.betterchunkculling.BetterChunkCulling;
import com.oxnull.betterchunkculling.CullingMath;
import com.oxnull.betterchunkculling.NothiriumCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin para aplicar culling
 * Intercepta isBoundingBoxInFrustum en cualquier metodo de RenderGlobal
 */
@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobalVisibility {
    @Unique
    private static int bcc$visualCulledCount = 0;
    
    @Redirect(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/culling/ICamera;isBoundingBoxInFrustum(Lnet/minecraft/util/math/AxisAlignedBB;)Z"
        )
    )
    private boolean betterchunkculling$filterFrustumCheck(
        ICamera camera,
        AxisAlignedBB aabb
    ) {
        if (net.minecraftforge.fml.common.Loader.isModLoaded("nothirium")) {
            return camera.isBoundingBoxInFrustum(aabb);
        }
        if (!BCCConfig.enableVisualCulling || !NothiriumCompat.shouldEnableVisualCulling()) {
            return camera.isBoundingBoxInFrustum(aabb);
        }
        
        boolean vanillaResult = camera.isBoundingBoxInFrustum(aabb);
        if (!vanillaResult) {
            return false;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null) return true;
        Entity viewEntity = mc.getRenderViewEntity();
        if (viewEntity == null) {
            viewEntity = mc.player;
        }
        if (viewEntity == null) return true;
        int chunkX = (int) Math.floor(aabb.minX / 16.0D) * 16;
        int chunkY = (int) Math.floor(aabb.minY / 16.0D) * 16;
        int chunkZ = (int) Math.floor(aabb.minZ / 16.0D) * 16;
        boolean inside = CullingMath.shouldRenderCenter(
            chunkX, chunkY, chunkZ,
            viewEntity.posX, viewEntity.posY + viewEntity.getEyeHeight(), viewEntity.posZ,
            mc.gameSettings.renderDistanceChunks,
            BCCConfig.verticalStretch
        );

        if (!inside) {
            bcc$visualCulledCount++;
            return false;
        }
        return true;
    }
}
