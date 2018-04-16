package com.hamakou

class HttpRequest implements Request {
  String request

  def HttpRequest(String request) {
    this.request = request
  }
}
