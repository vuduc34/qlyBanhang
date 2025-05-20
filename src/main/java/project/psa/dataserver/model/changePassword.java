package project.psa.dataserver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class changePassword {
    private String maNV;
    private String currentPassword;
    private String newPassword;
}
