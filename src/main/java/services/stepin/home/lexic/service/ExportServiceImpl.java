package services.stepin.home.lexic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;
import static java.lang.String.format;
import static services.stepin.home.lexic.model.LanguageCode.DE;

@Service
@RequiredArgsConstructor
@Log4j2
public class ExportServiceImpl implements ExportService {

    public static final int BATCH_SIZE = 3;

    private static final String PATH = "lexic.json";
    private final CardService cardService;
    private final ObjectMapper objectMapper;

    @Override
    public void exportAll() {

        long numberOfExported = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH))) {
            numberOfExported += exportForLanguage(DE, writer);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        String message = format("The dictionary of %d words was successfully exported to a file: %s",
                numberOfExported,
                PATH);

        Notification.show(message, 5_000, TOP_CENTER);

        check();
    }

    private long exportForLanguage(LanguageCode languageCode, BufferedWriter writer) {

        Pageable pageRequest = PageRequest.of(0, BATCH_SIZE);
        Slice<Card> batch = cardService.findAllByLanguageCode(languageCode, pageRequest);

        long numberOfExported = export(batch.getContent(), writer);

        while (batch.hasNext()) {
            pageRequest = batch.nextPageable();
            batch = cardService.findAllByLanguageCode(languageCode, pageRequest);

            numberOfExported += export(batch.getContent(), writer);
        }

        log.info(format(" %d words for language %s were successfully exported",
                numberOfExported,
                languageCode));

        return numberOfExported;
    }

    private int export(List<Card> cards, BufferedWriter writer) {
        cards.stream()
                .map(this::matToJsonAsString)
                .forEach(json -> writeJson(json, writer));

        return cards.size();
    }

    private String matToJsonAsString(Card card) {

        try {
            return objectMapper.writeValueAsString(card);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeJson(String json, BufferedWriter writer) {

        try {
            writer.write(json);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void check() {

        Path path = Paths.get(PATH);
        try (Stream<String> lines = Files.lines(path)) {

            lines.map(this::mapToCard)
                    .forEach(this::printCard);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Card mapToCard(String line) {
        try {
            return objectMapper.readValue(line, Card.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void printCard(Card card) {
        System.out.println(card);
    }

}