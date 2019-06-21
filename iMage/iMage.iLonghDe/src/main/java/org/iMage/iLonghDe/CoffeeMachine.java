package org.iMage.iLonghDe;

import org.iMage.iLonghDe.base.IState;
import org.iMage.iLonghDe.base.IStateMachine;

/**
 * CoffeeMachine A state Automat Why we need that package name violating package
 * name 3 faults
 */
public class CoffeeMachine implements IStateMachine {
    @Override
    public void cleaningButtonPressed() {

    }

    @Override
    public void coffeeButtonPressed() {

    }

    @Override
    public IState getCurrentState() {
        return null;
    }

    @Override
    public void powerButtonPressed() {

    }

    @Override
    public void standbyButtonPressed() {

    }
}