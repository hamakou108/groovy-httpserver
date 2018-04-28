package com.hamakou

import java.nio.charset.Charset

import com.hamakou.*

class HttpResponse implements Response {

  def HttpResponse(Request request) {
    ;
  }

  def generate() {
    return """HTTP/1.0 200 OK
Content-Type: text/html
Server: my-groooooovy
Content-Length: 14

Hello, World!"""
//    return """HTTP/1.0 200 OK
//Content-Type: text/html
//Server: my-groooooovy
//Content-Length: """ + request.getBytes(Charset.forName("UTF-8")).length + "\n\n" + request
  }
}
