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

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = BetterChunkCulling.MODID,
        name = BetterChunkCulling.NAME,
        version = BetterChunkCulling.VERSION,
        acceptedMinecraftVersions = "[1.12.2]",
        clientSideOnly = true,
        dependencies = BetterChunkCulling.DEPENDENCIES,
        useMetadata = true
)
public final class BetterChunkCulling {
    public static final String MODID = "betterchunkculling";
    public static final String NAME = "Better Chunk Culling";
    public static final String VERSION = "1.2";
    public static final String DEPENDENCIES = "required-after:mixinbooter";
    public static Logger logger;

    @Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		if (BCCConfig.debug) {
			logger.info("[BCC] Better Chunk Culling initialized");
			logger.info("[BCC] Mod ID: {}", MODID);
			logger.info("[BCC] Version: {}", VERSION);
			logger.info("[BCC] current verticalStretch: {}", BCCConfig.verticalStretch);
		}
	}
}
