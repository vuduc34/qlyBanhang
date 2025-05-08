package project.psa.dataserver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class loginResponse {
	private String token;
	private String username;
	private String role;
	private String fullname;
	private String phoneNumber;
}
