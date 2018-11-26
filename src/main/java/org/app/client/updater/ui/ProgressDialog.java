package org.app.client.updater.ui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JProgressBar;

import org.app.client.updater.remote.Download;

public class ProgressDialog extends JProgressBar implements Observer {

	private static final long serialVersionUID = -1062976394870514360L;

	public ProgressDialog(int min, int max) {
		super(min, max);
	}

	@Override
	public void setValue(int arg0) {
		// TODO Auto-generated method stub
		super.setValue(arg0);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		setValue((int) ((Download) arg0).getProgress());
	}

}
