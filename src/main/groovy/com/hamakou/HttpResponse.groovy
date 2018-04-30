package com.hamakou

import java.nio.charset.Charset
import org.apache.commons.lang3.EnumUtils

import com.hamakou.*

class HttpResponse implements Response {
    String version
    Integer statusCode
    String reasonPhrase
    String server
    Map contentMap = [:]

    def HttpResponse(Request request) {
        // setup server information
        this.setupServerInfo()

        try {
            // execute operation depending on method and uri
            this.execMethod(request.method, request.uri)
        } catch (Exception e) {
            setupStatusInfo(500)
            this.contentMap = Contents.generateEmpty()
        }
    }

    def setupServerInfo() {
        // set HTTP version for status line
        this.version = "HTTP/1.0"

        // set server name for header field
        this.server = "deoxys"
        //InetAddress.getLocalHost().getHostName()
    }

    def setupStatusInfo(Integer statusCode) {
        def statusTmp = Statuses.getAsList(statusCode)
        this.statusCode = statusTmp[0]
        this.reasonPhrase = statusTmp[1]
    }

    def execMethod(String method, String uri) {
        // return status 405 if the method is undefined
        if (!EnumUtils.isValidEnum(Methods, method)) {
            setupStatusInfo(405)
            this.contentMap = Contents.generateEmpty()
            return
        }

        // check if requested resource is defined
        // return status 403 if it is undefined
        if (!Resources.isDefined(method, uri)) {
            setupStatusInfo(403)
            this.contentMap = Contents.generateEmpty()
            return
        }

        // search contents and set some information about contents
        this.contentMap = Contents.generate(method, uri)
        if (this.contentMap["body"] == null) {
            setupStatusInfo(404)
            this.contentMap = Contents.generateEmpty()
            return
        }

        // return status 200 if all procedures succeeded
        setupStatusInfo(200)
    }

    def generateStatusLine() {
        String statusLine = this.version + " " + this.statusCode + " " + this.reasonPhrase + "\n"
        return statusLine.getBytes(Charset.forName("UTF-8"))
    }

    def generateHeader() {
        String header = ""
        header += "Content-Type: " + this.contentMap["type"] + "\n"
        header += "Server: " + this.server + "\n"
        header += "Content-Length: " + this.contentMap["length"] + "\n"
        return header.getBytes(Charset.forName("UTF-8"))
    }

    def generateBody() {
        return this.contentMap["body"] ?: "".getBytes(Charset.forName("UTF-8"))
    }

    def generateMsg() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.setBytes(this.generateStatusLine())
        outputStream.setBytes(this.generateHeader())
        outputStream.setBytes("\n".getBytes(Charset.forName("UTF-8")))
        outputStream.setBytes(this.generateBody())

        return outputStream.toByteArray();
    }
}
