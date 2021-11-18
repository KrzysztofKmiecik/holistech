package pl.kmiecik.holistech.fis.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

public interface IpClientService {
    IpCommunicationResponse sendAndReceiveIPMessage(String ipAddress, Integer ipPort, String message);

    @Data
    @AllArgsConstructor
    class IpCommunicationResponse {
        public static final IpCommunicationResponse SUCCESS = new IpCommunicationResponse(true, Collections.emptyList(), "");

        boolean connected;
        List<String> errors;
        String responseMessage;
    }
}

