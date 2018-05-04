package com.hamakou

import com.hamakou.*

/**
 * Status has types of status codes and reasone phrases and provies methods to deal with their
 * informations.
 */
enum Status {
  // Successful 2xx
  OK(200, "OK"),

  // Redirection 3xx

  // Client Error 4xx
  FORBIDDEN(403, "Forbidden"),
  NOTFOUND(404, "Not Found"),
  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

  // Server Error 5xx
  INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

  /**
   * The response status code.
   */
  private final Integer code

  /**
   * The reason phrase corresponding to the status code.
   */
  private final String reason

  /**
   * Constructs an own object Setting a status code and a reason phrase.
   *
   * @param code the response status code
   * @param reason the reason phrase corresponding to the status code
   */
  Status(Integer code, String reason) {
    this.code = code
    this.reason = reason
  }

  /**
   * Creates a list of a status code and a reason phrase which muches a code of argument.
   *
   * @param code the code corresponding to status to take out
   */
  static List getAsList(Integer code) {
    def tmpList = []
    Status.values().each {
      if (it.code == code) {
        tmpList.addAll(0, [it.code, it.reason])
      }
    }
    return tmpList
  }
}
