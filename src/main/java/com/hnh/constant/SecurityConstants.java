package com.hnh.constant;

public interface SecurityConstants {
    String[] STAFF_SHARED_API_PATHS = {
            "/api/auth/info",
            "/api/statistics",
            "/api/statistics/**"
    };

    String[] PUBLIC_API_PATHS = {
            "/api/auth/login",
            "/api/auth/refresh-token",
            "/api/auth/registration/**",
            "/api/auth/forgot-password",
            "/api/auth/reset-password",
            "/api/addresses",
            "/api/addresses/**",
            "/api/provinces",
            "/api/provinces/**",
            "/api/districts",
            "/api/districts/**",
            "/api/wards",
            "/api/wards/**",
            "/client-api/categories",
            "/client-api/categories/**",
            "/client-api/products",
            "/client-api/products/**",
            "/client-api/brands",
            "/client-api/brands/**",
            "/client-api/filters",
            "/client-api/filters/**",
            "/client-api/payment-methods",
            "/client-api/payment-methods/**",
            "/ws/**"
    };

    String[] ADMIN_API_PATHS = {
            "/api/users",
            "/api/users/**",
            "/api/roles",
            "/api/roles/**"
    };

    String[] MANAGER_API_PATHS = {
            "/api/properties",
            "/api/properties/**",
            "/api/categories",
            "/api/categories/**",
            "/api/tags",
            "/api/tags/**",
            "/api/guarantees",
            "/api/guarantees/**",
            "/api/units",
            "/api/units/**",
            "/api/suppliers",
            "/api/suppliers/**",
            "/api/brands",
            "/api/brands/**",
            "/api/specifications",
            "/api/specifications/**",
            "/api/products",
            "/api/products/**",
            "/api/variants",
            "/api/variants/**",
            "/api/images",
            "/api/images/**",
            "/api/warehouses",
            "/api/warehouses/**",
            "/api/storage-locations",
            "/api/storage-locations/**",
            "/api/product-inventories",
            "/api/product-inventories/**",
            "/api/promotions",
            "/api/promotions/**",
            "/api/reward-strategies",
            "/api/reward-strategies/**",
            "/api/payment-methods",
            "/api/payment-methods/**"
    };

    String[] OPERATOR_API_PATHS = {
            "/api/order-resources",
            "/api/order-resources/**",
            "/api/order-cancellation-reasons",
            "/api/order-cancellation-reasons/**",
            "/api/orders",
            "/api/orders/**",
            "/api/waybills",
            "/api/waybills/**",
            "/api/reviews",
            "/api/reviews/**",
            "/api/rooms",
            "/api/rooms/**",
            "/api/products",
            "/api/products/**",
            "/api/variants",
            "/api/variants/**",
            "/api/images",
            "/api/images/**"
    };

    String[] CLIENT_API_PATHS = {
            "/client-api/users",
            "/client-api/users/**",
            "/client-api/wishes",
            "/client-api/wishes/**",
            "/client-api/preorders",
            "/client-api/preorders/**",
            "/client-api/notifications",
            "/client-api/notifications/**",
            "/client-api/reviews",
            "/client-api/reviews/**",
            "/client-api/carts",
            "/client-api/carts/**",
            "/client-api/orders",
            "/client-api/orders/**",
            "/client-api/chat",
            "/client-api/chat/**"
    };

    String[] IGNORING_API_PATHS = {
            "/client-api/notifications/events",
            "/client-api/reviews/products/**",
            "/client-api/orders/success",
            "/client-api/orders/cancel",
            "/client-api/newsletter/**",
            "/images/**"
    };

    interface Role {
        String ADMIN = "ADMIN";

        String MANAGER = "MANAGER";

        String OPERATOR = "OPERATOR";

        String CUSTOMER = "CUSTOMER";
    }
}

