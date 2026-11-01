package com.mariuszilinskas.streamix.security.filter;

import com.mariuszilinskas.streamix.security.properties.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class ApiKeyFilterTest {

    private static final String VALID_KEY = "secret-api-key";
    private static final String API_KEY_HEADER = "X-API-Key";

    private final SecurityProperties properties = new SecurityProperties(Map.of("service-a", VALID_KEY), null);
    private final ApiKeyFilter filter = new ApiKeyFilter(properties);

    @Test
    void allowsRequestWithValidKey() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(API_KEY_HEADER, VALID_KEY);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void rejectsRequestWithWrongKey() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(API_KEY_HEADER, "wrong-key");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(chain);
    }

    @Test
    void rejectsRequestWithBlankKey() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(API_KEY_HEADER, "   ");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(chain);
    }

    @Test
    void rejectsRequestWithMissingHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(chain);
    }

    @Test
    void rejectsRequestWhenNoInboundKeysConfigured() throws Exception {
        ApiKeyFilter noKeysFilter = new ApiKeyFilter(new SecurityProperties(null, null));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(API_KEY_HEADER, VALID_KEY);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        noKeysFilter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(chain);
    }

    @Test
    void allowsRequestWhenAnyConfiguredKeyMatches() throws Exception {
        SecurityProperties multiKeyProperties = new SecurityProperties(
                Map.of("service-a", VALID_KEY, "service-b", "another-secret"),
                null
        );
        ApiKeyFilter multiKeyFilter = new ApiKeyFilter(multiKeyProperties);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(API_KEY_HEADER, "another-secret");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        multiKeyFilter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

}