import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;


class MainTest {

    @Test
    @Disabled("low running")
    void whenMainMethodLess22Seconds()  {
        String[] args = new String[]{};
        Assertions.assertTimeout(Duration.ofSeconds(22), () -> Main.main(args));
    }
}