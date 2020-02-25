package com.wuest.prefab;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

/**
 * @author WuestMan
 */
@SuppressWarnings({"UnstableApiUsage", "unused"})
public class ZipUtil {
	/**
	 * Compresses a string and converts to a byte array for writing.
	 *
	 * @param originalString The string to compress.
	 * @return A byte array which has ben compressed using GZip.
	 */
	private static byte[] compressString(String originalString) {
		if (originalString == null || originalString.length() == 0) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(originalString.getBytes());
			gzip.close();

			byte[] compressed = out.toByteArray();
			out.close();
			return compressed;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * De-compresses a GZip compressed byte array. Expects UTF-8 encoding.
	 *
	 * @param compressedString The byte array to de-compress.
	 * @return A string of the de-compressed data.
	 */
	private static String decompressString(byte[] compressedString) {
		ByteArrayInputStream bis = new ByteArrayInputStream(compressedString);
		GZIPInputStream gis;
		StringBuilder sb = new StringBuilder();

		try {
			gis = new GZIPInputStream(bis);
			BufferedReader br = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));

			String line;

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			br.close();
			gis.close();
			bis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * De-compresses a GZip compressed byte array. Expects UTF-8 encoding.
	 *
	 * @param compressedBytes The byte array to de-compress.
	 * @return A byte array of the de-compressed data.
	 */
	private static byte[] decompressBytes(byte[] compressedBytes) {
		try {
			Inflater decompressor = new Inflater();
			decompressor.setInput(compressedBytes);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedBytes.length);
			byte[] buf = new byte[1024];

			while (!decompressor.finished()) {
				int count = decompressor.inflate(buf);
				bos.write(buf, 0, count);
			}

			bos.close();
			return bos.toByteArray();
		} catch (IOException | DataFormatException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Compresses the resource file to a local computer location.
	 *
	 * @param resourceLocation The resource location to get data from.
	 * @param fileLocation     The file location to save the compressed data too.
	 */
	public static void zipResourceToFile(String resourceLocation, String fileLocation) {
		InputStream stream = Prefab.class.getClassLoader().getResourceAsStream(resourceLocation);
		String temp;

		try {
			assert stream != null;
			temp = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8.name()));
			stream.close();
			ZipUtil.zipStringToFile(temp, fileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Compresses a string to a file location.
	 *
	 * @param value        The string to compress.
	 * @param fileLocation The location of the file to write the compressed data too.
	 */
	public static void zipStringToFile(String value, String fileLocation) {
		try {
			byte[] compressed = ZipUtil.compressString(value);
			Files.write(compressed, new File(fileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * De-compresses a resource location into a string.
	 *
	 * @param resourceLocation The resource location to de-compress.
	 * @return The de-compressed string.
	 */
	public static String decompressResource(String resourceLocation) {
		InputStream stream = Prefab.class.getClassLoader().getResourceAsStream(resourceLocation);
		byte[] buf;
		String returnValue = "";

		try {
			assert stream != null;
			buf = ByteStreams.toByteArray(stream);
			returnValue = ZipUtil.decompressString(buf);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return returnValue;
	}

	/**
	 * De-compresses a resource location to a buffered image.
	 *
	 * @param resourceLocation The resource location of the image.
	 * @return A buffered image for the resource location.
	 */
	public static BufferedImage decompressImageResource(String resourceLocation) {
		InputStream stream = Prefab.class.getClassLoader().getResourceAsStream(resourceLocation);
		byte[] buf;
		BufferedImage returnValue = null;

		try {
			assert stream != null;
			buf = ByteStreams.toByteArray(stream);
			buf = ZipUtil.decompressBytes(buf);
			stream.close();

			// The file has been decompressed, convert those bytes to a BufferedImage.
			returnValue = ImageIO.read(new ByteArrayInputStream(buf));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return returnValue;
	}
}
