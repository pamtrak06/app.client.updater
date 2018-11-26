package org.app.client.updater.ui;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class ProgressRenderer extends JProgressBar implements TableCellRenderer {

	private static final long serialVersionUID = -5871743255687310401L;

	public ProgressRenderer(int min, int max) {
		super(min, max);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		setValue((int) ((Float) value).floatValue());
		return this;
	}
}
