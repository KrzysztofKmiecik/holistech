package pl.kmiecik.holistech.fis.domain;

public class FISVariantNotFoundExeption extends RuntimeException {

    public FISVariantNotFoundExeption(String message) {
        super("FIS server  response : "+ message);
    }


}
