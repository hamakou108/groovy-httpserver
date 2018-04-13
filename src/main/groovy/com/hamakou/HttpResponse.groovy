package com.hamakou

import java.nio.charset.Charset

class HttpResponse implements Response {
  String request

  def HttpResponse(String request) {
    this.request = request
  }

  def generate() {
    return """HTTP/1.0 200 OK
Content-Type: text/html
Server: my-groooooovy
Content-Length: """ + request.getBytes(Charset.forName("UTF-8")).length + "\n\n" + request
  }
}
