# Better Chunk Culling

Better Chunk Culling is a performance mod for Minecraft 1.12.2 designed to significantly increase FPS, especially on low-end computers and systems with integrated graphics, but at the cost of poorer rendering quality

## How it works (Technical Details)

The original Minecraft engine processes rendering fragments in rigid cubic regions using standard frustum calculations. Better Chunk Culling uses **MixinBooter** to integrate directly into the internal rendering process (`RenderGlobal` / chunk setup logic)

Replaces Vanilla's rendering checks with a **3D ellipsoidal culling algorithm**, which dynamically omits 16x16x16 block sections that are not visible and lie outside the player's field of view

To minimize the CPU load, use an optimized elliptic distance-squared formula that avoids computationally expensive square root calculations (`Math.sqrt`):

`Distance² = (dx)² + (dy * k)² + (dz)²`

*Note: Because invisible chunks are aggressively removed to maximize the frame rate, in rare cases, minor lighting glitches or delays in block updates may occur. I strongly recommend using the [Alfheim Lighting Engine](https://www.curseforge.com/minecraft/mc-mods/alfheim-lighting-engine).*

---

## Benchmark results

Tests conducted on older, ultra-low-end hardware (Intel Pentium E2220 CPU at 2.76 GHz with integrated Intel GMA 3100 / G31 graphics):

| Benchmark Condition | FPS | Rendered Chunk Sections |
| :--- | :--- | :--- |
| **Vanilla + MixinBooter** | 16 FPS | 75 / 2704 |
| **With Better Chunk Culling** | **27 FPS** (+68%) | **42 / 1936** (~45% less GPU load) |

![comparacion](screenshots/comparacion.png)
![prueba de rendimiento](screenshots/pruebaderendimientoenmimundo.png)

**Note**: This may not apply to all hardware configurations, but it worked for me

---

## Bug reports

If you encounter any problems or visual errors, please report them on the [GitHub Issue Tracker](https://github.com/0xNull12/BetterChunkCulling/issues)
