package com.hamakou

import com.hamakou.*

/**
 * Method has types of request methods.
 */
enum Method {
  // the request methods which is allowed for a client to use.
  GET,
  HEAD,

  /**
   * Returns {@code true} if the method of argument is contained in types of request methods.
   *
   * @param method the request method required from a client
   */
  static boolean contains(String method) {
    return Method.values().contains(method)
  }
}
