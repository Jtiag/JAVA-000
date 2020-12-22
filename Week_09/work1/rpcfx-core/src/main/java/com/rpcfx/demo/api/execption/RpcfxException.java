package com.rpcfx.demo.api.execption;

/**
 * @author jasper 2020/12/21 下午9:26
 * @version 1.0.0
 * @desc
 */
public class RpcfxException extends RuntimeException {
    private String msg;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public RpcfxException(String message, String msg) {
        super(message);
        this.msg = msg;
    }
}
