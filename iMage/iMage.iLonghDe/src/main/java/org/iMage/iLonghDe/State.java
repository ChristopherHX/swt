package org.iMage.iLonghDe;

import org.iMage.iLonghDe.base.IState;
import org.iMage.iLonghDe.base.IStateMachine;

public enum State implements IState {
    Standby {
        @Override
        public void entry(IStateMachine machine) {
            machine.display("");
        }

        @Override
        public void exit(IStateMachine machine) {

        }

        @Override
        public State powerButtonPressed() {
            return null;
        }

        @Override
        public State standbyButtonPressed() {
            return Waiting;
        }

        @Override
        public State coffeeButtonPressed() {
            return this;
        }

        @Override
        public State cleaningButtonPressed() {
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
        public State powerButtonPressed() {
            return this;
        }

        @Override
        public State standbyButtonPressed() {
            return Standby;
        }

        @Override
        public State coffeeButtonPressed() {
            return Brewing;
        }

        @Override
        public State cleaningButtonPressed() {
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
        public State powerButtonPressed() {
            return this;
        }

        @Override
        public State standbyButtonPressed() {
            return Standby;
        }

        @Override
        public State coffeeButtonPressed() {
            return this;
        }

        @Override
        public State cleaningButtonPressed() {
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
        public State powerButtonPressed() {
            return this;
        }

        @Override
        public State standbyButtonPressed() {
            return Standby;
        }

        @Override
        public State coffeeButtonPressed() {
            return Waiting;
        }

        @Override
        public State cleaningButtonPressed() {
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
        public State powerButtonPressed() {
            return this;
        }

        @Override
        public State standbyButtonPressed() {
            return this;
        }

        @Override
        public State coffeeButtonPressed() {
            return this;
        }

        @Override
        public State cleaningButtonPressed() {
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
    public abstract State powerButtonPressed();

    /**
     * standby Button pressed
     */
    public abstract State  standbyButtonPressed();

    /**
     * coffee Button pressed
     */
    public abstract State  coffeeButtonPressed();

    /**
     * cleaning Button pressed
     */
    public abstract State  cleaningButtonPressed();
}