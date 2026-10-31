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
package com.oxnull.betterchunkculling.mixin.nothirium;

import com.oxnull.betterchunkculling.BCCConfig;
import com.oxnull.betterchunkculling.BetterChunkCulling;
import com.oxnull.betterchunkculling.CullingMath;
import meldexun.nothirium.util.SectionPos;
import meldexun.renderlib.util.Frustum;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Compatibilidad con Nothirium
 *
 * Intercepta AbstractRenderChunk.isFrustumCulled(Frustum) para añadir
 * culling elipsoidal a Nothirium
 */
@Mixin(targets = "meldexun.nothirium.renderer.chunk.AbstractRenderChunk", remap = false)
public abstract class MixinNothiriumAbstractRenderChunk {
    @Unique
    private static int bcc$nothiriumCulled = 0;

    @Unique
    private static boolean bcc$firstCall = true;

    @Shadow(remap = false)
    public abstract SectionPos getPos();

    @Inject(
            method = "isFrustumCulled",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void bcc$ellipsoidCull(
            Frustum frustum,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if(bcc$firstCall) {
            bcc$firstCall = false;
        }

        if(!BCCConfig.enableVisualCulling || !BCCConfig.enableNothiriumCompat) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if(mc.world == null) {
            return;
        }

        Entity view = mc.getRenderViewEntity();
        if(view == null) {
            view = mc.player;
        }

        if(view == null) {
            return;
        }

        Vec3d camera = view.getPositionEyes(mc.getRenderPartialTicks());

        // Obtener coordenadas desde SectionPos
        // SectionPos almacena coordenadas de chunks (chunkX, chunkY, chunkZ)
        // getBlockX() multiplica por 16 para dar coordenadas de bloque
        SectionPos pos = getPos();
        int minX = pos.getBlockX();
        int minY = pos.getBlockY();
        int minZ = pos.getBlockZ();

        boolean inside = CullingMath.shouldRenderAabb(
                minX, minY, minZ,
                camera.x, camera.y, camera.z,
                mc.gameSettings.renderDistanceChunks,
                BCCConfig.verticalStretch
        );

        if (!inside) {
            bcc$nothiriumCulled++;
            if(BCCConfig.debug && bcc$nothiriumCulled % 1000 == 1) {
                BetterChunkCulling.logger.info(
                        "[BCC-Nothirium] Discarded chunks: {} (verticalStretch={})",
                        bcc$nothiriumCulled,
                        BCCConfig.verticalStretch
                );
            }
            cir.setReturnValue(true);
        }
    }
}
