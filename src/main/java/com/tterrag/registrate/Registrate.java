package com.tterrag.registrate;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Registrate extends AbstractRegistrate<Registrate> {
    /**
     * Create a new {@link Registrate} and register event listeners for registration and data generation. Used in lieu of adding side-effects to constructor, so that alternate initialization
     * strategies can be done in subclasses.
     * 
     * @param modid
     *            The mod ID for which objects will be registered
     * @return The {@link Registrate} instance
     */
    public static Registrate create(String modid) {
        return new Registrate(modid);
    }

    protected Registrate(String modid) {
        super(modid);
    }
}
