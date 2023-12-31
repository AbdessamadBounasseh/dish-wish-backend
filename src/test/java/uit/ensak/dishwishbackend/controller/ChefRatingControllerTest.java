package uit.ensak.dishwishbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uit.ensak.dishwishbackend.dto.RatingDTO;
import uit.ensak.dishwishbackend.exception.ClientNotFoundException;
import uit.ensak.dishwishbackend.model.ChefRating;
import uit.ensak.dishwishbackend.service.ChefRatingService;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChefRatingController.class)
class ChefRatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ChefRatingService chefRatingService;

    @InjectMocks
    private ChefRatingController chefRatingController;

    @Test
    void getChefRatings_shouldReturnListOfRatings() throws Exception {
        Long chefId = 1L;
        ChefRating chefRating = new ChefRating();
        Mockito.when(chefRatingService.getChefRatings(chefId)).thenReturn(Collections.singletonList(chefRating));

        mockMvc.perform(get("/chef-ratings/{chefId}", chefId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").exists());

        Mockito.verify(chefRatingService).getChefRatings(chefId);
    }

    @Test
    void rateChef_shouldReturnRatedChef() throws Exception {
        RatingDTO ratingDTO = new RatingDTO();
        ChefRating chefRating = new ChefRating();
        Mockito.when(chefRatingService.addRatingToChef(ratingDTO)).thenReturn(chefRating);

        mockMvc.perform(post("/chef-ratings/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(ratingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());

        Mockito.verify(chefRatingService).addRatingToChef(ratingDTO);
    }
}

