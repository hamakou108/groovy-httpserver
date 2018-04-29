package com.hamakou

// default imports
//import java.lang.*
//import java.util.*
//import java.io.*
//import java.net.*
//import groovy.lang.*
//import groovy.util.*
//import java.math.BigInteger
//import java.math.BigDecimal

import java.nio.channels.ServerSocketChannel

class HttpServer {
    // server address
    String address

    // listen port
    Integer port

    def HttpServer(String address, Integer port) {
        this.address = address
        this.port = port
    }

    def start() {
        ServerSocketChannel.open().withCloseable { ssc ->
            ssc.socket().bind(new InetSocketAddress(address, port))
            System.out.println("Started server on port " + port)

            while (true) {
                // a "blocking" call which waits until a connection is requested
                Socket socket = ssc.socket().accept()
                new Thread(new HttpServerTask(socket)).start();
            }
        }
    }

    static void main(String... args) {
        def hServer = new HttpServer("127.0.0.1", 12345)
        hServer.start()
    }
}
