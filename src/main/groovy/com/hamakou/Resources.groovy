package com.hamakou

import com.hamakou.*

/**
 * Resource has types of combinations of a method and a resource which are required as URI by a
 * client.
 */
enum Resource {
  GET_ROOT("GET", "/"),
  HEAD_ROOT("HEAD", "/"),

  /**
   * The request method.
   */
  private final String method

  /**
   * The directory of resources.
   */
  private final String resource

  /**
   * Constructs an own object.
   *
   * @param method the request method
   * @param resource the directory of resources
   */
  Resource(String method, String resource) {
    this.method = method
    this.resource = resource
  }

  /**
   * Check if the combinations of a method and a resource is allowed to be requested.
   *
   * @param method the request method
   * @param uri the path of a requested resouce
   * @return returns {@code true} if the combination of a method and a resource is allowed to be
   * accessed
   */
  static boolean isDefined(String method, String uri) {
    def flg = false
    Resource.values().each {
      if (it.method == method && uri ==~ /^${it.resource}.*$/) {
        flg = true
      }
    }
    return flg
  }
}
