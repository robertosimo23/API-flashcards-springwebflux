package dev.RobertoSimoes.reactiveflashcards.API.Controller.request;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Sort;

import static dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserSortBy.NAME;
import static dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserSortDirection.ASC;
import static dev.RobertoSimoes.reactiveflashcards.API.Controller.request.UserSortDirection.DESC;

public record UserPageRequest(@JsonProperty("sentence")
                              @Schema(description = "texto para filtrar por nome e email (case insensitive)", example = "ana")
                              String sentence,
                              @PositiveOrZero
                              @JsonProperty("page")
                              @Schema(description = "pagina solicitada", example = "1", defaultValue = "0")
                              Long page,
                              @Min(1)
                              @Max(50)
                              @JsonProperty("limit")
                              @Schema(description = "tamanho da página", example = "30", defaultValue = "20")
                              Integer limit,
                              @JsonProperty("sortBy")
                              @Schema(description = "campo para ordenação", enumAsRef = true, defaultValue = "NAME")
                              UserSortBy sortBy,
                              @JsonProperty("sortDirection")
                              @Schema(description = "sentido da ordenaçào", enumAsRef = true, defaultValue = "ASC")
                              UserSortDirection sortDirection) {

    @Builder(toBuilder = true)
    public UserPageRequest {
        sortBy = ObjectUtils.defaultIfNull(sortBy, NAME);
        sortDirection = ObjectUtils.defaultIfNull(sortDirection, ASC);
        limit = ObjectUtils.defaultIfNull(limit, 20);
        page = ObjectUtils.defaultIfNull(page, 0L);
    }

    @Schema(hidden = true)
    public Sort getSort() {
        return sortDirection.equals(DESC) ? Sort.by(sortBy.getField()).descending() : Sort.by(sortBy.getField()).ascending();
    }

    @Schema(hidden = true)
    public Long getSkip() {
        return page > 0 ? ((page - 1) * limit) : 0;
    }
}
