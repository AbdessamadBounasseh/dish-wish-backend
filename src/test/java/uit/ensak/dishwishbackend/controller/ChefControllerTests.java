package uit.ensak.dishwishbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uit.ensak.dishwishbackend.model.Chef;
import uit.ensak.dishwishbackend.service.ChefService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = ChefController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ChefControllerTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private Chef chef;
    @MockBean
    private ChefService chefService;

    public ChefControllerTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void init() {
        chef = new Chef();
        chef.setId(1L);
        chef.setFirstName("Cook");
        }

    @Test
    public void ChefController_GetChefById_ReturnChef() throws Exception {

        when(chefService.getChefById(anyLong())).thenReturn(chef);

        ResultActions response = mockMvc.perform(get("/chefs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chef)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(chef.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(chef.getId())));
    }
}
