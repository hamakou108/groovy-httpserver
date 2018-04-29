package com.hamakou

import com.hamakou.*

enum Statuses {
    OK(200, "OK"),
    FORBIDDEN(403, "Forbidden"),
    NOTFOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    private final Integer code
    private final String reason

    Statuses(Integer code, String reason) {
        this.code = code
        this.reason = reason
    }

    static List getAsList(Integer code) {
        def tmpList = []
        Statuses.values().each {
            if (it.code == code) {
                tmpList.addAll(0, [it.code, it.reason])
            }
        }
        return tmpList
    }
}