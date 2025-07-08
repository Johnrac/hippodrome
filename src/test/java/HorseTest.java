import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class HorseTest {

    private MockedStatic<Horse> horseMockedStatic;

    @BeforeEach
    void setUp() {
        horseMockedStatic = mockStatic(Horse.class);
    }

    @AfterEach
    void tearDown() {
        horseMockedStatic.close();
    }

    @Order(1)
    @Nested
    @DisplayName("Constructor test")
    class ConstructorTest {

        @Test
        @DisplayName("When name null then throw IAE")
        void whenNameNull_thenThrowIAE() {
            assertThrows(IllegalArgumentException.class, () ->
                    createObjectForName(null));
        }

        @Test
        @DisplayName("When name null then message exception \"Name cannot be null.\"")
        void whenNameNull_thenThrowMessage() {
            try {
                createObjectForName(null);
            } catch (IllegalArgumentException e) {
                assertEquals("Name cannot be null.", e.getMessage());
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {" ", "\t", "\n", "\f", "\r",})
        @DisplayName("When name is blank then return throw IAE")
        void whenNameIsBlank_thenReturnThrowIAE(String argument) {
            assertThrows(IllegalArgumentException.class, () ->
                    createObjectForName(argument));
        }

        @ParameterizedTest
        @ValueSource(strings = {" ", "\t", "\n", "\f", "\r",})
        @DisplayName("When name is blank then return message exception \"Name cannot be blank.\"")
        void whenNameIsBlank_thenThrowMessage(String argument) {
            try {
                createObjectForName(argument);
            } catch (IllegalArgumentException e) {
                assertEquals("Name cannot be blank.", e.getMessage());
            }
        }

        @Test
        @DisplayName("When speed is negative then throw IAE")
        void whenSpeedIsNegative_thenThrowIAE() {
            assertThrows(IllegalArgumentException.class, () ->
                    createObjectForSpeed(-2));
        }

        @Test
        @DisplayName("When speed is negative negative then return message exception \"Speed cannot be negative.\"")
        void whenSpeedIsNegative_thenThrowMessage() {
            try {
                createObjectForSpeed(-2);
            } catch (IllegalArgumentException e) {
                assertEquals("Speed cannot be negative.", e.getMessage());
            }
        }

        @Test
        @DisplayName("When distance is negative then throw IAE")
        void whenThirdParameterNegative_thenThrowIAE() {
            assertThrows(IllegalArgumentException.class, () ->
                    createObjectForDistance(-20));
        }

        @Test
        @DisplayName("When distance is negative then message exception \"Distance cannot be negative.\"")
        void whenDistanceIsNegative_thenThrowMessage() {
            try {
                createObjectForDistance(-20);
            } catch (IllegalArgumentException e) {
                assertEquals("Distance cannot be negative.", e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Methods test")
    class MethodsTest {

        @Test
        @DisplayName("When call method getName then return first parameter constructor")
        void getName() {
            String firstParameter = "horse";
            Horse horse = createObjectForName(firstParameter);
            assertEquals(firstParameter,horse.getName());
        }

        @Test
        @DisplayName("When call method getSpeed then return second parameter constructor")
        void getSpeed() {
            int secondParameter = 2;
            Horse horse = createObjectForSpeed(secondParameter);
            assertEquals(secondParameter,horse.getSpeed());
        }

        @Test
        @DisplayName("When call method getDistance then return third parameter constructor")
        void getDistance() {
            int thirdParameter = 30;
            Horse horse = createObjectForDistance(thirdParameter);
            assertEquals(thirdParameter,horse.getDistance());
        }

        @Test
        @DisplayName("When create object with constructor with two parameters then method getDistance return 0")
        void getDistanceDefault() {
            Horse horse = new Horse("horse",20);
            assertEquals(0,horse.getDistance());
        }

        @Test
        @DisplayName("When call method move then call static method getRandomDouble with parameter 0.2 and 0.9")
        void verifyCallGetRandomDoubleWithParameters() {
            // given
            Horse horse = createObjectForName("horse");
            // when
            horse.move();
            // then
            horseMockedStatic.verify(() -> Horse.getRandomDouble(0.2,0.9));
        }

        static Stream<Double> sourceValues() {
            return DoubleStream.iterate(0.2, n -> n + 0.1)
                    .limit(8)
                    .boxed();
        }

        @ParameterizedTest
        @MethodSource("sourceValues")
        void move(double parameter) {
            // given
            horseMockedStatic.when(() -> Horse.getRandomDouble(0.2,0.9)).thenReturn(parameter);
            int speed = 2;
            int distance = 20;
            String name = "horse";
            Horse horse = new Horse(name,speed,distance);
            // when
            horse.move();
            // then
            assertEquals(distance + speed * parameter, horse.getDistance());
        }

    }

    Horse createObjectForName(String name) {
        return new Horse(name, 2, 20);
    }
    Horse createObjectForSpeed(int speed) {
        return new Horse("horse", speed, 20);
    }
    Horse createObjectForDistance(int distance) {
        return new Horse("horse", 20, distance);
    }

}