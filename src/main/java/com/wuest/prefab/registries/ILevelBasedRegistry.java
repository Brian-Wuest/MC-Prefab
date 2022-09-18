package com.wuest.prefab.registries;

import net.minecraft.world.level.Level;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This abstract class is used to hold onto a Level (World) based registry.
 * The type parameter is used to hold onto a vector (thread-safe) of objects.
 * This registry is not expected to be persisted.
 * It needs to be re-created whenever a server is started.
 * @param <T> The type of object to save in the registry.
 */
public abstract class ILevelBasedRegistry<T> {
    protected ConcurrentHashMap<Level, Vector<T>> internalRegistry;

    /**
     * Creates a new instance of this abstract class.
     */
    protected ILevelBasedRegistry() {
        this.internalRegistry = new ConcurrentHashMap<>();
    }

    /**
     * Registers the level (if it doesn't exist) as well as the element to the internal collection.
     * @param level The level to register if it doesn't exist or to find the level-appropriate registrations.
     * @param element The element to be registered.
     */
    public void register(Level level, T element) {
        Vector<T> elements;

        if(!this.internalRegistry.containsKey(level)) {
            elements = new Vector<>();
            this.internalRegistry.put(level, elements);
        } else {
            elements = this.internalRegistry.get(level);
        }

        // Make sure to check for null in-case the key was removed between the contains check and the get.
        if (elements != null) {
            boolean foundExistingElement = false;

            for (T existingElement : elements) {
                if (existingElement.hashCode() == element.hashCode()) {
                    foundExistingElement = true;
                    break;
                }
            }

            // Make sure this element does not already exist.
            if (!foundExistingElement) {
                elements.add(element);
                this.onElementRegistered(level, element);
            }
        }
    }

    /**
     * Removes an element from a level registration.
     * @param level The level which to remove the element from.
     *              If there are no more elements left, this level is also removed.
     * @param originalElement The original element to remove from the collection if possible.
     */
    public void remove(Level level, T originalElement) {
        if (this.internalRegistry.containsKey(level)) {
            Vector<T> elements = this.internalRegistry.get(level);

            // Make sure to check for null in-case the key was removed between the contains check and the get.
            if (elements != null) {
                // Try to find this element in the collection.
                for (int i = 0; i < elements.size(); i++) {
                    T element = elements.get(i);

                    if (element.hashCode() == originalElement.hashCode()) {
                        // remove the element from the collection to ensure that another player/thread doesn't try to remove the same item.
                        elements.remove(element);

                        // Let the implementor know that this element was removed to do any custom processing.
                        this.onElementRemoved(level, element);
                        break;
                    }
                }

                if (elements.size() == 0) {
                    // No more elements, remove the level from the array.
                    this.internalRegistry.remove(level);
                }
            }
        }
    }

    /**
     * Called when the element is removed from the registry.
     * This allows implementors an opportunity to do something custom in this scenario.
     * @param level The level which the element was removed.
     * @param element The element removed from the registry.
     */
    protected abstract void onElementRemoved(Level level, T element);

    /**
     * Called when the element is added to the registry.
     * This allows implementors an opportunity to do something custom in this scenario.
     * @param level the level which the element was added.
     * @param element the element added to the registry.
     */
    protected abstract void onElementRegistered(Level level, T element);
}
