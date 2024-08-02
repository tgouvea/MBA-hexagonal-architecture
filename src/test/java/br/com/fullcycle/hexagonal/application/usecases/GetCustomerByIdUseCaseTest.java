package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GetCustomerByIdUseCaseTest {

    @Test
    @DisplayName("Obter Cliente por Id")
    public void testGetClientById() {

        //given
        final var expectedId = UUID.randomUUID().getMostSignificantBits();
        final var expectedCPF = "123456789";
        final var expectedEmail = "joao@email.com";
        final var expectedName = "Joao";

        final var returnCustomer = new Customer();
        returnCustomer.setId(expectedId);
        returnCustomer.setCpf(expectedCPF);
        returnCustomer.setEmail(expectedEmail);
        returnCustomer.setName(expectedName);

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        Mockito.when(customerService.findById(expectedId)).thenReturn(Optional.of(returnCustomer));

        final var userCase = new GetCustomerByIdUseCase(customerService);
        final var output = userCase.execute(input).get();


        //then
        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedCPF, output.cpf());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());

    }

    @Test
    @DisplayName("Obter vazio ao realizar busca de cliente por Id")
    public void testGetClientByIdWithInvalidId() {

        //given
        final var expectedId = UUID.randomUUID().getMostSignificantBits();

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        Mockito.when(customerService.findById(expectedId)).thenReturn(Optional.empty());

        final var userCase = new GetCustomerByIdUseCase(customerService);
        final var output = userCase.execute(input);


        //then
        Assertions.assertTrue(output.isEmpty());
    }

}