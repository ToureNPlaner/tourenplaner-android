/*
 * Copyright 2013 ToureNPlaner
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

package de.uni.stuttgart.informatik.ToureNPlaner.Net.Handler;

import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute.ClientGraph;
import de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute.NullGraph;
import de.uni.stuttgart.informatik.ToureNPlaner.ClientSideCompute.SimpleGraph;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Request;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.JacksonManager;
import de.uni.stuttgart.informatik.ToureNPlaner.Net.Session;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.TeeInputStream;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * @author Niklas Schnelle
 */
public class SyncCoreLoader {

	public static final int coreLevel = 50;
	private static final String corePrefix = "core";
	private static final String coreSuffix = ".json";
	private ClientGraph core = null;
	private static final String TAG = "ToureNPlaner";
	private final Session session;
	
	public SyncCoreLoader(Session session) {
		this.session = session;
	}
	protected void writeRequest(OutputStream outputStream) throws Exception {
		ObjectMapper mapper = JacksonManager.getJsonMapper();
		JsonNodeFactory factory = mapper.getNodeFactory();
		ObjectNode root = new ObjectNode(factory);
		ObjectNode constraintsNode = new ObjectNode(factory);
		constraintsNode.put("coreLevel", coreLevel);
		root.put("constraints", constraintsNode);
		JsonGenerator generator = mapper.getJsonFactory()
				.createJsonGenerator(outputStream);
		mapper.writeTree(generator, root);
		generator.close();
	}

	private InputStream readCoreFileFromNetAndCache() throws IOException {
		HttpURLConnection con = null;
		try {
			con = session.openPostConnection("/algcore");
			con.setDoOutput(true);
			writeRequest(con.getOutputStream());
			File cacheDirFile = ToureNPlanerApplication.getContext().getExternalCacheDir();
			Log.d(TAG, "Trying to download core to " + cacheDirFile.getAbsolutePath() + "/"+ corePrefix + coreLevel + coreSuffix);
			FileOutputStream coreFileStream = new FileOutputStream(new File(cacheDirFile, corePrefix + coreLevel + coreSuffix));
			Log.d(TAG, "Content-Length: " + con.getContentLength());
			InputStream in = new BufferedInputStream(con.getInputStream());
			TeeInputStream teeStream = new TeeInputStream(in, coreFileStream, true);
			return teeStream;

		} catch (MalformedURLException e) {
			e.printStackTrace();
			if (con != null) {
				con.disconnect();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			if (con != null) {
				con.disconnect();
			}
			return null;
		}
	}

	private InputStream readCoreFileCached(File coreFile) throws IOException {
		if (new Date().getTime() > coreFile.lastModified() + 1000 * 60 * 100) {
			return readCoreFileFromNetAndCache();
		}
		return new FileInputStream(coreFile);
	}

	public synchronized SimpleGraph getCoreGraph() throws IOException {
		if (core == null) {
			File cacheDirFile = ToureNPlanerApplication.getContext().getExternalCacheDir();
			File coreFile = new File(cacheDirFile, corePrefix + coreLevel + coreSuffix);
			InputStream coreFileStream = null;
			if (!coreFile.exists()) {
				Log.d(TAG, "Core does not exist");
				coreFileStream = readCoreFileFromNetAndCache();
			} else {
				Log.d(TAG, "Core exists");
				coreFileStream = readCoreFileCached(coreFile);
			}
			try {
				core = ClientGraph.readClientGraph(new NullGraph(), JacksonManager.ContentType.SMILE, coreFileStream);
				Log.d(TAG, "Nodes: "+core.getNodeCount());
			} catch (Exception ex) {
				ex.printStackTrace();
				// We might have messed up our cached Core, delete it
				// so we don't stumble upon it
				if (coreFile.exists()) {
					Log.e(TAG, "Deleting core because of Exception");
					//coreFile.delete();
				}
				core = null;
			}
		} else {
			Log.d(TAG, "Core is loaded");
		}

		return core;
	}
}
