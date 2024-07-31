package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;


public class CreateCustomerUseCaseTest {

    @Test
    @DisplayName("Criar um cliente")
    public void createCustomerUseCase() {
        //given
        final var expectedCPF = "123456789";
        final var expectedEmail = "joao@email.com";
        final var expectedName = "Joao";

        final var createInputCustomerUseCase = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        Mockito.when(customerService.findByCpf(expectedCPF)).thenReturn(Optional.empty());
        Mockito.when(customerService.findByEmail(expectedEmail)).thenReturn(Optional.empty());
        Mockito.when(customerService.save(Mockito.any())).thenAnswer(
                a -> {
                    var customer = a.getArgument(0, Customer.class);
                    customer.setId(UUID.randomUUID().getMostSignificantBits());
                    return customer;
                }
        );

        final var userCase = new CreateCustomerUseCase(customerService);
        final var output = userCase.execute(createInputCustomerUseCase);

        //then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedCPF, output.cpf());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Cliente não pode ter CPF duplicado")
    public void createCustomerUseCaseWithDuplicateCPF() {
        //given
        final var expectedCPF = "123456789";
        final var expectedEmail = "joao@email.com";
        final var expectedName = "Joao";

        final var expectedErrorMessage = "Customer already exists";

        final var createInputCustomerUseCase = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

        final var returnCustomer = new Customer();
        returnCustomer.setId(UUID.randomUUID().getMostSignificantBits());
        returnCustomer.setCpf(expectedCPF);
        returnCustomer.setEmail(expectedEmail);
        returnCustomer.setName(expectedName);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        Mockito.when(customerService.findByCpf(expectedCPF)).thenReturn(Optional.of(returnCustomer));

        final var userCase = new CreateCustomerUseCase(customerService);
        final var output = Assertions.assertThrows(ValidationException.class, () -> userCase.execute(createInputCustomerUseCase));

        //then
        Assertions.assertEquals(expectedErrorMessage, output.getMessage());
    }

    @Test
    @DisplayName("Cliente não pode ter Email duplicado")
    public void createCustomerUseCaseWithDuplicateEmail() {
        //given
        final var expectedCPF = "123456789";
        final var expectedEmail = "joao@email.com";
        final var expectedName = "Joao";

        final var expectedErrorMessage = "Customer already exists";

        final var createInputCustomerUseCase = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

        final var returnCustomer = new Customer();
        returnCustomer.setId(UUID.randomUUID().getMostSignificantBits());
        returnCustomer.setCpf(expectedCPF);
        returnCustomer.setEmail(expectedEmail);
        returnCustomer.setName(expectedName);

        //when
        final var customerService = Mockito.mock(CustomerService.class);
        Mockito.when(customerService.findByEmail(expectedEmail)).thenReturn(Optional.of(returnCustomer));

        final var userCase = new CreateCustomerUseCase(customerService);
        final var output = Assertions.assertThrows(ValidationException.class, () -> userCase.execute(createInputCustomerUseCase));

        //then
        Assertions.assertEquals(expectedErrorMessage, output.getMessage());
    }

}
