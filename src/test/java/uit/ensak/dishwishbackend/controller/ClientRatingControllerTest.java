package uit.ensak.dishwishbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.model.ClientRating;
import uit.ensak.dishwishbackend.model.Rating;
import uit.ensak.dishwishbackend.service.ClientRatingService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClientRatingControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Mock
    private ClientRatingService clientRatingService;

    @InjectMocks
    private ClientRatingController clientRatingController;

    public ClientRatingControllerTest() {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(clientRatingController).build();
    }

    @Test
    void getClientRatings_ReturnsClientRatings() throws Exception {
        Long clientId = 1L;
        List<Rating> mockRatings = Collections.singletonList(new ClientRating());
        when(clientRatingService.getClientRatings(anyLong())).thenReturn(mockRatings);

        ResultActions resultActions = mockMvc.perform(get("/client-ratings/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void rateClient_ReturnsClientRating() throws Exception {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setClientId(1L);
        ratingDTO.setChefId(2L);
        ratingDTO.setRating(4.5);

        ClientRating mockClientRating = new ClientRating();
        when(clientRatingService.addRatingToClient(ratingDTO)).thenReturn(mockClientRating);

        ResultActions resultActions = mockMvc.perform(post("/client-ratings/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingDTO)));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.client").exists())
                .andExpect(jsonPath("$.chef").exists())
                .andExpect(jsonPath("$.rating").value(4.5));
    }
}
