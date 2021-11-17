package pl.kmiecik.holistech.fis.domain;

public class FISVariantNotFoundException extends RuntimeException {

    public FISVariantNotFoundException(String message) {
        super("FIS server  response : "+ message);
    }

}
