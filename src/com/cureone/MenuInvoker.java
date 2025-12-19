package com.cureone;

import java.lang.reflect.Method;

/**
 * Utility to invoke common controller menu methods safely.
 */
public class MenuInvoker {

    // Try common menu method names on the controller object
    public static void invokeMenu(Object controller) {
        invokeAnyMenu(controller);
    }

    public static void invokeAnyMenu(Object controller) {
        if (controller == null) return;
        String[] methods = { "showMenu", "startMenu", "startStaffMenu", "start", "menu", "run" };
        for (String m : methods) {
            try {
                Method method = controller.getClass().getMethod(m);
                method.invoke(controller);
                return;
            } catch (NoSuchMethodException ignored) {
                // try next
            } catch (Exception e) {
                System.out.println("Failed to call menu method '" + m + "' on " + controller.getClass().getSimpleName() + ": " + e.getMessage());
                return;
            }
        }
        System.out.println("No menu method found on " + controller.getClass().getName());
    }

    // Special: for DoctorController, some projects may have showMenu with args — we try no-arg first.
    public static void invokeDoctorMenu(Object controller) {
        invokeAnyMenu(controller);
    }
}
