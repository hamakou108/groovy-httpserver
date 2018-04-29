package com.hamakou

import java.nio.charset.Charset
import org.apache.commons.lang3.EnumUtils

import com.hamakou.*

class HttpResponse implements Response {
    String version
    Integer statusCode
    String reasonPhrase
    String server
    Map contentMap

    def HttpResponse(Request request) {
       // setup server information
       this.setupServerInfo()

       // execute operation depending on method and uri
       this.execMethod(request.method, request.uri)
    }

    def execMethod(String method, String uri) {
        // return status 405 if the method is undefined
        if (!EnumUtils.isValidEnum(Methods, method)) {
            def statusTmp = Statuses.getAsList(405)
            this.statusCode = statusTmp[0]
            this.reasonPhrase = statusTmp[1]
            return
        }

        // check if requested resource is defined
        // return status 403 if it is undefined
        if (!Resources.isDefined(method, uri)) {
            def statusTmp = Statuses.getAsList(403)
            this.statusCode = statusTmp[0]
            this.reasonPhrase = statusTmp[1]
            return
        }

        // search contents and set some information about contents
        this.contentMap = Contents.generate(method, uri)
        if (contentMap["body"] == null) {
            def statusTmp = Statuses.getAsList(404)
            this.statusCode = statusTmp[0]
            this.reasonPhrase = statusTmp[1]
            return
        }

        // return status 200 if all procedures succeeded
        def statusTmp = Statuses.getAsList(200)
        this.statusCode = statusTmp[0]
        this.reasonPhrase = statusTmp[1]
    }

    def setupServerInfo() {
        // set HTTP version for status line
        this.version = "HTTP/1.0"

        // set server name for header field
        this.server = "deoxys"
        //InetAddress.getLocalHost().getHostName()
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
