package com.crackers.vinayakatraders.exception;

public class InvalidInventoryStateTransitionException extends RuntimeException {
    public InvalidInventoryStateTransitionException() {
        super("can only update records in Ordered or Received state");
    }
}
