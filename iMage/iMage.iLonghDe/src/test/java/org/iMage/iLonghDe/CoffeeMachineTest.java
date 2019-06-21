package org.iMage.iLonghDe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * CoffeeMachineTest
 */
public class CoffeeMachineTest {
    private CoffeeMachine machine;

    /**
     * Creates a new CoffeeMachine per Test
     */
    @Before
    public void setUp() {
        machine = new CoffeeMachine();
    }

    /**
     * Tests Brewing Coffee
     */
    @Test
    public void BrewingCoffeeTest() {
        var standbystate = machine.getCurrentState();
        machine.standbyButtonPressed();
        var waitstate = machine.getCurrentState();
        assertNotEquals(standbystate, waitstate);
        machine.coffeeButtonPressed();
        var coffeestate = machine.getCurrentState();
        assertNotEquals(waitstate, coffeestate);
        machine.coffeeButtonPressed();
        assertEquals(waitstate, machine.getCurrentState());
        machine.standbyButtonPressed();
        assertEquals(standbystate, machine.getCurrentState());
    }
}