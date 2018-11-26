package org.app.client.updater;

import org.app.client.updater.exception.AcuException;

public interface IUpdater {

	public boolean update() throws AcuException;

	public void rollback() throws AcuException;

	public void getLastVersion() throws AcuException;

	public void getActiveVersion() throws AcuException;

	public void installOrUpdate() throws AcuException;

	public void runApplication() throws AcuException;

	public void sendNotification() throws AcuException;

}
