package de.uni.stuttgart.informatik.ToureNPlaner.Net;

public interface Observer {
    void onCompleted(ConnectionHandler caller, Object object);
    void onError(ConnectionHandler caller, Object object);
}
