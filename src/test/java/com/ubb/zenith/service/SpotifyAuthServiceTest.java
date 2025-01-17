package com.ubb.zenith.service;

import com.ubb.zenith.exception.UserNotFoundException;
import com.ubb.zenith.model.User;
import com.ubb.zenith.repository.UserRepository;
import lombok.SneakyThrows;
import okhttp3.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyAuthServiceTest {

    @InjectMocks
    private SpotifyAuthService spotifyAuthService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OkHttpClient mockClient;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Setează mock-ul OkHttpClient utilizând reflecția
        Field clientField = SpotifyAuthService.class.getDeclaredField("client");
        clientField.setAccessible(true);
        clientField.set(spotifyAuthService, mockClient);

        // Setează valorile pentru clientId, clientSecret și redirectUri
        Field clientIdField = SpotifyAuthService.class.getDeclaredField("clientId");
        clientIdField.setAccessible(true);
        clientIdField.set(spotifyAuthService, "mockClientId");

        Field clientSecretField = SpotifyAuthService.class.getDeclaredField("clientSecret");
        clientSecretField.setAccessible(true);
        clientSecretField.set(spotifyAuthService, "mockClientSecret");

        Field redirectUriField = SpotifyAuthService.class.getDeclaredField("redirectUri");
        redirectUriField.setAccessible(true);
        redirectUriField.set(spotifyAuthService, "http://localhost/callback");
    }

    @Test
    void getAccessTokenSuccess() throws IOException {
        // Arrange
        String expectedToken = "mockAccessToken";

        String responseBody = """
                {
                    "access_token": "mockAccessToken",
                    "token_type": "Bearer",
                    "expires_in": 3600
                }
                """;

        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://mockurl.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("")
                .body(ResponseBody.create(responseBody, MediaType.get("application/json")))
                .build();

        Call mockCall = mock(Call.class);
        when(mockClient.newCall(any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act
        String token = spotifyAuthService.getAccessToken();

        // Assert
        assertNotNull(token);
        assertEquals(expectedToken, token);
    }

    @Test
    void refreshAccessTokenSuccess() throws IOException {
        // Arrange
        String refreshToken = "mockRefreshToken";
        String expectedToken = "newMockAccessToken";

        String responseBody = """
                {
                    "access_token": "newMockAccessToken",
                    "token_type": "Bearer",
                    "expires_in": 3600
                }
                """;

        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://mockurl.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("")
                .body(ResponseBody.create(responseBody, MediaType.get("application/json")))
                .build();

        Call mockCall = mock(Call.class);
        when(mockClient.newCall(any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act
        String token = spotifyAuthService.refreshAccessToken(refreshToken);

        // Assert
        assertNotNull(token);
        assertEquals(expectedToken, token);
    }

    @SneakyThrows
    @Test
    void exchangeCodeForTokenSuccess() throws IOException {
        // Arrange
        String code = "mockAuthCode";
        String accessToken = "mockAccessToken";
        String refreshToken = "mockRefreshToken";

        String responseBody = """
                {
                    "access_token": "mockAccessToken",
                    "refresh_token": "mockRefreshToken",
                    "expires_in": 3600
                }
                """;

        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://mockurl.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("")
                .body(ResponseBody.create(responseBody, MediaType.get("application/json")))
                .build();

        Call mockCall = mock(Call.class);
        when(mockClient.newCall(any())).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act
        JSONObject result = spotifyAuthService.exchangeCodeForToken(code);

        // Assert
        assertNotNull(result);
        assertEquals(accessToken, result.getString("access_token"));
        assertEquals(refreshToken, result.getString("refresh_token"));
    }

    @Test
    void saveTokensSuccess() {
        // Arrange
        String username = "testUser";
        String accessToken = "mockAccessToken";
        String refreshToken = "mockRefreshToken";
        int expiresIn = 3600;

        User mockUser = new User();
        mockUser.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        assertDoesNotThrow(() -> spotifyAuthService.saveTokens(username, accessToken, refreshToken, expiresIn));

        // Assert
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void saveTokensUserNotFound() {
        // Arrange
        String username = "nonExistentUser";
        String accessToken = "mockAccessToken";
        String refreshToken = "mockRefreshToken";
        int expiresIn = 3600;

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            spotifyAuthService.saveTokens(username, accessToken, refreshToken, expiresIn);
        });
        assertEquals("User not found: nonExistentUser", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).save(any(User.class));
    }
}
