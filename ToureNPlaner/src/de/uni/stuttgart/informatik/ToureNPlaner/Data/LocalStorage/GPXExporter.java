package de.uni.stuttgart.informatik.ToureNPlaner.Data.LocalStorage;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.Result;
import de.uni.stuttgart.informatik.ToureNPlaner.Data.ResultNode;
import de.uni.stuttgart.informatik.ToureNPlaner.ToureNPlanerApplication;

import java.io.*;

// Inspired (aka copypasted) from https://code.google.com/p/mytracks/source/browse/MyTracks/src/com/google/android/apps/mytracks/io/file/GpxTrackWriter.java
public class GPXExporter {

	public static void writeHeader(Result r, PrintWriter printWriter) {
		printWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		printWriter.println("<gpx");
		printWriter.println("version=\"1.1\"");
		printWriter.println(
				"creator=\"" + "ToureNPlaner" + "\"");
		printWriter.println("xmlns=\"http://www.topografix.com/GPX/1/1\"");
		printWriter.println(
				"xmlns:topografix=\"http://www.topografix.com/GPX/Private/TopoGrafix/0/1\"");
		printWriter.println("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		printWriter.println("xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1"
				+ " http://www.topografix.com/GPX/1/1/gpx.xsd"
				+ " http://www.topografix.com/GPX/Private/TopoGrafix/0/1"
				+ " http://www.topografix.com/GPX/Private/TopoGrafix/0/1/topografix.xsd\">");
		printWriter.println("<metadata>");
		//TODO: name etc
		printWriter.println("<name>" + "ToureNPlaner Route" + "</name>");
		printWriter.println("<desc>" + "GPX Route created by ToureNPlaner" + "</desc>");
		printWriter.println("</metadata>");
	}

	public static void writeFooter(PrintWriter printWriter) {
		if (printWriter != null) {
			printWriter.println("</gpx>");
		}
	}

	private static double factor = 1000000;
	public static void writeTrack(Result r, PrintWriter printWriter) {
		if (printWriter != null) {
			printWriter.println("<trk>");
			printWriter.println("<name>" + "Track" + "</name>");
			printWriter.println("<desc>" + "Track created by ToureNPlaner" + "</desc>");
			printWriter.println("<type>" + "" + "</type>");
			//printWriter.println("<extensions><topografix:color>c0c0c0</topografix:color></extensions>");


			for (int[] subway : r.getWay()) {
				printWriter.println("<trkseg>");

				for (int i = 0; i < subway.length; i+=2) {
					printWriter.println("<trkpt lat=\"" + subway[i+1]/factor + "\" lon=\"" + subway[i]/factor + "\"></trkpt>");
				}
				printWriter.println("</trkseg>");
			}
			printWriter.println("</trk>");


			for (ResultNode rn : r.getPoints()) {
				printWriter.println("<wpt lat=\"" + rn.getGeoPoint().getLatitude()  + "\" lon=\""+ rn.getGeoPoint().getLongitude() +"\">");
//				printWriter.println(
//						"<time>" + StringUtils.formatDateTimeIso8601(location.getTime()) + "</time>");
//				printWriter.println("<name>" + StringUtils.formatCData(waypoint.getName()) + "</name>");
//				printWriter.println("<cmt>" + StringUtils.formatCData(waypoint.getType().name()) + "</cmt>");
//				printWriter.println(
//						"<desc>" + StringUtils.formatCData(waypoint.getDescription()) + "</desc>");
//				printWriter.println("<type>" + StringUtils.formatCData(waypoint.getCategory()) + "</type>");
				printWriter.println("</wpt>");
			}
		}
	}

	public static void exportGPX(Result r, String filename) {
		try {
			File root = Environment.getExternalStorageDirectory();
			if (!root.canWrite()){
				throw new IOException("Can't write to " + root.getAbsolutePath());
			}
			String filepath = root + "/" + filename;
			OutputStream out = new FileOutputStream(filepath);
			PrintWriter printWriter = new PrintWriter(out);
			writeHeader(r, printWriter);
			writeTrack(r, printWriter);
			writeFooter(printWriter);
			printWriter.close();
			Toast.makeText(ToureNPlanerApplication.getContext(), "Successfully exported gpx file to " + filepath, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			String error = "Error saving route as GPX\n" + e.getMessage();
			Toast.makeText(ToureNPlanerApplication.getContext(), error, Toast.LENGTH_LONG).show();
			Log.w("TP", error);
		}
	}

}
