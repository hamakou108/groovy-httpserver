//@Grab(group='org.spockframework', module='spock-core', version='1.0-groovy-2.4')
import spock.lang.*

import com.hamakou.*

class HttpResponseTest extends Specification {
    def "正しいレスポンスが返ってくる"() {
        setup:
        Response response = new HttpResponse(request)

        expect:
        response.generate() == result

        where:
        request | result
        "abc"     | """HTTP/1.0 200 OK
Content-Type: text/html
Server: my-groooooovy
Content-Length: 3

abc"""
        "あいうえお" | """HTTP/1.0 200 OK
Content-Type: text/html
Server: my-groooooovy
Content-Length: 15

あいうえお"""
    }
}
