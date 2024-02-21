import com.example.carmodels.constants.ErrorConst;
import com.example.carmodels.exception.DataValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ExceptionTests {
    @Test
    public void dataValidationExceptionTest() {
        DataValidationException exception = new DataValidationException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.VALIDATION_FAILED);
    }

}
