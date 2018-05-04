package com.hamakou

import java.nio.charset.Charset

import com.hamakou.*

/**
 * HttpServerTask provides a task to create response.
 */
class HttpServerTask implements Runnable {
  Socket socket;

  /**
   * Constructs a task with a socket.
   */
  def HttpServerTask(Socket socket) {
    this.socket = socket
  }

  /**
   * Outputs a response by a request.
   *
   * <p> This method reads a request message and outputs a response message.
   *
   * @throws IOException when the input or output stream has a trouble
   */
  @Override
  void run() {
    try {
      // open up IO streams
      new BufferedReader(new InputStreamReader(socket.getInputStream())).withCloseable { reader ->
        socket.getOutputStream().withCloseable { writer ->
          // receives a http request from client and convert to a String object
          String line;
          String msg = "";
          while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
              break;
            }
            msg += line + "\n";
          }

          // parses a request message and outputs a response as a byte array
          Request request = new HttpRequest(msg)
          Response response = new HttpResponse(request)
          writer.write(response.generateMsg())
          writer.flush()

          // close IO streams, then socket
          System.out.println("Closing connection with client");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
