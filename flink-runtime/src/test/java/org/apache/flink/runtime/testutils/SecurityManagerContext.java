package org.apache.flink.runtime.testutils;

import org.apache.flink.util.function.RunnableWithException;

/**
 * Enables to run code with a {@link SecurityManager}.
 */
public class SecurityManagerContext implements AutoCloseable {

	private final SecurityManager previousSecurityManager;

	public SecurityManagerContext(final SecurityManager newSecurityManager) {
		this.previousSecurityManager = System.getSecurityManager();
		System.setSecurityManager(newSecurityManager);
	}

	public static void runWithSecurityManager(final SecurityManager securityManager, final RunnableWithException runnable) {
		try (SecurityManagerContext ignored = new SecurityManagerContext(securityManager)) {
			try {
				runnable.run();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void close() {
		System.setSecurityManager(previousSecurityManager);
	}
}
