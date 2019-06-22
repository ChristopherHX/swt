package org.iMage.iLonghDe;

import org.iMage.iLonghDe.base.IState;
import org.iMage.iLonghDe.base.IStateMachine;

/**
 * States for the CoffeeMachine StateMachine
 */
public enum CoffeeMachineState implements IState {
    /**
     * The Machine is in standby
     */
    Standby {
        @Override
        public void entry(IStateMachine machine) {
            machine.display("");
        }

        @Override
        public void exit(IStateMachine machine) {

        }

        @Override
        public CoffeeMachineState powerButtonPressed() {
            return Off;
        }

        @Override
        public CoffeeMachineState standbyButtonPressed() {
            return Waiting;
        }

        @Override
        public CoffeeMachineState coffeeButtonPressed() {
            return this;
        }

        @Override
        public CoffeeMachineState cleaningButtonPressed() {
            return this;
        }
    },
    
    /**
     * The Machine is waiting for input
     */
    Waiting {
        @Override
        public void entry(IStateMachine machine) {
            machine.display("Waiting");
        }

        @Override
        public void exit(IStateMachine machine) {

        }

        /**
         * Realy why not turn off
         */
        @Override
        public CoffeeMachineState powerButtonPressed() {
            return this;
        }

        @Override
        public CoffeeMachineState standbyButtonPressed() {
            return Standby;
        }

        @Override
        public CoffeeMachineState coffeeButtonPressed() {
            return Brewing;
        }

        @Override
        public CoffeeMachineState cleaningButtonPressed() {
            return Cleaning;
        }

    },

    /**
     * The Machine is cleaning itself
     */
    Cleaning {
        @Override
        public void entry(IStateMachine machine) {
            machine.display("Cleaning");
        }

        @Override
        public void exit(IStateMachine machine) {

        }

        /**
         * Realy why not turn off
         */
        @Override
        public CoffeeMachineState powerButtonPressed() {
            return this;
        }

        @Override
        public CoffeeMachineState standbyButtonPressed() {
            return Standby;
        }

        @Override
        public CoffeeMachineState coffeeButtonPressed() {
            return this;
        }

        @Override
        public CoffeeMachineState cleaningButtonPressed() {
            return Waiting;
        }

    },

    /**
     * The Machine is brewing Coffee
     */
    Brewing {
        @Override
        public void entry(IStateMachine machine) {
            machine.display("BrewCoffee");
        }

        @Override
        public void exit(IStateMachine machine) {
            machine.display("Done");
        }

        /**
         * Realy why not turn off
         */
        @Override
        public CoffeeMachineState powerButtonPressed() {
            return this;
        }

        @Override
        public CoffeeMachineState standbyButtonPressed() {
            return Standby;
        }

        @Override
        public CoffeeMachineState coffeeButtonPressed() {
            return Waiting;
        }

        @Override
        public CoffeeMachineState cleaningButtonPressed() {
            return this;
        }

    },
    /**
     * State which does nothing than keep the state
     * Could accept powerbutton to turn on in real world
     */
    Off {

        @Override
        public void entry(IStateMachine machine) {

        }

        @Override
        public void exit(IStateMachine machine) {

        }

        @Override
        public CoffeeMachineState powerButtonPressed() {
            return this;
        }

        @Override
        public CoffeeMachineState standbyButtonPressed() {
            return this;
        }

        @Override
        public CoffeeMachineState coffeeButtonPressed() {
            return this;
        }

        @Override
        public CoffeeMachineState cleaningButtonPressed() {
            return this;
        }

    };

    /**
     * entry function
     * @param machine Coffemachine object for display
     */
    public abstract void entry(IStateMachine machine);

    /**
     * exit function
     * @param machine Coffemachine object for display
     */
    public abstract void exit(IStateMachine machine);

    /**
     * power Button pressed
     * @return CoffeeMachineState enum after this button was pressed / same (no change)
     */
    public abstract CoffeeMachineState powerButtonPressed();

    /**
     * standby Button pressed
     * @return CoffeeMachineState enum after this button was pressed / same (no change)
     */
    public abstract CoffeeMachineState  standbyButtonPressed();

    /**
     * coffee Button pressed
     * @return CoffeeMachineState enum after this button was pressed / same (no change)
     */
    public abstract CoffeeMachineState  coffeeButtonPressed();

    /**
     * cleaning Button pressed
     * @return CoffeeMachineState enum after this button was pressed / same (no change)
     */
    public abstract CoffeeMachineState  cleaningButtonPressed();
}