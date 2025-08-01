package com.prefab.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;

public class StrictModeOptions {

    @Comment("Determines whether or not Strict Building Mode is enabled.")
    public  boolean enabled = false;

    @Comment("The list of blocks which can be overwritten during building even when Strict Building Mode is Enabled.")
    public ArrayList<String> overwritableBlocks = new ArrayList<>();

    @Comment("The list of block tags which can be overwritten during building even when Strict Building Mode is Enabled.")
    public ArrayList<String> overwritableTags = new ArrayList<>();

    public StrictModeOptions() {

    }
}
