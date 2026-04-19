package com.ecommerce.InventoryService.util;

public class UserContext {

    private static final ThreadLocal<String> user = new ThreadLocal<>();
    private static final ThreadLocal<String> role = new ThreadLocal<>();

    public static void set(String u, String r) {
        user.set(u);
        role.set(r);
    }

    public static String getUser() {
        return user.get();
    }

    public static String getRole() {
        return role.get();
    }

    public static void clear() {
        user.remove();
        role.remove();
    }
}