package io.github.openguava.guavatool.core.exception;

/**
 * 序列化异常
 * @author openguava
 *
 */
public class SerializationException extends RuntimeException {

	private static final long serialVersionUID = -1L;
	
	public SerializationException(String msg) {
        super(msg);
    }
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
