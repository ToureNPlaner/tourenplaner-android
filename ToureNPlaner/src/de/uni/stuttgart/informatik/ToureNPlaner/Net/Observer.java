package de.uni.stuttgart.informatik.ToureNPlaner.Net;

public interface Observer {
    void onCompleted(Object object);
    void onError(Object object);
}
