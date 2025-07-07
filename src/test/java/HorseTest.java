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
        @DisplayName("When first null parameter then throw IAE")
        void whenFirstNullParameter_thenThrowIAE() {
            assertThrows(IllegalArgumentException.class, () ->
                    createObjectWithFirstParameter(null));
        }

        @Test
        @DisplayName("When first null parameter then message exception \"Name cannot be null.\"")
        void whenFirstNullParameter_thenThrowMessage() {
            try {
                createObjectWithFirstParameter(null);
            } catch (IllegalArgumentException e) {
                assertEquals("Name cannot be null.", e.getMessage());
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {" ", "\t", "\n", "\f", "\r",})
        @DisplayName("When space char then return throw IAE")
        void whenSpaceChars_thenReturnThrowIAE(String argument) {
            assertThrows(IllegalArgumentException.class, () ->
                    createObjectWithFirstParameter(argument));
        }

        @ParameterizedTest
        @ValueSource(strings = {" ", "\t", "\n", "\f", "\r",})
        @DisplayName("When space char then return message exception \"Name cannot be blank.\"")
        void whenSpaceChars_thenThrowMessage(String argument) {
            try {
                createObjectWithFirstParameter(argument);
            } catch (IllegalArgumentException e) {
                assertEquals("Name cannot be blank.", e.getMessage());
            }
        }




        @Test
        @DisplayName("When second parameter negative then throw IAE")
        void whenSecondParameterNegative_thenThrowIAE() {
            assertThrows(IllegalArgumentException.class, () ->
                    createObjectWithSecondParameter(-2));
        }

        @Test
        @DisplayName("When second negative parameter then return message exception \"Speed cannot be negative.\"")
        void whenSecondNegativeParameter_thenThrowMessage() {
            try {
                createObjectWithSecondParameter(-2);
            } catch (IllegalArgumentException e) {
                assertEquals("Speed cannot be negative.", e.getMessage());
            }
        }



        @Test
        @DisplayName("When third parameter negative then throw IAE")
        void whenThirdParameterNegative_thenThrowIAE() {
            assertThrows(IllegalArgumentException.class, () ->
                    createObjectWithThirdParameter(-20));
        }

        @Test
        @DisplayName("When third negative parameter then message exception \"Distance cannot be negative.\"")
        void whenThirdNegativeParameter_thenThrowMessage() {
            try {
                createObjectWithThirdParameter(-20);
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
            Horse horse = createObjectWithFirstParameter(firstParameter);
            assertEquals(firstParameter,horse.getName());
        }

        @Test
        @DisplayName("When call method getSpeed then return second parameter constructor")
        void getSpeed() {
            int secondParameter = 2;
            Horse horse = createObjectWithSecondParameter(secondParameter);
            assertEquals(secondParameter,horse.getSpeed());
        }

        @Test
        @DisplayName("When call method getDistance then return third parameter constructor")
        void getDistance() {
            int thirdParameter = 30;
            Horse horse = createObjectWithThirdParameter(thirdParameter);
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
            Horse horse = createObjectWithFirstParameter("horse");
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

    Horse createObjectWithFirstParameter(String firstParameter) {
        return new Horse(firstParameter, 2, 20);
    }
    Horse createObjectWithSecondParameter(int secondParameter) {
        return new Horse("horse", secondParameter, 20);
    }
    Horse createObjectWithThirdParameter(int thirdParameter) {
        return new Horse("horse", 20, thirdParameter);
    }

}