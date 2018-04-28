//@Grab(group='org.spockframework', module='spock-core', version='1.0-groovy-2.4')
import spock.lang.*

import com.hamakou.*

class HttpResponseTest extends Specification {
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

    def "正しいレスポンスが返ってくる"() {
        setup:
        Request request = new HttpRequest(msg)
        Response response = new HttpResponse(request)

        expect:
        response.generate() == result

        where:
        msg | result
        msgGet | """HTTP/1.0 200 OK
Content-Type: text/html
Server: my-groooooovy
Content-Length: 14

Hello, World!"""
//        "あいうえお" | """HTTP/1.0 200 OK
//Content-Type: text/html
//Server: my-groooooovy
//Content-Length: 15
//
//あいうえお"""
    }
}
