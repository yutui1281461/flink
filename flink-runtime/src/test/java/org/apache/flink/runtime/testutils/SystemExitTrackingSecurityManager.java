package org.apache.flink.runtime.testutils;

import java.security.Permission;

import static org.apache.flink.util.Preconditions.checkState;

/**
 * SecurityManager implementation that forbids and tracks calls to {@link System#exit(int)}.
 */
public class SystemExitTrackingSecurityManager extends SecurityManager {

	private int status;

	private int count;

	@Override
	public void checkPermission(final Permission perm) {

	}

	@Override
	public void checkPermission(final Permission perm, final Object context) {

	}

	@Override
	public synchronized void checkExit(final int status) {
		this.status = status;
		++count;
		throw new SecurityException("SystemExitTrackingSecurityManager is installed. JVM will not exit");
	}

	public synchronized int getStatus() {
		checkState(isSystemExitCalled());
		return status;
	}

	public synchronized boolean isSystemExitCalled() {
		return getCount() > 0;
	}

	public synchronized int getCount() {
		return count;
	}

}
