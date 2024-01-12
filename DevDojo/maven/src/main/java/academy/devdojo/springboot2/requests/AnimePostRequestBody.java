package academy.devdojo.springboot2.requests;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@NotNull
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {
@NotEmpty(message = "The anime name, cannot be empty")
@Schema(description = "This is the Animes's name", example = "DatteBayo", required = true)
    private String name;

}



