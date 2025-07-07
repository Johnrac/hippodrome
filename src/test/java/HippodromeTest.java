import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class HippodromeTest {

    @Order(1)
    @Nested
    @DisplayName("Constructor test")
    class ConstructorTest {

        @Test
        @DisplayName("When null parameter then throw IAE")
        void whenNullParameter_thenThrowIAE() {
            assertThrows(IllegalArgumentException.class, () ->
                    createObject(null));
        }

        @Test
        @DisplayName("When null parameter then message exception \"Horses cannot be null.\"")
        void whenNullParameter_thenThrowMessage() {
            try {
                createObject(null);
            } catch (IllegalArgumentException e) {
                assertEquals("Horses cannot be null.", e.getMessage());
            }
        }


        @Test
        @DisplayName("When empty list then return throw IAE")
        void whenEmptyList_thenReturnThrowIAE() {
            ArrayList<Horse> emptyList = new ArrayList<>();
            assertThrows(IllegalArgumentException.class, () -> createObject(emptyList));
        }


        @Test
        @DisplayName("When list empty then return message exception \"Horses cannot be empty.\"")
        void whenListEmpty_thenThrowMessage() {
            try {
                createObject(new ArrayList<>());
            } catch (IllegalArgumentException e) {
                assertEquals("Horses cannot be empty.", e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Methods test")
    class MethodsTest {


        @Test
        void getHorses() {
            // given
            List<Horse> horses = createHorses(30);
            // when
            Hippodrome hippodrome = createObject(horses);
            // then
            assertEquals(horses, hippodrome.getHorses());
        }

        @Test
        void move() {
            // given
            List<Horse> horses = createHorses(50, true);
            Hippodrome hippodrome = new Hippodrome(horses);
            // when
            hippodrome.move();
            // then
            horses.forEach(horse -> verify(horse).move());
        }

        @Test
        void getWinner() {
            // given
            List<Horse> horses = createHorses(50, true);
            Hippodrome hippodrome = new Hippodrome(horses);
            Horse expectedHorse = horses.stream()
                    .max(Comparator.comparing(Horse::getDistance))
                    .orElse(null);
            // when
            Horse actualHorse = hippodrome.getWinner();
            // then
            assertEquals(expectedHorse, actualHorse);
        }
    }

    private List<Horse> createHorses(int count, boolean isMock) {
        Random rnd = new Random();
        List<Horse> horses = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Horse horse = isMock
                    ? mock(Horse.class)
                    : new Horse("horse" + i, i * rnd.nextInt(100), rnd.nextInt(count));
            horses.add(horse);
        }
        return horses;
    }

    private List<Horse> createHorses(int count) {
        return createHorses(count, false);
    }

    private Hippodrome createObject(List<Horse> horses) {
        return new Hippodrome(horses);
    }
}