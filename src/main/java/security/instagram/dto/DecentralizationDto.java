package security.instagram.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DecentralizationDto {
    @JsonIgnore
    private Long id;

    @NotBlank(message = "Role is required")
    private String role;          // USER or ADMIN

    @NotBlank(message = "API pattern is required")
    private String apiPattern;    // e.g. /v1/api/users/**

    @NotBlank(message = "HTTP method is required")
    @Pattern(regexp = "(?i)^(ALL|(?:(GET|POST|PUT|DELETE|PATCH|OPTIONS|HEAD)(;(GET|POST|PUT|DELETE|PATCH|OPTIONS|HEAD))*)$)",
             message = "Invalid http methods format")
    private String httpMethod;    // e.g. GET;POST or ALL
}