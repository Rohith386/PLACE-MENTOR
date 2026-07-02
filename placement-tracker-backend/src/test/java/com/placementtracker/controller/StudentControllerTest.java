package com.placementtracker.controller;

import com.placementtracker.dto.StudentDTO;
import com.placementtracker.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void getProfileWhenStudentMissingReturnsDefaultProfile() throws Exception {
        given(studentService.getStudentProfile("clerk-1")).willReturn(null);

        mockMvc.perform(get("/students/profile")
                .header("X-Clerk-ID", "clerk-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(""))
                .andExpect(jsonPath("$.lastName").value(""))
                .andExpect(jsonPath("$.email").value(""));
    }
}
