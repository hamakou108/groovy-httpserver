import spock.lang.*

import com.hamakou.*

class HttpRequestTest extends Specification {
  @Shared msgGetOrigin = """GET / HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
  @Shared msgGetOriginLongSP = """GET / HTTP/1.0
Host:       localhost: 12345
User-Agent: curl/7.54.0
Accept:     */*"""
  @Shared msgGetHtml = """GET /hamada.html HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
  @Shared msgHeadOrigin = """HEAD / HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
  @Shared msgPost = """POST / HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*
Content-Length: 8
Content-Type: application/x-www-form-urlencoded

Oh!!"""

  def "正しくリクエストを受け取っている"() {
    setup:
    Request request = new HttpRequest(msg)

    expect:
    request.msg == result

    where:
    msg | result
    msgGetOrigin | msgGetOrigin
    msgHeadOrigin | msgHeadOrigin
  }

  def "リクエストラインを取り出せる"() {
    setup:
    Request request = new HttpRequest(msg)

    expect:
    request.method + " " + request.uri + " " + request.version == result

    where:
    msg | result
    msgGetOrigin | "GET / HTTP/1.0"
    msgGetHtml | "GET /hamada.html HTTP/1.0"
    msgHeadOrigin | "HEAD / HTTP/1.0"
  }

  def "リクエストヘッダーフィールドを取り出せる"() {
    setup:
    Request request = new HttpRequest(msg)

    expect:
    request.headerMap.get("Host") == result

    where:
    msg | result
    msgGetOrigin | "localhost:12345"
    msgGetOriginLongSP | "localhost: 12345"
    msgHeadOrigin | "localhost:12345"
  }

  def "リクエストボディを取り出せる"() {
    setup:
    Request request = new HttpRequest(msg)

    expect:
    request.body == result

    where:
    msg | result
    msgGetOrigin | null
    msgHeadOrigin | null
    //msgPost | "Oh!!"
  }
}
