package com.hamakou

import com.hamakou.*

enum Resources {
  GET_ROOT("GET", "/"),
  HEAD_ROOT("HEAD", "/"),

  private final String method
  private final String resource

  Resources(String method, String resource) {
    this.method = method
    this.resource = resource
  }

  static boolean isDefined(String method, String uri) {
    def flg = false
    Resources.values().each {
      if (it.method == method && uri ==~ /^${it.resource}.*$/) {
        flg = true
      }
    }
    return flg
  }
}
