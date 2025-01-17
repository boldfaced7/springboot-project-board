package com.boldfaced7.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomPage<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<T> content;
    private int pageNumber;
    private int totalPages;
    private long totalElements;

    public <U> CustomPage<U> map(Function<T, U> converter) {
        List<U> result = content.stream().map(converter).toList();

        return CustomPage.<U>builder()
                .content(result)
                .pageNumber(pageNumber)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    public static <T> CustomPage<T> empty() {
        return CustomPage.<T>builder()
                .content(List.of())
                .pageNumber(0)
                .totalPages(0)
                .totalElements(0)
                .build();
    }

    public static <T> CustomPage<T> convert(Page<T> page) {
        return CustomPage.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }
}
