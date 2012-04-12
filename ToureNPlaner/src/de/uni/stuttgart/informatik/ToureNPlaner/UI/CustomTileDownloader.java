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

package de.uni.stuttgart.informatik.ToureNPlaner.UI;

import org.mapsforge.android.maps.mapgenerator.tiledownloader.TileDownloader;
import org.mapsforge.core.Tile;

import java.io.Serializable;
import java.net.URL;

public class CustomTileDownloader extends TileDownloader implements Serializable {
	private final String hostName;
	private final String protocol;
	private final String path;
	private final byte maxZoom;

	public CustomTileDownloader(URL url, byte maxZoom) {
		this.maxZoom = maxZoom;
		this.hostName = url.getHost();
		this.protocol = url.getProtocol();
		this.path = url.getPath();
	}

	@Override
	public String getHostName() {
		return hostName;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public String getTilePath(Tile tile) {
		return String.format(path, tile.zoomLevel, tile.tileX, tile.tileY);
	}

	@Override
	public byte getZoomLevelMax() {
		return maxZoom;
	}
}
