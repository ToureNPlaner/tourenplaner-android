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
import de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute.ClientGraph;
import de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute.NullGraph;
import de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute.SimpleGraph;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import org.apache.commons.io.input.TeeInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.util.Collections;
import java.util.Date;

public class ToureNPlanerApplication extends Application {
	public static ClientGraph core;
	public static  int coreLevel = 40;
	public static String coreURL = "http://tourenplaner.informatik.uni-stuttgart.de/cores/";
	public static String coreName = "core"+coreLevel+".json";
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

	public static InputStream readCoreFileFromNetAndCache(String cacheDir) throws IOException {
		HttpURLConnection con = null;
		try {
			URL url = new URL(coreURL + coreName);
			con = (HttpURLConnection) url.openConnection();
			Log.d("TP", "Trying to download core to "+context.getExternalCacheDir().getAbsolutePath()+coreName);
			FileOutputStream coreFileStream = new FileOutputStream(new File(context.getExternalCacheDir(), coreName));
			Log.d("TP", "Content-Length: "+con.getContentLength());
			InputStream in = new BufferedInputStream(con.getInputStream());
			TeeInputStream teeStream = new TeeInputStream(con.getInputStream(), coreFileStream, true);
			return teeStream;

		} catch (MalformedURLException e) {
			e.printStackTrace();
			if (con != null ){
				con.disconnect();
			}
			return null;
		}
	}

	public static long getLastModifiedOnServer() throws IOException {
		HttpURLConnection con = null;
		long result = new Date().getTime();
		try {
			URL url = new URL(coreURL + coreName);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("HEAD");
			result = con.getHeaderFieldDate("Last-Modified", result);
			Log.d("TP", "Last modified is "+new Date(result));

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return result;
		} finally {
			if (con != null ){
				con.disconnect();
			}
		}
		return result;
	}

	public static InputStream readCoreFileCached(File coreFile, String cacheDir) throws IOException{
		if (getLastModifiedOnServer() > coreFile.lastModified()-1000*60*10){
			return readCoreFileFromNetAndCache(cacheDir);
		}
		return  new FileInputStream(coreFile);
	}

	public static synchronized SimpleGraph getCoreGraph() throws IOException {
		if (core == null){
			String cacheDir = context.getExternalCacheDir().getAbsolutePath();
			File coreFile = new File(context.getExternalCacheDir(), coreName);
			InputStream coreFileStream = null;
			if (!coreFile.exists()){
				Log.d("TP", "Core does not exist");
				coreFileStream = readCoreFileFromNetAndCache(cacheDir);
			} else {
				Log.d("TP", "Core exists");
				coreFileStream = readCoreFileCached(coreFile, cacheDir);
			}
			try {
				core = ClientGraph.readClientGraph(new NullGraph(), JacksonManager.ContentType.JSON, coreFileStream);
			} catch (Exception ex) {
				// We might have messed up our cached Core, delete it
				// so we don't stumble upon it
				if (coreFile.exists()){
					Log.e("TP", "Deleting core because of Exception");
					coreFile.delete();
				}
				core = null;
			}
		} else {
			Log.d("TP", "Core is loaded");
		}

		return core;
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
