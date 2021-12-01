package eu.telecomnancy.resttest;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.telecomnancy.resttest.Model.Club;
import eu.telecomnancy.resttest.ModelVue.ClubModel;
import eu.telecomnancy.resttest.Service.ClubService;
import eu.telecomnancy.resttest.Service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest
public class ClubControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ClubService clubService;
    @MockBean
    StudentService studentService;

    Club RECORD_1 = new Club("Coffee");
    Club RECORD_2 = new Club("Beer");
    Club RECORD_3 = new Club("Danse");

    ClubModel club1 = new ClubModel(RECORD_1);
    ClubModel club2 = new ClubModel(RECORD_2);
    ClubModel club3 = new ClubModel(RECORD_3);


    @Test
    public void getAllClubs_success() throws Exception {
        List<Club> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

        Mockito.when(clubService.getAllClubs()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/clubs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void getClubById_success() throws Exception {
        Mockito.when(clubService.findClubById(0L)).thenReturn(RECORD_1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/clubs/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Coffee")));
    }

    @Test
    public void createClub_success() throws Exception {
        Club club = Club.builder()
                .name("Animest")
                .build();

        Mockito.when(clubService.createClub("Animest")).thenReturn(club);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/clubs")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(club));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Animest")));
    }

    @Test
    public void updateClub_success() throws Exception {
        Club updatedRecord = Club.builder()
                .id(1L)
                .name("Dessin")
                .build();

        Mockito.when(clubService.getClub(RECORD_1.getId())).thenReturn(RECORD_1);
        Mockito.when(clubService.updateClub(1L,"Dessin")).thenReturn(updatedRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/clubs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedRecord));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Dessin")));
   }

}

