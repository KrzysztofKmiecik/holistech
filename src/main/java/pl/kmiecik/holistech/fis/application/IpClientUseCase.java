package pl.kmiecik.holistech.fis.application;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kmiecik.holistech.fis.application.port.IpClientService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

@Service
@Data
@Slf4j
class IpClientUseCase implements IpClientService {

    private final IpCommunicationResponse ipCommunicationResponse;

    @Override
    public IpCommunicationResponse sendAndReceiveIPMessage(String ipAddress, Integer ipPort, String message) {

        try (
                Socket mySocket = new Socket(InetAddress.getByName(ipAddress.trim()), ipPort);
                PrintWriter printWriter = new PrintWriter(mySocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()))
        ) {
            printWriter.println(message);
            ipCommunicationResponse.setResponseMessage(in.readLine());
            ipCommunicationResponse.setConnected(true);
            ipCommunicationResponse.setErrors(Collections.emptyList());
            return ipCommunicationResponse;
        } catch (Exception e) {
            ipCommunicationResponse.setConnected(false);
            ipCommunicationResponse.setResponseMessage("Error Connection");
            ipCommunicationResponse.setErrors(List.of(e.getMessage()));
            log.error("Network "+ e.getMessage());
            return ipCommunicationResponse;
        }
    }
}
