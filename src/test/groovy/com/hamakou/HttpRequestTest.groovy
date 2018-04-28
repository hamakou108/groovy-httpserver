import spock.lang.*

import com.hamakou.*

class HttpRequestTest extends Specification {
    @Shared msgGet = """GET / HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
    @Shared msgHead = """HEAD / HTTP/1.0
Host:        localhost: 12345
User-Agent:  curl/7.54.0
Accept:      */*"""
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
        request.getMsg() == result

        where:
        msg | result
        msgGet | msgGet
        msgHead | msgHead
    }

    def "リクエストラインを取り出せる"() {
        setup:
        Request request = new HttpRequest(msg)

        expect:
        request.getLine() == result

        where:
        msg | result
        msgGet | "GET / HTTP/1.0"
        msgHead | "HEAD / HTTP/1.0"
    }

    def "リクエストのヘッダーフィールドを取り出せる"() {
        setup:
        Request request = new HttpRequest(msg)

        expect:
        request.getHeaderFieldMap("Host") == result

        where:
        msg | result
        msgGet | "localhost:12345"
        msgHead | "localhost: 12345"
    }

    def "リクエストボディを取り出せる"() {
        setup:
        Request request = new HttpRequest(msg)

        expect:
        request.getBody() == result

        where:
        msg | result
        msgGet | null
        msgHead | null
        //msgPost | "Oh!!"
    }
}
