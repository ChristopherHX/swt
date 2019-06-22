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
    public void brewingCoffeeTest() {
        var standbystate = machine.getCurrentState();
        machine.standbyButtonPressed();
        var waitstate = machine.getCurrentState();
        assertNotEquals(standbystate, waitstate);
        machine.coffeeButtonPressed();
        var coffeestate = machine.getCurrentState();
        assertNotEquals(waitstate, coffeestate);
        // Need to keep the state for some Buttons
        machine.cleaningButtonPressed();
        assertEquals(coffeestate, machine.getCurrentState());
        machine.powerButtonPressed();
        assertEquals(coffeestate, machine.getCurrentState());
        machine.coffeeButtonPressed();
        assertEquals(waitstate, machine.getCurrentState());
        machine.standbyButtonPressed();
        assertEquals(standbystate, machine.getCurrentState());
    }

    /**
     * Tests Cleaning the machine
     */
    @Test
    public void cleaningTest() {
        var standbystate = machine.getCurrentState();
        machine.standbyButtonPressed();
        var waitstate = machine.getCurrentState();
        assertNotEquals(standbystate, waitstate);
        machine.cleaningButtonPressed();
        var cleaningstate = machine.getCurrentState();
        assertNotEquals(waitstate, cleaningstate);
        machine.cleaningButtonPressed();
        assertEquals(waitstate, machine.getCurrentState());
        machine.standbyButtonPressed();
        assertEquals(standbystate, machine.getCurrentState());
    }

    /**
     * Waiting and turn off machine Test
     */
    @Test
    public void waitingAndTurnOffTest() {
        assertEquals(CoffeeMachineState.Standby, machine.getCurrentState());
        machine.standbyButtonPressed();
        assertEquals(CoffeeMachineState.Waiting, machine.getCurrentState());
        machine.standbyButtonPressed();
        assertEquals(CoffeeMachineState.Standby, machine.getCurrentState());
        machine.powerButtonPressed();
        assertEquals(CoffeeMachineState.Off, machine.getCurrentState());
    }

    /**
     * Waiting, brewing, cleaning and turn off machine Test
     */
    @Test
    public void waitingBrewingCleaningAndTurnOffTest() {
        assertEquals(CoffeeMachineState.Standby, machine.getCurrentState());
        machine.standbyButtonPressed();
        assertEquals(CoffeeMachineState.Waiting, machine.getCurrentState());
        machine.coffeeButtonPressed();
        assertEquals(CoffeeMachineState.Brewing, machine.getCurrentState());
        machine.standbyButtonPressed();
        assertEquals(CoffeeMachineState.Standby, machine.getCurrentState());
        machine.standbyButtonPressed();
        assertEquals(CoffeeMachineState.Waiting, machine.getCurrentState());
        machine.cleaningButtonPressed();
        assertEquals(CoffeeMachineState.Cleaning, machine.getCurrentState());
        machine.standbyButtonPressed();
        assertEquals(CoffeeMachineState.Standby, machine.getCurrentState());
        machine.powerButtonPressed();
        assertEquals(CoffeeMachineState.Off, machine.getCurrentState());
    }
}