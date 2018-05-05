package com.hamakou

/**
 * HttpRequest provides methods to parse a http request message and saves its information as
 * instance variables.
 */
class HttpRequest implements Request {
  /**
   * The raw request message.
   */
  String msg

  /**
   * The request method like GET, HEAD.
   */
  String method

  /**
   * The request URI.
   */
  String uri

  /**
   * The HTTP version of this server.
   */
  String version

  /**
   * The request header as a map.
   */
  def headerMap = [:]

  /**
   * The request body.
   */
  String body

  /**
   * Saves and parses a request messages.
   *
   * @param msg the raw request message
   */
  def HttpRequest(String msg) {
    this.msg = msg
    this.parseMsg()
  }

  /**
   * Parses a request message and saves its information.
   */
  def parseMsg() {
    // separate to request-line, header, body
    def msgSplitedByLineBreak = this.msg.split("\n", 2).toList()
    def msgSplitedByDoubleLineBreak = msgSplitedByLineBreak[1].split("\n\n", 2).toList()

    // set request line
    def lineTmp = msgSplitedByLineBreak[0].tokenize(" *")
    this.method = lineTmp[0]
    this.uri = lineTmp[1]
    this.version = lineTmp[2]

    // set requeet header field as a map object
    msgSplitedByDoubleLineBreak[0].tokenize("\n").each {
      def tmp = it.split(": *", 2).toList()
      this.headerMap.put(tmp[0], tmp[1])
    }

    // set request body
    this.body = msgSplitedByDoubleLineBreak[1]
  }
}
