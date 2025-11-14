import org.example.Car;
import org.example.Engine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestMock {

    @Mock
    private Engine engine;

    @InjectMocks
    private Car car;

    @Test
    void test(){
        when(engine.start()).thenReturn("Engine is damage");

        String result = car.drive();
        assertEquals("Engine is damage",result);

        verify(engine).start();
    }


}