/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Collections;
import java.util.Date;

public class ToureNPlanerApplication extends Application {
	private static Context context;
	private static KeyStore keyStore;
	private static SSLContext sslContext;

	public static SSLContext getSslContext() {
		if (sslContext == null)
			setupSsl();
		return sslContext;
	}

	public static KeyStore getKeyStore() {
		if (keyStore == null)
			initKeystore();
		return keyStore;
	}

	private static void initKeystore() {
		try {
			keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(ToureNPlanerApplication.context.openFileInput("keystore"), null);
		} catch (Exception e) {
			try {
				keyStore.load(new KeyStore.LoadStoreParameter() {
					@Override
					public KeyStore.ProtectionParameter getProtectionParameter() {
						return new KeyStore.PasswordProtection(null);
					}
				});
			} catch (Exception e2) {
				Log.e("TP", "SSL", e2);
			}
		}
		// import the provided certificates
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(ToureNPlanerApplication.context.getResources().openRawResource(R.raw.keystore), "tourenplaner".toCharArray());
			for (String alias : Collections.list(ks.aliases())) {
				keyStore.setCertificateEntry(alias, ks.getCertificate(alias));
			}
		} catch (Exception e2) {
			Log.e("TP", "SSL", e2);
		}
	}

	public static void setupSsl() {
		TrustManagerFactory tmf;
		try {
			sslContext = SSLContext.getInstance("TLS");
			tmf = TrustManagerFactory.getInstance("X509");
		} catch (Exception e) {
			Log.e("TP", "SSL", e);
			return;
		}
		try {
			tmf.init(getKeyStore());
			sslContext.init(null, tmf.getTrustManagers(), null);
		} catch (Exception e) {
			Log.e("TP", "SSL", e);
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(new ProfileDumper());

		context = getApplicationContext();
		disableConnectionReuseIfNecessary();
		new Thread() {
			@Override
			public void run() {
				performCleanUp();
			}
		}.start();
	}

	public static Context getContext() {
		return context;
	}

	public static String getApplicationIdentifier() {
		String id = "";
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			id = info.packageName + " " + info.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			// should never happen
		}
		return id;
	}

	/**
	 * Cleans up old Session Files
	 */
	private static void performCleanUp() {
		File[] files = Session.openCacheDir().listFiles();

		if (files == null)
			return;

		long currentTime = new Date().getTime();

		for (File file : files) {
			// If file is older than a day
			if (currentTime - file.lastModified() >= 24 * 60 * 60 * 1000) {
				file.delete();
			}
		}
	}

	private static void disableConnectionReuseIfNecessary() {
		// HTTP connection reuse which was buggy pre-froyo
		// Build.VERSION_CODES.FROYO
		if (Build.VERSION.SDK_INT < 8) {
			System.setProperty("http.keepAlive", "false");
			Log.i("ToureNPlaner", "HTTP keep-alive disabled");
		}
	}

	private static class ProfileDumper implements Thread.UncaughtExceptionHandler {
		private final Thread.UncaughtExceptionHandler defaultExceptionHandler;

		private ProfileDumper() {
			defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		}

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			if (ex instanceof OutOfMemoryError) {
				synchronized (context) {
					try {
						Log.i("TP", "Dumping");
						android.os.Debug.dumpHprofData(Environment.getExternalStorageDirectory() + "/dump.hprof");
						Log.i("TP", "Dumping finished");
					} catch (IOException e) {
						Log.e("TP", "Couldn't dump", e);
					}
				}
			}

			defaultExceptionHandler.uncaughtException(thread, ex);
		}
	}
}
