package pl.kmiecik.holistech.fis.application;

import org.springframework.stereotype.Service;
import pl.kmiecik.holistech.fis.application.port.IpClientService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Service
class IpClientUseCase implements IpClientService {

    @Override
    public String sendAndReceiveIPMessage(String ipAddress, Integer ipPort, String message) {
        String str = "";
        try (
                Socket mySocket = new Socket(ipAddress, ipPort);
                PrintWriter printWriter = new PrintWriter(mySocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()))
        ) {
            printWriter.println(message);
            str = in.readLine();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return str;
    }
}
