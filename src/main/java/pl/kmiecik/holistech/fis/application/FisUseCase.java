package pl.kmiecik.holistech.fis.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kmiecik.holistech.config.CustomProperties;
import pl.kmiecik.holistech.fis.application.port.FisService;
import pl.kmiecik.holistech.fis.application.port.IpClientService;
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
class FisUseCase implements FisService {

    private final IpClientService ipClientService;
    private final CustomProperties customProperties;


    @Override
    public void sendAndReceiveIPMessage(final String msg, final String ip, final Integer port) {
        final String receiveIPMessage = ipClientService.sendAndReceiveIPMessage(ip, port, msg);
        if (receiveIPMessage.contains("FAIL")) {
            throw new FISVariantNotFoundExeption(receiveIPMessage);
        }
        log.info("my message is = " + msg + "--->>> FIS response is = " + receiveIPMessage);
        System.out.println(msg + receiveIPMessage);
    }

    @Override
    public String createADDFIXTURE(final String processName, final String fixture, final String status) {

        if (processName == null || fixture == null || status == null) {
            throw new IllegalArgumentException("Illegal argument");
        }
        return "ADDFIXTURE|process=" + processName.trim().toUpperCase()
                + "|fixture=" + fixture.trim().toUpperCase()
                + "|status=" + status.trim().toUpperCase();
    }

    @Override
    public void sendFixtureStatusToFis(final Fixture fixture) {
        String messageToFis = createADDFIXTURE(fixture.getFisProcess().toString(), fixture.getName(), fixture.getStatusStrain().getDisplayFISStatusName());
        String ip = customProperties.getFisip();
        Integer port = customProperties.getFisport();
        sendAndReceiveIPMessage(messageToFis, ip, port);
    }


}
