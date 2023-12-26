package uit.ensak.dishwishbackend.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Test
    void JwtService_extractUsername_succeed() {
        // given
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmRlc3NhbWFkYm91bmFzc2VoQGdtYWlsLmNvbSIsImlhdCI6MTcwMzU4Njg2NCwiZXhwIjoxNzAzNjczMjY0fQ.BMRwLIO0AoSyfCf1B4M1TE4faMvRH8RB1LtGXb_Ouww";
        String secretKey = "e0c352481de147d859946af7610bbe1c1626d4e939d828295977491197b67914";
        // associé à l'email 'abdessamadbounasseh@gmail.com'

        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", secretKey);

        // when
        String email = jwtService.extractUsername(token);

        // then
        assertThat(email).isEqualTo("abdessamadbounasseh@gmail.com");
    }

    @Test
    void JwtService_generateToken_succeed() {
    }

    @Test
    void JwtService_testGenerateToken_succeed() {
    }

    @Test
    void JwtService_generateRefreshToken_succeed() {
    }

    @Test
    void JwtService_isTokenValid_succeed() {
    }

    @Test
    void JwtService_isTokenExpired_succeed() {
    }

    @Test
    void JwtService_extractClaim_succeed() {
    }
}