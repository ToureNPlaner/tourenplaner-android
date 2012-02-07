package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler.RawHandler;

public interface Observer {
	void onCompleted(RawHandler caller, Object object);

	void onError(RawHandler caller, Object object);
}
