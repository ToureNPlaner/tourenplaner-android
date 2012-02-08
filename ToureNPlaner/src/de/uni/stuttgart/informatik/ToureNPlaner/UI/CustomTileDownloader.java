package de.uni.stuttgart.informatik.ToureNPlaner.UI;

import org.mapsforge.android.maps.mapgenerator.tiledownloader.TileDownloader;
import org.mapsforge.core.Tile;

import java.net.URL;

public class CustomTileDownloader extends TileDownloader {
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
