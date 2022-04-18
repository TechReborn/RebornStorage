package net.gigabit101.rebornstorage.core.multiblock;

import net.gigabit101.rebornstorage.RebornStorage;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashMap;
import java.util.Set;

/**
 * This is a very static singleton registry class which directs incoming events
 * to sub-objects, which actually manage each individual world's multiblocks.
 *
 * @author Erogenous Beef
 */
public class MultiblockRegistry
{
    // World > WorldRegistry map
    private static HashMap<Level, MultiblockWorldRegistry> registries = new HashMap<Level, MultiblockWorldRegistry>();

    /**
     * Called before Tile Entities are ticked in the world. Do bookkeeping here.
     *
     * @param world The world being ticked
     */
    public static void tickStart(Level world)
    {
        if (registries.containsKey(world))
        {
            MultiblockWorldRegistry registry = registries.get(world);
            registry.processMultiblockChanges();
            registry.tickStart();
        }
    }

    /**
     * Called when the world has finished loading a chunk.
     *
     * @param world The world which has finished loading a chunk
     * @param chunk Loaded chunk
     */
    public static void onChunkLoaded(Level world, LevelChunk chunk)
    {
        if (registries.containsKey(world))
        {
            registries.get(world).onChunkLoaded(chunk);
        }
    }

    /**
     * Register a new part in the system. The part has been created either
     * through user action or via a chunk loading.
     *
     * @param world The world into which this part is loading.
     * @param part  The part being loaded.
     */
    public static void onPartAdded(Level world, IMultiblockPart part)
    {
        MultiblockWorldRegistry registry = getOrCreateRegistry(world);
        registry.onPartAdded(part);
    }

    /**
     * Call to remove a part from world lists.
     *
     * @param world The world from which a multiblock part is being removed.
     * @param part  The part being removed.
     */
    public static void onPartRemovedFromWorld(Level world, IMultiblockPart part)
    {
        if (registries.containsKey(world))
        {
            registries.get(world).onPartRemovedFromWorld(part);
        }
    }

    /**
     * Called whenever a world is unloaded. Unload the relevant registry, if we
     * have one.
     *
     * @param world The world being unloaded.
     */
    public static void onWorldUnloaded(Level world)
    {
        if (registries.containsKey(world))
        {
            registries.get(world).onWorldUnloaded();
            registries.remove(world);
        }
    }

    /**
     * Call to mark a controller as dirty. Dirty means that parts have been
     * added or removed this tick.
     *
     * @param world      The world containing the multiblock
     * @param controller The dirty controller
     */
    public static void addDirtyController(Level world, MultiblockControllerBase controller)
    {
        if (registries.containsKey(world))
        {
            registries.get(world).addDirtyController(controller);
        } else
        {
            RebornStorage.logger.warning("Adding a dirty controller to a world that has no registered controllers! This is most likey not an issue with reborn core, please check the full log file for more infomation!");
        }
    }

    /**
     * Call to mark a controller as dead. It should only be marked as dead when
     * it has no connected parts. It will be removed after the next world tick.
     *
     * @param world      The world formerly containing the multiblock
     * @param controller The dead controller
     */
    public static void addDeadController(Level world, MultiblockControllerBase controller)
    {
        if (registries.containsKey(world))
        {
            registries.get(world).addDeadController(controller);
        } else
        {
            RebornStorage.logger.warning(String.format("Controller %d in world %s marked as dead, but that world is not tracked! Controller is being ignored.", controller.hashCode(), world));
        }
    }

    /**
     * @param world The world whose controllers you wish to retrieve.
     * @return An unmodifiable set of controllers active in the given world, or
     * null if there are none.
     */
    public static Set<MultiblockControllerBase> getControllersFromWorld(Level world)
    {
        if (registries.containsKey(world))
        {
            return registries.get(world).getControllers();
        }
        return null;
    }

    // / *** PRIVATE HELPERS *** ///

    private static MultiblockWorldRegistry getOrCreateRegistry(Level world)
    {
        if (registries.containsKey(world))
        {
            return registries.get(world);
        } else
        {
            MultiblockWorldRegistry newRegistry = new MultiblockWorldRegistry(world);
            registries.put(world, newRegistry);
            return newRegistry;
        }
    }

}
