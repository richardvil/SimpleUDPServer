import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPServer extends Thread {

    private DatagramSocket socket;
    private byte[] buf = new byte[1024];
    private Path logPath;

    UDPServer(int port, String logsFile) throws IOException {
        logPath = Paths.get(logsFile);
        if (!Files.exists(logPath)) {
            System.out.println("*** Creating log file ***");
            Files.createFile(logPath);
        }

        System.out.println("*** Starting server on port " + port + " ***");
        socket = new DatagramSocket(port);
    }

    public void run() {
        boolean running = true;

        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String dStr = sdfDateTime.format(new Date());

                byte[] data = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());

                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                String received = new String(data, 0, data.length);

                Files.write(logPath, data, StandardOpenOption.APPEND);

                if (received.equals("end")) {
                    running = false;
                    continue;
                }

                System.out.println("[Received] [" + dStr + "] [" + address.getCanonicalHostName() + ":" + port + "] " + Utils.convertByteArrayToHexString(data));

                String hexCmd = "29 29 21 00 05 BA B1 06 29 0D";
                byte[] loginConfirmation = Utils.convertHexStringToByteArray(hexCmd);
                DatagramPacket sendPacket = new DatagramPacket(loginConfirmation, loginConfirmation.length, address, port);
                socket.send(sendPacket);

                dStr = sdfDateTime.format(new Date());
                System.out.println("[Sent] [" + dStr + "] [" + address.getCanonicalHostName() + ":" + port + "] " + Utils.convertByteArrayToHexString(loginConfirmation));

                Files.write(logPath, loginConfirmation, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}
