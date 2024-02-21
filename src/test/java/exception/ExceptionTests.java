package exception;

import com.example.carmodels.constants.ErrorConst;
import com.example.carmodels.exception.AccessDeniedException;
import com.example.carmodels.exception.DataValidationException;
import com.example.carmodels.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ExceptionTests {
    @Test
    public void dataValidationExceptionTest() {
        DataValidationException exception = new DataValidationException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.VALIDATION_FAILED);
    }

    @Test
    public void accessDeniedExceptionTest() {
        AccessDeniedException exception = new AccessDeniedException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.ACCESS_DENIED);
    }

    @Test
    public void resourceNotFoundExceptionTest() {
        ResourceNotFoundException exception = new ResourceNotFoundException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.RESOURCE_NOT_FOUND);
    }

}
