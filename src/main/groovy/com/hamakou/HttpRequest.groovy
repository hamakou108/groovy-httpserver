package com.hamakou

class HttpRequest implements Request {
  String msg
  String method
  String uri
  String version
  def headerMap = [:]
  String body

  def HttpRequest(String msg) {
    this.setMsg(msg)
    this.parseMsg()
  }

  def getMsg() {
    return this.msg
  }

  def setMsg(String msg) {
    this.msg = msg
  }

  def getMethod() {
    return this.method
  }

  def setMethod(String method) {
    this.method = method
  }

  def getUri() {
    return this.uri
  }

  def setUri(String uri) {
    this.uri = uri
  }

  def getVersion() {
    return this.version
  }

  def setVersion(String version) {
    this.version = version
  }

  def getHeaderMap(String key) {
    return this.headerMap.get(key)
  }

  def putHeaderMap(String key, String value) {
    this.headerMap.put(key, value)
  }

  def getBody() {
    return this.body
  }

  def setBody(String body) {
    this.body = body
  }

  def parseMsg() {
    // separate to request-line, header, body
    def msgSplitedByLineBreak = this.msg.split("\n", 2).toList()
    def msgSplitedByDoubleLineBreak = msgSplitedByLineBreak[1].split("\n\n", 2).toList()

    // set request line
    def lineTmp = msgSplitedByLineBreak[0].tokenize(" *")
    this.setMethod(lineTmp[0])
    this.setUri(lineTmp[1])
    this.setVersion(lineTmp[2])

    // set requeet header field as map
    msgSplitedByDoubleLineBreak[0].tokenize("\n").each {
      def tmp = it.split(": *", 2).toList()
      this.putHeaderMap(tmp[0], tmp[1])
    }

    // set request body
    this.setBody(msgSplitedByDoubleLineBreak[1])
  }
}
