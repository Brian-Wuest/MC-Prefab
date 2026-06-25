package com.prefab;

public class TooltipHelper {
    private  static Boolean isClient = null;

    public static boolean isClientEnvironment() {
        // Note: The null check ensures we only if we are a client environment (and have the catch situation) once.
        if (isClient == null) {
            try {
                // Check if a core client class exists in the current JVM.
                Class.forName("net.minecraft.client.gui.screens.Screen");
                isClient = true;
            } catch (ClassNotFoundException e) {
                isClient = false;
            }
        }

        return isClient;
    }

    public static boolean isShiftPressed() {
        if (isClientEnvironment()) {
            return ClientAccess.checkShift();
        }

        return false;
    }

    private  static class ClientAccess {
        private static boolean checkShift() {
            // Note: fully qualify this to avoid top-level import/loading.
            return net.minecraft.client.gui.screens.Screen.hasShiftDown();
        }
    }
}
