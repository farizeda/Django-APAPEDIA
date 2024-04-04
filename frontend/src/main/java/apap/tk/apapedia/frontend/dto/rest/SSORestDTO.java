package apap.tk.apapedia.frontend.dto.rest;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "serviceResponse", namespace = "http://www.yale.edu/tp/cas")
@XmlAccessorType(XmlAccessType.FIELD)
public class SSORestDTO {
    @XmlElement(name = "authenticationFailure", namespace = "http://www.yale.edu/tp/cas")
    private String authenticationFailure;

    @XmlElement(name = "authenticationSuccess", namespace = "http://www.yale.edu/tp/cas")
    private AuthenticationSuccess authenticationSuccess;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AuthenticationSuccess {
        @XmlElement(name = "user", namespace = "http://www.yale.edu/tp/cas")
        private String user;
    }
}
