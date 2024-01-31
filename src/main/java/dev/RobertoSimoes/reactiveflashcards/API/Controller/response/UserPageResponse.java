package dev.RobertoSimoes.reactiveflashcards.API.Controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

public record UserPageResponse(@JsonProperty("currentPage")
                               @Schema(description = "pagina restornada", example = "1")
                               Long currentPage,
                               @JsonProperty("totalPages")
                               @Schema(description = "total de paginas", example = "20")
                               Long totalPages,
                               @JsonProperty(" totalItems")
                               @Schema(description = "soma", example = "quantidade de registro paginados")
                               Long totalItems,
                               @JsonProperty("content")
                               @Schema(description = "usuarios da pagina")
                               List<UserResponse> content) {
    public static PageUserResponseBuilder builder() {
        return new PageUserResponseBuilder();
    }

    public PageUserResponseBuilder toBuilder(final Integer limit) {
        return new PageUserResponseBuilder(limit, currentPage, totalItems, content);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageUserResponseBuilder {
        private Integer limit;
        private Long currentPage;
        private Long totalItems;
        private List<UserResponse> content;

        public PageUserResponseBuilder limit(final Integer limit) {
            this.limit = limit;
            return this;
        }

        public PageUserResponseBuilder currentPage(final Long currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public PageUserResponseBuilder totalItems(final Long totalItems) {
            this.totalItems = totalItems;
            return this;
        }

        public PageUserResponseBuilder content(final List<UserResponse> content) {
            this.content = content;
            return this;
        }

        public UserPageResponse build() {
            var totalPages = (totalItems / limit) + ((totalItems % limit > 0) ? 1 : 0);
            return new UserPageResponse(currentPage, totalPages, totalItems, content);
        }
    }
}
