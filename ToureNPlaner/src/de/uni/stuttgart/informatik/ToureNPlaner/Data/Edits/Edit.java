package de.uni.stuttgart.informatik.ToureNPlaner.Data.Edits;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

public abstract class Edit {
	protected final Session session;

	public Edit(Session session) {
		this.session = session;
	}

	public abstract void perform();
}
