package com.hamakou

import java.nio.charset.Charset

import com.hamakou.*

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
                socket.getOutputStream().withCloseable { writer ->
                    // receives a http request from client as String
                    String line;
                    String msg = "";

                    while ((line = reader.readLine()) != null) {
                        if (line.isEmpty()) {
                            break;
                        }
                        msg += line + "\n";
                    }

                    // parse request and generate response
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
