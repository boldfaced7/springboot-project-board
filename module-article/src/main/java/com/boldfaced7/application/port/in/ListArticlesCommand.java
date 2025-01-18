package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ListArticlesCommand(
        @NotNull @Size(min = 0) Integer pageNumber
) {}
