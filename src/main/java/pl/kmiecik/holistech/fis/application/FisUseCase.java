package pl.kmiecik.holistech.fis.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.kmiecik.holistech.fis.application.port.FisService;
import pl.kmiecik.holistech.fis.domain.FISVariantNotFoundExeption;
import pl.kmiecik.holistech.fixture.domain.Fixture;

/**
 * FIS (Factory Information System)
 * Server   IP : 10.235.241.235  Port : 24431
 * "ADDFIXTURE|proces=ICT|fixture=1|status=PASS"
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FisUseCase implements FisService {

    @Value("${fis.ip}")
    private String ip;

    @Value("${fis.port}")
    private Integer port;

    @Override
    public void sendAndReceiveIPMessage(final String msg, final String ip, final Integer port) {
        final IpClient ipClient = new IpClient();
        //  final String actual = ipClient.sendAndReceiveIPMessage("10.235.241.235", 24364, "HandleGETSTATIONLIST|");
        final String receiveIPMessage = ipClient.sendAndReceiveIPMessage(ip, port, msg);
        if (receiveIPMessage.contains("FAIL")) {
            throw new FISVariantNotFoundExeption(receiveIPMessage);
        }
        log.info("my message is = " + msg + "--->>> FIS response is = " + receiveIPMessage);
        System.out.println(msg + receiveIPMessage);
    }

    @Override
    public String createADDFIXTURE(final String processName, final String fixture, final String status) {
        return "ADDFIXTURE|process=" + processName.trim().toUpperCase()
                + "|fixture=" + fixture.trim().toUpperCase()
                + "|status=" + status.trim().toUpperCase();
    }

    @Override
    public void sendFixtureStatusToFis(final Fixture fixture) {
        String messageToFis = createADDFIXTURE(fixture.getFisProcess().toString(), fixture.getName(), fixture.getStatusStrain().getDisplayFISStatusName());
        sendAndReceiveIPMessage(messageToFis, ip, port);
    }


}
