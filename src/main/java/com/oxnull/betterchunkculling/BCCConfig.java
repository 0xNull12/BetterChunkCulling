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
package com.oxnull.betterchunkculling;

import meldexun.nothirium.mc.Nothirium;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import scala.reflect.internal.Trees.If;
import scala.reflect.internal.Trees.This;

/**
* Configuracion del mod
*/
@Config(
        modid = BetterChunkCulling.MODID,
        name = "betterchunkculling"
)
public final class BCCConfig {

    private BCCConfig() {
    }

    // factor de escala vertical del elipsoide
    @Config.Comment({
            "Vertical scale factor of the rendering ellipsoid",
            "0.5 = the effective vertical radius is half the horizontal radius",
            "1.0 = perfect sphere",
            "Larger values increase the vertical radius",
            "Very small values greatly reduce the vertical rendering"
    })
    @Config.Name("verticalStretch")
    @Config.RangeDouble(min = 0.05D, max = 16.0D)
    public static volatile double verticalStretch = 0.5D;

    // Desactiva la niebla del juego completamente
    @Config.Comment({
            "Turn off the game's fog",
            "Allows you to see the edges of ellipsoidal culling",
            "It also slightly improves performance",
            "It may make the game look less atmospheric"
    })
    @Config.Name("noFog")
    public static volatile boolean noFog = true;

    // Activa o desactiva los mensajes de debug en la consola
    @Config.Comment({
            "Enable debug messages in the console",
            "Displays information about discarded chunks",
            "Useful for verifying that the mod works",
            "Disable to reduce console spam"
    })
    @Config.Name("debug")
    public static volatile boolean debug = false;

    @Config.Comment({
        "Enable Nothirium compatibility",
        "This only takes effect if Nothirium is installed",
        "If disabled, Better Chunk Culling will not apply culling",
        "when Nothirium is present, because Nothirium replaces",
        "the vanilla rendering pipeline"
    })
    @Config.Name("enableNothiriumCompat")
    public static volatile boolean enableNothiriumCompat = true;
    
    @Config.Comment({
        "Enable visual chunk culling",
        "Discard chunks outside the ellipsoid during rendering"
    })
    @Config.Name("enableVisualCulling")
    public static volatile boolean enableVisualCulling = true;

    // Actualiza la configuracion si se cambia
    @Mod.EventBusSubscriber(
            modid = BetterChunkCulling.MODID,
            value = Side.CLIENT
    )
    public static class ConfigSyncHandler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (BetterChunkCulling.MODID.equals(event.getModID())) {
                ConfigManager.sync(BetterChunkCulling.MODID, Config.Type.INSTANCE);
                
                java.util.concurrent.atomic.AtomicBoolean fence = 
                    new java.util.concurrent.atomic.AtomicBoolean(false);
                fence.get();

                if (BetterChunkCulling.logger != null) {
                    BetterChunkCulling.logger.info(
                            "Updated settings: verticalStretch={}, noFog={}, debug={}",
                            verticalStretch, noFog, debug
                    );
                }
            }
        }
    }
}
