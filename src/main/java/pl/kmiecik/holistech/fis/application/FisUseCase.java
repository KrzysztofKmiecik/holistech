package pl.kmiecik.holistech.fis.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kmiecik.holistech.config.CustomProperties;
import pl.kmiecik.holistech.fis.application.port.FisService;
import pl.kmiecik.holistech.fis.application.port.IpClientService;
import pl.kmiecik.holistech.fis.application.port.IpClientService.IpCommunicationResponse;
import pl.kmiecik.holistech.fis.domain.FISNoCommunicationException;
import pl.kmiecik.holistech.fis.domain.FISVariantNotFoundException;
import pl.kmiecik.holistech.fixture.domain.Fixture;

/**
 * FIS (Factory Information System)
 * Server   IP : 10.235.241.235  Port : 24431
 * "ADDFIXTURE|proces=ICT|fixture=1|status=PASS"
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
class FisUseCase implements FisService {

    private final IpClientService ipClientService;
    private final CustomProperties customProperties;


    @Override
    public void sendAndReceiveIPMessage(final String msg, final String ip, final Integer port) {
        IpCommunicationResponse ipCommunicationResponse = ipClientService.sendAndReceiveIPMessage(ip, port, msg);
        if (!ipCommunicationResponse.isConnected()) {
            throw new FISNoCommunicationException("No Fis Communication");
        }
        final String receiveIPMessage = ipCommunicationResponse.getResponseMessage();
        if (receiveIPMessage.contains("FAIL")) {
            throw new FISVariantNotFoundException(receiveIPMessage);
        }
        log.info("my message is = " + msg + "--->>> FIS response is = " + receiveIPMessage);
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
