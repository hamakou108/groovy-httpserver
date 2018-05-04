package com.hamakou

import java.nio.charset.Charset

import com.hamakou.*

/**
 * Content has types of {@code Content-Type} and a suffix.
 */
enum Content {
  TEXT_PLAIN("text/plain", /txt/),
  TEXT_HTML("text/html", /html|htm/),
  TEXT_CSS("text/css", /css/),
  TEXT_XML("text/xml", /xml/),
  APPLICATION_JAVASCRIPT("application/javascript", /js/),
  APPLICATION_JSON("application/json", /json/),
  IMAGE_JPEG("image/jpeg", /jpg|jpeg/),
  IMAGE_PNG("image/png", /png/),
  IMAGE_GIF("image/gif", /gif/),

  /**
   * The content type which is represented by the format like {@code text/html}.
   */
  private final String type

  /**
   * The suffix of the content.
   */
  private final String suffix

  /**
   * The byte lengths of the content.
   */
  private final String length

  /**
   * Constructs an own object.
   *
   * @param type the content type
   * @param suffix the suffix of the content
   */
  Content(String type, String suffix) {
    this.type = type
    this.suffix = suffix
  }

  /**
   * Generates a byte object of the content by the request method and path of resource.
   *
   * <p> If the requested resouce does not match with any enum types of content, an empty byte is
   * set as the value of the content body and {@code text/plain} is set as the value of content
   * type.
   *
   * @param method the request method
   * @param uri the request uri corresponding to resources
   * @return the map which has informations of the content
   */
  static Map generate(String method, String uri) {
    def contentMap = [:]

    if (uri == "/") {
      contentMap["type"] = Content.TEXT_PLAIN.type
      contentMap["body"] = "Hello, World!".getBytes(Charset.forName("UTF-8"))
    } else {
      Content.values().each {
        if (uri ==~ /(.+)(\.${it.suffix})(\?.*)*$/) {
          contentMap["type"] = it.type

          def file1 = new File("src/dist/resources" + uri)
          def file2 = new File("resources" + uri)
          if (file1.exists()) {
            contentMap["body"] = file1.getBytes()
          } else if (file2.exists()) {
            contentMap["body"] = file2.getBytes()
          }
        }
      }
      // all contents did not match enum
      if (contentMap["body"] == null) {
        contentMap["type"] = Content.TEXT_PLAIN.type
      }
    }

    if (contentMap["body"] != null) {
      contentMap["length"] = contentMap["body"].length
    }

    // if method is HEAD, remove contents of body
    if ((method as Method) == Method.HEAD) {
      contentMap["body"] = "".getBytes(Charset.forName("UTF-8"))
    }

    return contentMap
  }

  /**
   * Generates an empty byte object of the content no matter what the request message is.
   *
   * <p> {@code text/plain} is set as the value of content type.
   *
   * @return the map which has informations of the empty content
   */
  static Map generateEmpty() {
    def contentMap = [:]
    contentMap["type"] = Content.TEXT_PLAIN.type
    contentMap["body"] = "".getBytes(Charset.forName("UTF-8"))
    contentMap["length"] = contentMap["body"].length
    return contentMap
  }
}
