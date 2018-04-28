package com.hamakou

class HttpRequest implements Request {
  String msg
  String line
  def headerFieldMap = [:]
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

  def getBody() {
    return this.body
  }

  def setBody(String body) {
    this.body = body
  }

  def getLine() {
    return this.line
  }

  def setLine(String line) {
    this.line = line
  }

  def getHeaderFieldMap(String key) {
    return this.headerFieldMap.get(key)
  }

  def putHeaderFieldMap(String key, String value) {
    this.headerFieldMap.put(key, value)
  }

  def parseMsg() {
    // separate to request-line, header, body
    def msgSplitedByLineBreak = this.msg.split("\n", 2).toList()
    def msgSplitedByDoubleLineBreak = msgSplitedByLineBreak[1].split("\n\n", 2).toList()

    // set request line
    this.setLine(msgSplitedByLineBreak[0])

    // set requeet header field as map
    msgSplitedByDoubleLineBreak[0].tokenize("\n").each {
      def tmp = it.split(": *", 2).toList()
      this.putHeaderFieldMap(tmp[0], tmp[1])
    }

    // set request body
    this.setBody(msgSplitedByDoubleLineBreak[1])
  }
}
