package com.hamakou

class HttpServerTask implements Runnable {
  Socket socket;

  def HttpServerTask(Socket socket) {
    this.socket = socket
  }

  @Override
  void run() {
    try {
      // open up IO streams
      new BufferedReader(new InputStreamReader(socket.getInputStream())).withCloseable { reader ->
        new PrintWriter(socket.getOutputStream(), true).withCloseable { writer ->
          // receives a http request from client
          String request;
          request = reader.readLine()

          // generate response
          Response response = new HttpResponse(request)
          writer.println(response.generate())

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
