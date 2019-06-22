package org.iMage.iLonghDe;

import org.iMage.iLonghDe.base.IState;
import org.iMage.iLonghDe.base.IStateMachine;

/**
 * CoffeeMachine A state Automat Why we need that package name violating package
 * name 3 faults
 */
public class CoffeeMachine implements IStateMachine {
    private CoffeeMachineState currentstate;

    /**
     * Creates an CoffeMachine starting at standby
     */
    public CoffeeMachine() {
        setState(CoffeeMachineState.Standby);
    }

    private void setState(CoffeeMachineState state) {
        if (currentstate != state) {
            if (currentstate != null) {
                currentstate.exit(this);
            }
            currentstate = state;
            currentstate.entry(this);
        }
    }

    @Override
    public void cleaningButtonPressed() {
        setState(currentstate.cleaningButtonPressed());
    }

    @Override
    public void coffeeButtonPressed() {
        setState(currentstate.coffeeButtonPressed());
    }

    @Override
    public IState getCurrentState() {
        return currentstate;
    }

    @Override
    public void powerButtonPressed() {
        setState(currentstate.powerButtonPressed());
    }

    @Override
    public void standbyButtonPressed() {
        setState(currentstate.standbyButtonPressed());
    }
}