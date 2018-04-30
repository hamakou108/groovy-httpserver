package com.hamakou

import java.nio.charset.Charset

import com.hamakou.*

enum Contents {
    TEXT_PLAIN("text/plain", /txt/),
    TEXT_HTML("text/html", /html|htm/),
    TEXT_CSS("text/css", /css/),
    TEXT_XML("text/xml", /xml/),
    APPLICATION_JAVASCRIPT("application/javascript", /js/),
    APPLICATION_JSON("application/json", /json/),
    //IMAGE_JPEG("image/jpeg", "jpg|jpeg"),
    IMAGE_JPEG("image/jpeg", /jpg|jpeg/),
    IMAGE_PNG("image/png", /png/),
    IMAGE_GIF("image/gif", /gif/),

    private final String type
    private final String suffix
    private final String length

    Contents(String type, String suffix) {
        this.type = type
        this.suffix = suffix
    }

    static Map generate(String method, String uri) {
        def contentMap = [:]

        if (uri == "/") {
            contentMap["type"] = Contents.TEXT_PLAIN.type
            contentMap["body"] = "Hello, World!".getBytes(Charset.forName("UTF-8"))
        } else {
            Contents.values().each {
                if (uri ==~ /(.+)(\.${it.suffix})(\?.*)*$/) {
                    contentMap["type"] = it.type

                    def file1 = new File("src/dist/resources" + uri)
                    def file2 = new File("resources" + uri)
                    if (file1.exists()) {
                        contentMap["body"] = file1.getBytes()
                    } else if (file2.exists()) {
                        contentMap["body"] = file2.getBytes()
                    }
                }
            }
            // all contents did not match enum
            if (contentMap["body"] == null) {
                contentMap["type"] = Contents.TEXT_PLAIN.type
            }
        }

        if (contentMap["body"] != null) {
            contentMap["length"] = contentMap["body"].length
        }

        // if method is HEAD, remove contents of body
        if ((method as Methods) == Methods.HEAD) {
            contentMap["body"] = "".getBytes(Charset.forName("UTF-8"))
        }

        return contentMap
    }

    static Map generateEmpty() {
        def contentMap = [:]

        contentMap["type"] = Contents.TEXT_PLAIN.type
        contentMap["body"] = "".getBytes(Charset.forName("UTF-8"))
        contentMap["length"] = contentMap["body"].length

        return contentMap
    }
}
