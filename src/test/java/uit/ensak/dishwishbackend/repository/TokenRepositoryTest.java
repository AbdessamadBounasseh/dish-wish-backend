package uit.ensak.dishwishbackend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import uit.ensak.dishwishbackend.model.Client;
import uit.ensak.dishwishbackend.model.VerificationToken;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void TokenRepositoryTest_deleteByToken_tokenDeletedSuccessfully() {
        String tokenExample1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        String tokenExample2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4iLCJpYXQiOjE1MTYyMzkwMjJ9.A6Ak1IC1KhtSzAor4-i-bZhmCHQya-sRlPy9-DGgQwA";

        //arrange
        VerificationToken token1 = new VerificationToken();
        token1.setToken(tokenExample1);
        tokenRepository.save(token1);

        VerificationToken token2 = new VerificationToken();
        token2.setToken(tokenExample2);
        tokenRepository.save(token2);

        //act
        tokenRepository.deleteByToken(token1.getToken());

        //assert
        assertThat(tokenRepository.findAll().size()).isEqualTo(1);
        assertThat(tokenRepository.findAll().get(0).getToken()).isEqualTo(tokenExample2);
    }

    @Test
    void TokenRepositoryTest_findByCode_returnTheToken() {

        String code = "b46fd8";

        //arrange
        VerificationToken token = new VerificationToken();
        token.setCode(code);
        tokenRepository.save(token);

        //act
        VerificationToken retrieveToken = tokenRepository.findByCode(code).orElseThrow();

        //assert
        assertThat(retrieveToken).isNotNull();
        assertThat(retrieveToken.getCode()).isEqualTo(code);
    }

    @Test
    void TokenRepositoryTest_findByToken_returnTheToken() {

        String tokenExample = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        //arrange
        VerificationToken token = new VerificationToken();
        token.setToken(tokenExample);
        tokenRepository.save(token);

        //act
        VerificationToken retrieveToken = tokenRepository.findByToken(tokenExample).orElseThrow();

        //assert
        assertThat(retrieveToken).isNotNull();
        assertThat(retrieveToken.getToken()).isEqualTo(tokenExample);
    }

    @Test
    void TokenRepositoryTest_findAllValidTokenByUserId_getThreeValidTokens() {
        String tokenExample1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4iLCJpYXQiOjE1MTYyMzkwMjJ9.A6Ak1IC1KhtSzAor4-i-bZhmCHQya-sRlPy9-DGgQwA";
        String tokenExample2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvIG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.WlT_QjFWd85BQKBiUwiiBTyMC9MJnSvhk6XA8KcX4H8";
        String tokenExample3 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvIHMgb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.uVWOtv2cKHJBDtvGZhpw9q2TamT_ym_K8NqBj_V4C90";
        String tokenExample4 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        Client client = Client.builder()
                .id(1L)
                .email("junit@mail.com")
                .build();
        clientRepository.save(client);

        //arrange
        VerificationToken validToken1 = new VerificationToken();
        validToken1.setToken(tokenExample1);
        validToken1.setExpired(false);
        validToken1.setRevoked(false);
        validToken1.setClient(client);

        VerificationToken validToken2 = new VerificationToken();
        validToken2.setToken(tokenExample2);
        validToken2.setExpired(true);
        validToken2.setRevoked(false);
        validToken2.setClient(client);

        VerificationToken validToken3 = new VerificationToken();
        validToken3.setToken(tokenExample3);
        validToken3.setExpired(false);
        validToken3.setRevoked(true);
        validToken3.setClient(client);

        VerificationToken nonValidToken = new VerificationToken();
        nonValidToken.setToken(tokenExample4);
        nonValidToken.setExpired(true);
        nonValidToken.setRevoked(true);
        nonValidToken.setClient(client);

        tokenRepository.saveAll(List.of(validToken1, validToken2, validToken3, nonValidToken));

        //act
        List<VerificationToken> tokens = tokenRepository.findAllValidTokenByUserId(client.getId());

        //assert
        assertThat(tokens.size()).isEqualTo(3);
        assertThat(tokens.contains(nonValidToken)).isFalse();
    }

    @Test
    void TokenRepositoryTest_deleteAllByClientId_deleteOneOfTwo() {
        //arrange
        String tokenExample1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4iLCJpYXQiOjE1MTYyMzkwMjJ9.A6Ak1IC1KhtSzAor4-i-bZhmCHQya-sRlPy9-DGgQwA";
        String tokenExample2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvIG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.WlT_QjFWd85BQKBiUwiiBTyMC9MJnSvhk6XA8KcX4H8";

        Client client1 = Client.builder()
                .id(1L)
                .email("junit1@gmail.com")
                .build();

        Client client2 = Client.builder()
                .id(2L)
                .email("junit2@gmail.com")
                .build();

        clientRepository.saveAll(List.of(client1, client2));

        VerificationToken token1 = new VerificationToken();
        token1.setToken(tokenExample1);
        token1.setClient(client1);

        VerificationToken token2 = new VerificationToken();
        token2.setToken(tokenExample2);
        token2.setClient(client2);

        tokenRepository.saveAll(List.of(token1, token2));

        //act
        tokenRepository.deleteAllByClientId(client1.getId());

        //assert
        assertThat(tokenRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void TokenRepositoryTest_findAllByClientId_returnAllTokensOfTheClient() {
        //arrange
        String tokenExample1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4iLCJpYXQiOjE1MTYyMzkwMjJ9.A6Ak1IC1KhtSzAor4-i-bZhmCHQya-sRlPy9-DGgQwA";
        String tokenExample2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvIG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.WlT_QjFWd85BQKBiUwiiBTyMC9MJnSvhk6XA8KcX4H8";
        String tokenExample3 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvIHMgb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.uVWOtv2cKHJBDtvGZhpw9q2TamT_ym_K8NqBj_V4C90";

        Client client1 = Client.builder()
                .id(1L)
                .email("junit1@gmail.com")
                .build();

        Client client2 = Client.builder()
                .id(2L)
                .email("junit2@gmail.com")
                .build();

        clientRepository.saveAll(List.of(client1, client2));

        VerificationToken token1 = new VerificationToken();
        token1.setToken(tokenExample1);
        token1.setClient(client1);

        VerificationToken token2 = new VerificationToken();
        token2.setToken(tokenExample2);
        token2.setClient(client1);

        VerificationToken token3 = new VerificationToken();
        token3.setToken(tokenExample3);
        token3.setClient(client2);

        tokenRepository.saveAll(List.of(token1, token2, token3));

        //act
        List<VerificationToken> tokens = tokenRepository.findAllByClientId(client1.getId());

        //assert
        assertThat(tokens.size()).isEqualTo(2);

    }
}