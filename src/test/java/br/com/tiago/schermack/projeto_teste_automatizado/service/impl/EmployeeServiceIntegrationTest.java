package br.com.tiago.schermack.projeto_teste_automatizado.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.tiago.schermack.projeto_teste_automatizado.dto.EmployeeRequestDTO;
import br.com.tiago.schermack.projeto_teste_automatizado.dto.EmployeeResponseDTO;
import br.com.tiago.schermack.projeto_teste_automatizado.entity.Employee;
import br.com.tiago.schermack.projeto_teste_automatizado.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@Transactional
public class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Deve criar um funcionário e retornar um EmployeeResponseDTO")
    void shouldCreateEmployeeAndReturnReponseDTO() {

        //arrange
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO("Tiago", "tiago.schermack@gmail.com");
        
        //act
        EmployeeResponseDTO responseDTO = employeeService.create(requestDTO);
        
        //assert
        assertEquals("Tiago", responseDTO.firstName());
        assertEquals("tiago.schermack@gmail.com", responseDTO.email());

    }

    @Test
    @DisplayName("Deve atualizar um funcionário e retornar um EmployeeResponseDTO")
    void shouldUpdateEmployeeAndReturnResponseDTO() {

         //arrange
        EmployeeRequestDTO requestDTO = 
            new EmployeeRequestDTO("Tiago", "tiago.schermack@gmail.com");

        EmployeeResponseDTO createEmployee = 
            employeeService.create(new EmployeeRequestDTO("João", "João.GG@Gmail.com"));

        //act
        EmployeeResponseDTO responseDTO = employeeService.update(createEmployee.id(), requestDTO);

        //assert
        assertEquals(createEmployee.id(), responseDTO.id());
        assertEquals("Tiago", responseDTO.firstName());
        assertEquals("tiago.schermack@gmail.com", responseDTO.email());
    }

    @Test
    @DisplayName("Deve lançar um erro quando o funcionário não for encontrado e retornar um EmployeeResponseDTO")
    void shouldThrowAnErrorWhenTheEmployeeIsNotFoundAndReturnAnEmployeeResponseDTO() {

        //arrange
        EmployeeRequestDTO requestDTO = 
            new EmployeeRequestDTO("Tiago", "tiago.schermack@gmail.com");

            assertThrows(EntityNotFoundException.class, () -> employeeService.update(666L, requestDTO));
    }

    @Test
    @DisplayName("deve deletar um funcionário com sucesso")
    void shouldDeleteEmployeeSuccessfully() {

        //arrange
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO("Tiago", "tiago.schermack@gmail.com");
        
        //act
        EmployeeResponseDTO responseDTO = employeeService.create(requestDTO);
        employeeService.delete(responseDTO.id());

        //assert
        assertThrows(EntityNotFoundException.class, () -> employeeService.findById(responseDTO.id()));
    }
}
