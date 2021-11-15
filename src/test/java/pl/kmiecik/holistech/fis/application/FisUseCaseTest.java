package pl.kmiecik.holistech.fis.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.kmiecik.holistech.fis.application.port.FisService;
import pl.kmiecik.holistech.fis.application.port.IpClientService;
import pl.kmiecik.holistech.fis.domain.FISVariantNotFoundExeption;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class FisUseCaseTest {
    FisService fisService;
    IpClientService ipClientService;
    @Test
    @DisplayName("should Send And Receive IPMessage")
    void shouldSendAndReceiveIPMessage() {
        //given
        ipClientService = Mockito.mock(IpClientService.class);
        fisService = new FisUseCase(ipClientService);
        String fisMsg="ADDFIXTURE|process=ICT|fixture=FIXT1|status=PASS";
        String ip = "10.235.241.235";
        int port = 24431;
        Mockito.when(ipClientService.sendAndReceiveIPMessage(ip, port,fisMsg)).thenReturn("BCMP|status=FAIL");
        //when

        //then
        assertThatExceptionOfType(FISVariantNotFoundExeption.class).isThrownBy(()->fisService.sendAndReceiveIPMessage(fisMsg, ip, port));
    }

    @Test
    @DisplayName("should Create ADDFIXTURE message from lowercase")
    void shouldCreateADDFIXTUREfromlowercase() {
        //given
        ipClientService= Mockito.mock(IpClientService.class);
        String expected="ADDFIXTURE|process=ICT|fixture=FIXT1|status=PASS";
        //when
        String actual = fisService.createADDFIXTURE(" icT "," fixT1  "," PasS ");
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("should throw When Process Is Null For CreateADDFIXTURE")
    void shouldthrowWhenProcessIsNullForCreateADDFIXTURE() {
        //given
        String expected="ADDFIXTURE|process=ICT|fixture=FIXT1|status=PASS";
        //when
        //then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                ()->fisService.createADDFIXTURE(null," fixT1  "," PasS "));

    }

    @Test
    @DisplayName("should throw When fixture Is Null For CreateADDFIXTURE")
    void shouldthrowWhenfixtureIsNullForCreateADDFIXTURE() {
        //given
        String expected="ADDFIXTURE|process=ICT|fixture=FIXT1|status=PASS";
        //when
        //then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                ()->fisService.createADDFIXTURE("ict",null," PasS "));
    }

    @Test
    @DisplayName("should throw When status Is Null For CreateADDFIXTURE")
    void shouldthrowWhenstatusIsNullForCreateADDFIXTURE() {
        //given
        String expected="ADDFIXTURE|process=ICT|fixture=FIXT1|status=PASS";
        //when
        //then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                ()->fisService.createADDFIXTURE("ict","fixT1" ,null));
    }

    @BeforeEach
    void setUp() {
        fisService = new FisUseCase(ipClientService);
    }
}