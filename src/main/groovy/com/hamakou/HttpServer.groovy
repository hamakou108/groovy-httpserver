package com.hamakou

import java.nio.channels.ServerSocketChannel

/**
 * HttpServer is used to run http server.
 */
class HttpServer {
  /**
   * The address to connects from a client.
   */
  String address

  /**
   * The port to connects from a client.
   */
  Integer port

  /**
   * Constructs a server object with address and port to connect.
   *
   * @param address the address to connects from a client; must not be {@code null}
   * @param port the port to connects from a client; must not be {@code null}
   */
  def HttpServer(String address, Integer port) {
    this.address = address
    this.port = port
  }

  /**
   * Starts to await a request from a client.
   *
   * <p> This method binds a address and port to socket, and then await a request from a client.
   * When the socket accepts a request, it calls a non-blocking task to create response.
   */
  def start() {
    ServerSocketChannel.open().withCloseable { ssc ->
      ssc.socket().bind(new InetSocketAddress(address, port))
      System.out.println("Started server on port " + port)

      while (true) {
        // a "non-blocking" call which waits until a connection is requested
        Socket socket = ssc.socket().accept()
        new Thread(new HttpServerTask(socket)).start();
      }
    }
  }

  /**
   * Creates an own object with the specified address and port.
   *
   * @param args command line arguments; not to used
   */
  static void main(String... args) {
    def hServer = new HttpServer("127.0.0.1", 12345)
    hServer.start()
  }
}
