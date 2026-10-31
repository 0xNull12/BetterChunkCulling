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

import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Compatibilidad con Nothirium
 */
public final class NothiriumCompat {    
    private static final Logger LOGGER = LogManager.getLogger("BetterChunkCulling");
    private static boolean nothiriumDetected = false;
    private NothiriumCompat() {
    }
    
    // Detecta si Nothirium esta instalado
    public static void detectNothirium() {
        nothiriumDetected = Loader.isModLoaded("nothirium");
    }
    
    // Retorna true si Nothirium esta instalado
    public static boolean isNothiriumPresent() {
        return nothiriumDetected;
    }
    
    /**
     * Revisa si el MixinRenderGlobalVisibility deberia estar activo
     * Con Nothirium, se desactiva el culling porque va a
     * interferir con su sistema de renderizado
     */
    public static boolean shouldEnableVisualCulling() {
        return !nothiriumDetected;
    }
    
    // Revisa si el (MixinChunkRenderDispatcher deberia estar activo
    public static boolean shouldEnableCompileCulling() {
        return true;
    }
}