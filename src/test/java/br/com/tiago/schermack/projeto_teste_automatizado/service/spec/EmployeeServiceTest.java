package br.com.tiago.schermack.projeto_teste_automatizado.service.spec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.tiago.schermack.projeto_teste_automatizado.dto.EmployeeRequestDTO;
import br.com.tiago.schermack.projeto_teste_automatizado.dto.EmployeeResponseDTO;
import br.com.tiago.schermack.projeto_teste_automatizado.entity.Employee;
import br.com.tiago.schermack.projeto_teste_automatizado.repository.EmployeeRepository;
import br.com.tiago.schermack.projeto_teste_automatizado.service.impl.EmployeeService;
import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Deve criar um funcionário e retornar um EmployeeResponseDTO")
    void shouldCreateEmployeeAndReturnReponseDTO() {

        //arrange
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO("Tiago", "tiago.schermack@gmail.com");
        Employee employeeSaved = new Employee (requestDTO.firstName(), requestDTO.email());
        employeeSaved.setId(1L);    

        when(employeeRepository.save(any(Employee.class)))
            .thenReturn(employeeSaved);

        //act
        EmployeeResponseDTO responseDTO = employeeService.create(requestDTO);

        //assert
        assertEquals(1L                       , responseDTO.id        ());
        assertEquals("Tiago"                    , responseDTO.firstName ());
        assertEquals("tiago.schermack@gmail.com", responseDTO.email     ());

        verify(employeeRepository, times(1))
            .save(any(Employee.class));
    }

    @Test
    @DisplayName("Deve atualizar um funcionário e retornar um EmployeeResponseDTO")
    void shouldUpdateEmployeeAndReturnResponseDTO() {

        //arrange
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO("Tiago", "tiago.schermack@gmail.com");
        Employee employeeUpdate = new Employee ("Joao", "joao@gmail.com");
        employeeUpdate.setId(1L);    

        when(employeeRepository.findById(employeeUpdate.getId()))
            .thenReturn(java.util.Optional.of(employeeUpdate));

        //act
        EmployeeResponseDTO responseDTO = employeeService.update(employeeUpdate.getId(), requestDTO);

        //assert
        assertEquals(employeeUpdate.getId()              ,responseDTO.id       ());
        assertEquals("Tiago"                    , responseDTO.firstName());
        assertEquals("tiago.schermack@gmail.com", responseDTO.email    ());

        verify(employeeRepository, times(1)).findById(employeeUpdate.getId());
    }

    @Test
    @DisplayName("Deve lançar um erro quando o funcionário não for encontrado e retornar um EmployeeResponseDTO")
    void shouldThrowAnErrorWhenTheEmployeeIsNotFoundAndReturnAnEmployeeResponseDTO() {

        //arrange
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO("Tiago", "tiago.schermack@gmail.com");

        when(employeeRepository.findById(1L))
            .thenReturn(Optional.empty());

        //assert-act
        assertThrows(EntityNotFoundException.class, () -> employeeService.update(1L, requestDTO));
    }
}
