package com.hamakou

import com.hamakou.*

enum Methods {
    GET,
    HEAD,

    static boolean contains(String method) {
        return Methods.values().contains(method)
    }
}
