package com.urlshortener.url_shortener.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.urlshortener.url_shortener.entity.ShortUrl;
import com.urlshortener.url_shortener.repository.ShortUrlRepository;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    ShortUrlRepository repository;

    @InjectMocks
    UrlService service;

    @Test
    void createShortUrl_returnsCodeAndSavesEntity() {
        // Arrange: não é preciso configurar nada.
        // Sem stubs, findByShortCode devolve Optional.empty(), ou seja, o código ainda não existe.

        // Act: chamar o método que estamos a testar
        String shortCode = service.createShortUrl("https://example.com");

        // Assert: o código devolvido é válido
        assertNotNull(shortCode);
        assertEquals(6, shortCode.length());

        // Verify: o repository foi chamado para guardar exatamente uma entidade
        ArgumentCaptor<ShortUrl> captor = ArgumentCaptor.forClass(ShortUrl.class);
        verify(repository, times(1)).save(captor.capture());

        // Assert sobre o que foi guardado
        ShortUrl saved = captor.getValue();
        assertEquals("https://example.com", saved.getUrlOriginal());
        assertEquals(shortCode, saved.getShortCode());
    }

    @Test 
    void getOriginalUrl_return(){
        ShortUrl entity = new ShortUrl("https://example.com", "abc123");
        when(repository.findByShortCode("abc123")).thenReturn(Optional.of(entity));

        // Act
        String result = service.getOriginalUrl("abc123");

        // Assert: a URL devolvida é a guardada, e o counter subiu para 1
        assertEquals("https://example.com", result);
        assertEquals(1, entity.getCounter());

        // Verify: o service guardou a entity
        verify(repository).save(entity);

    }

    @Test
    void createShortUrl_rejectsUrlWithoutHttpScheme() {
        assertThrows(IllegalArgumentException.class,
            () -> service.createShortUrl("www.example.com"));

        verify(repository, never()).save(any());
    }

    @Test
    void createShortUrl_throwsWhenNoUniqueCodeFound() {
        // Arrange: o repository diz sempre que o código já existe
        when(repository.findByShortCode(anyString()))
            .thenReturn(Optional.of(new ShortUrl("https://x.com", "taken")));

        // Act + Assert: tem de lançar exceção, não ficar preso
        assertThrows(IllegalStateException.class,
            () -> service.createShortUrl("https://example.com"));
    }
}
