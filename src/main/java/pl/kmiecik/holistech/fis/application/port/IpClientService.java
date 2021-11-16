package pl.kmiecik.holistech.fis.application.port;

public interface IpClientService {
    String sendAndReceiveIPMessage(String ipAddress, Integer ipPort, String message);
}
