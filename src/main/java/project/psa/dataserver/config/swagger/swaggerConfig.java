package project.psa.dataserver.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;


@Configuration
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization"
)
@OpenAPIDefinition(
        info = @Info(
                title = "Data security API",
                version = "1.0",
                description = "Data security API reference for developers"
        ),
        security = @SecurityRequirement(name = "JWT") // Yêu cầu Authorization header
)
public class swaggerConfig {

}
