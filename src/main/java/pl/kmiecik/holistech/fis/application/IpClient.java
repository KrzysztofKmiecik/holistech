package pl.kmiecik.holistech.fis.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class IpClient {

    public String sendAndReceiveIPMessage(String ipAdress, Integer ipPort, String message) {
        String str = "";
        try (
                Socket mySocket = new Socket(ipAdress, ipPort);
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
