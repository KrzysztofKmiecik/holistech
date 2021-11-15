package pl.kmiecik.holistech.fis.application.port;

public interface IpClientService {
    String sendAndReceiveIPMessage(String ipAdress, Integer ipPort, String message);
}
