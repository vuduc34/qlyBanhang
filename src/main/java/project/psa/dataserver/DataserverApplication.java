package project.psa.dataserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class DataserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataserverApplication.class, args);
	}

}
//  cloudflared tunnel --url https://localhost:8443 --no-tls-verify
