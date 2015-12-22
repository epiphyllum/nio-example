package com.epiphyllum.nio.sum;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Date;
import java.util.regex.Pattern;

public class TimeServer {

    // We can't use the normal daytime port (unless we're running as root,
    // which is unlikely), so we use this one instead
    private static int            PORT    = 8013;

    // The port we'll actually use
    private static int            port    = PORT;

    // Charset and encoder for US-ASCII
    private static Charset        charset = Charset.forName("US-ASCII");
    private static CharsetEncoder encoder = charset.newEncoder();

    // Direct byte buffer for writing
    private static ByteBuffer     dbuf    = ByteBuffer.allocateDirect(1024);

    // Open and bind the server-socket channel
    //
    private static ServerSocketChannel setup() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress isa = new InetSocketAddress("localhost", port);
        ssc.socket().bind(isa);
        return ssc;
    }

    // Service the next request to come in on the given channel
    //
    private static void serve(ServerSocketChannel ssc) throws IOException {
        while (true) {
            SocketChannel sc = ssc.accept();
            try {
                String now = new Date().toString();
                sc.write(encoder.encode(CharBuffer.wrap(now + "\r\n")));
                System.out.println("server:" + sc.socket().getInetAddress() + " : " + now);
                sc.close();
            } finally {
                // Make sure we close the channel (and hence the socket)
                sc.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("Usage: java TimeServer [port]");
            return;
        }

        // If the first argument is a string of digits then we take that
        // to be the port number
        if ((args.length == 1) && Pattern.matches("[0-9]+", args[0])) port = Integer.parseInt(args[0]);

        ServerSocketChannel ssc = setup();
        for (;;)
            serve(ssc);

    }

}
