package pl.kmiecik.holistech.fis.application.port;

import pl.kmiecik.holistech.fixture.domain.Fixture;

public interface FisService {

    void sendAndReceiveIPMessage(String msg, String ip, Integer port);

    String createADDFIXTURE(String processName, String fixture, String status);

    void sendFixtureStatusToFis(Fixture fixture);
}
