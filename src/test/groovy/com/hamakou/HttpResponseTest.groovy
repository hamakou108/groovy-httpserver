import spock.lang.*

import java.nio.charset.Charset

import com.hamakou.*

class HttpResponseTest extends Specification {

    // request message
    @Shared msgGetOrigin = """GET / HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
    @Shared msgGetHtml = """GET /index.html HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
    @Shared msgGetHtml404 = """GET /indexxx.html HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
    @Shared msgGetCss = """GET /css/styles.css HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
    @Shared msgGetJs = """GET /js/scripts.js HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
    @Shared msgGetJpeg = """GET /images/deoxys.jpeg HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
    @Shared msgGetIco = """GET /favicon.ico HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
    @Shared msgHeadOrigin = """HEAD / HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*"""
    @Shared msgPostOrigin = """POST / HTTP/1.0
Host: localhost:12345
User-Agent: curl/7.54.0
Accept: */*
Content-Length: 8
Content-Type: application/x-www-form-urlencoded

Oh!!"""

    def "URL に応じて適切なコンテンツタイプが返却される"() {
        setup:
        Request request = new HttpRequest(msg)
        Response response = new HttpResponse(request)

        expect:
        response.contentMap["type"] == result

        where:
        msg | result
        // Step 1
        msgGetOrigin | "text/plain"
        // Step 2
        msgGetHtml | "text/html"
        // Step 3
        msgGetCss | "text/css"
        // Step 4
        msgGetJpeg | "image/jpeg"
        // Step 5
        msgHeadOrigin | "text/plain"
        // Step 6
        msgGetHtml404 | "text/plain"
    }

    def "URL に応じて適切なコンテンツレングスが返却される"() {
        setup:
        Request request = new HttpRequest(msg)
        Response response = new HttpResponse(request)

        expect:
        response.contentMap["length"] == result

        where:
        msg | result
        // Step 1
        msgGetOrigin | 13
        // Step 2
        msgGetHtml | 361
        // Step 3
        msgGetCss | 82
        // Step 4
        msgGetJpeg | 8228
        // Step 5
        msgHeadOrigin | 13
    }

    def "URL に応じて適切なステータスラインが返却される"() {
        setup:
        Request request = new HttpRequest(msg)
        Response response = new HttpResponse(request)

        expect:
        new String(response.generateStatusLine(), "UTF-8") == result

        where:
        msg | result
        // Step 1
        msgGetOrigin | "HTTP/1.0 200 OK\n"
        // Step 2
        msgGetHtml | "HTTP/1.0 200 OK\n"
        // Step 3
        msgGetCss | "HTTP/1.0 200 OK\n"
        // Step 4
        msgGetJpeg | "HTTP/1.0 200 OK\n"
        // Step 5
        msgHeadOrigin | "HTTP/1.0 200 OK\n"
        // Step 6
        msgGetHtml404 | "HTTP/1.0 404 Not Found\n"
        // Step 7
        msgPostOrigin | "HTTP/1.0 405 Method Not Allowed\n"
    }

    def "HEAD メソッドに対してボディがレスポンスされない"() {
        setup:
        Request request = new HttpRequest(msg)
        Response response = new HttpResponse(request)

        expect:
        response.generateBody() == result

        where:
        msg | result
        // Step 5
        msgHeadOrigin | "".getBytes(Charset.forName("UTF-8"))
    }
}
