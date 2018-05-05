package com.hamakou

import java.nio.charset.Charset
import org.apache.commons.lang3.EnumUtils

import com.hamakou.*

/**
 * HttpResponse provides methods to generate response message from request information.
 */
class HttpResponse implements Response {

  /**
   * The HTTP version of this server.
   */
  String version

  /**
   * The status code as a result of analyzing request information.
   */
  Integer statusCode

  /**
   * The reason phrase corresponding to the status code.
   */
  String reasonPhrase

  /**
   * This server name.
   */
  String server

  /**
   * The information of response contents.
   */
  Map contentMap = [:]

  /**
   * Generate response information by request information.
   *
   * @param request request information parsed by request message
   */
  def HttpResponse(Request request) {
    // setup server information
    this.setupServerInfo()

    try {
      // generate information to response depending on method and uri
      this.execMethod(request.method, request.uri)
    } catch (Exception e) {
      // returns status 500 if some exception occurs
      setupStatusInfo(500)
      this.contentMap = Content.generateEmpty()
    }
  }

  /**
   * Generates server information which is independent of a request.
   */
  def setupServerInfo() {
    // set HTTP version for status line
    this.version = "HTTP/1.0"

    // set server name for header field
    this.server = "deoxys"
  }

  /**
   * Generates status information by status code.
   *
   * @param statusCode status code which is compliance with RFC 1945
   */
  def setupStatusInfo(Integer statusCode) {
    def statusTmp = Status.getAsList(statusCode)
    this.statusCode = statusTmp[0]
    this.reasonPhrase = statusTmp[1]
  }

  /**
   * Executes methods to generate information to response.
   *
   * <p> If response information includes unsuccessful elements, sets error status code and
   * appropriate other information and just return.
   *
   * @param method the request method like GET, HEAD
   * @param uri the requested uri
   */
  def execMethod(String method, String uri) {
    // returns status 405 if the method is undefined
    if (!EnumUtils.isValidEnum(Method, method)) {
      setupStatusInfo(405)
      this.contentMap = Content.generateEmpty()
      return
    }

    // returns status 403 if the requested resource is forbiddend to access
    if (!Resource.isDefined(method, uri)) {
      setupStatusInfo(403)
      this.contentMap = Content.generateEmpty()
      return
    }

    // searches contents and sets some information about contents
    this.contentMap = Content.generate(method, uri)

    // returns status 404 if the requested content does not exist
    if (this.contentMap["body"] == null) {
      setupStatusInfo(404)
      this.contentMap = Content.generateEmpty()
      return
    }

    // returns status 200 if all procedures succeeded
    setupStatusInfo(200)
  }

  /**
   * Generates status line as a byte array.
   */
  def generateStatusLine() {
    String statusLine = this.version + " " + this.statusCode + " " + this.reasonPhrase + "\n"
    return statusLine.getBytes(Charset.forName("UTF-8"))
  }

  /**
   * Generates response header as a byte array.
   */
  def generateHeader() {
    String header = ""
    header += "Content-Type: " + this.contentMap["type"] + "\n"
    header += "Server: " + this.server + "\n"
    header += "Content-Length: " + this.contentMap["length"] + "\n"
    return header.getBytes(Charset.forName("UTF-8"))
  }

  /**
   * Generates response body as a byte array.
   */
  def generateBody() {
    // returns an empty byte if a response body is null
    return this.contentMap["body"] ?: "".getBytes(Charset.forName("UTF-8"))
  }

  /**
   * Generates a whole response messagea as a byte array.
   */
  def generateMsg() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.setBytes(this.generateStatusLine())
    outputStream.setBytes(this.generateHeader())
    outputStream.setBytes("\n".getBytes(Charset.forName("UTF-8")))
    outputStream.setBytes(this.generateBody())

    return outputStream.toByteArray();
  }
}
