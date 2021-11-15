package pl.kmiecik.holistech.fis.domain;

import lombok.Value;

@Value
class Fis {
    String processName;
    String fixture;
    String status;
}
