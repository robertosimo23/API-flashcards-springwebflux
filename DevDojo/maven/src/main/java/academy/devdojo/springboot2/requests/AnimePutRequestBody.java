package academy.devdojo.springboot2.requests;

import academy.devdojo.springboot2.domain.Anime;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePutRequestBody {
    private String name;
    @Getter
    private Long id;
}
