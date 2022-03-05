package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.echo.BgsProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
//import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.srv.Server;

import java.io.IOException;

public class TPCMain {
    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            int port = Integer.parseInt(args[0]);
            Server.threadPerClient(port, () -> new BgsProtocol(), LineMessageEncoderDecoder::new).serve();
        } else{
            System.out.println("You need to supply only port");
        }
    }
}

