package com.dsintez;

public enum ECommand {
        SEND(10),
        RECEIVE(11),
        OK(20),
        ERROR(21),
        ;

        private final int commandCode;

        ECommand(int commandCode) {
            this.commandCode = commandCode;
        }

        public int getCommandCode() {
            return commandCode;
        }

}
