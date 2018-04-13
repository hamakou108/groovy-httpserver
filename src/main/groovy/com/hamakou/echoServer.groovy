class EchoServer {
    // server address
    String address

    // listen port
    Integer port

    def EchoServer(String address, Integer port) {
        this.address = address
        this.port = port
    }

    def run() {
        try {
            ServerSocket sSocket = new ServerSocket()
            sSocket.bind(new InetSocketAddress(address, port))
            System.out.println("Started server on port " + port);

            while (true) {
                // a "blocking" call which waits until a connection is requested
                Socket socket = sSocket.accept()

                // open up IO streams
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
                PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(), true)

                // waits for data and reads it in until connection dies
                // readLine() blocks until the server receives a new line from client
                String s;
                while ((s = reader.readLine()) != null) {
                    writer.println(s);
                }

                // close IO streams, then socket
                System.out.println("Closing connection with client");
                writer.close();
                reader.close();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            try {
                socket.close()
                sSocket.close()
                writer.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }
}

def es = new EchoServer("127.0.0.1", 18080)
es.run()
