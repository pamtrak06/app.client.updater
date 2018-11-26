package org.app.client.updater.config;

import org.app.client.updater.exception.AcuException;

public interface IValidation {

	// Validation
	public boolean validate() throws AcuException;

	// Validate json syntax content
	public boolean checkContent();

}
