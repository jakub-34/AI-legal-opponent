package legalopponent.domain;

import java.util.List;

public record LegalResponse(
        String answer,
        List<String> relevantSources
) {
}
