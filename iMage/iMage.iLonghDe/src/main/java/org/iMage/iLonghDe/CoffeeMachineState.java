package org.iMage.iLonghDe;

import org.iMage.iLonghDe.base.IState;
import org.iMage.iLonghDe.base.IStateMachine;

public enum CoffeeMachineState implements IState {
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
            return null;
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
            return Cleaing;
        }

    },

    Cleaing {
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

    }
    ;

    /**
     * entry function
     */
    public abstract void entry(IStateMachine machine);

    /**
     * exit function
     */
    public abstract void exit(IStateMachine machine);

    /**
     * power Button pressed
     */
    public abstract CoffeeMachineState powerButtonPressed();

    /**
     * standby Button pressed
     */
    public abstract CoffeeMachineState  standbyButtonPressed();

    /**
     * coffee Button pressed
     */
    public abstract CoffeeMachineState  coffeeButtonPressed();

    /**
     * cleaning Button pressed
     */
    public abstract CoffeeMachineState  cleaningButtonPressed();
}