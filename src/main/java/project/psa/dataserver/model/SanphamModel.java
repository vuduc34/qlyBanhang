package project.psa.dataserver.model;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SanphamModel {
    private String tensp;
    private String dvt;
    private String nuocsx;
    private BigDecimal gia;
    private String status;
}
