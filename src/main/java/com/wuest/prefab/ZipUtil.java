package com.wuest.prefab;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.wuest.prefab.StructureGen.CustomStructures.StructureAlternateStart;

public class ZipUtil
{
	public static byte[] compressString(String originalString)
	{
		if (originalString == null || originalString.length() == 0)
		{
			return null;
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
		try
		{
			gzip = new GZIPOutputStream(out);
	        gzip.write(originalString.getBytes());
	        gzip.close();
	        
	        byte[] compressed = out.toByteArray();
	        out.close();
	        return compressed;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String decompressString(byte[] compressedString)
	{
		ByteArrayInputStream bis = new ByteArrayInputStream(compressedString);
		GZIPInputStream gis;
		StringBuilder sb = new StringBuilder();
		
		try
		{
			gis = new GZIPInputStream(bis);
			BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
			
			String line;
			
			while((line = br.readLine()) != null) 
			{
				sb.append(line);
			}
			
			br.close();
			gis.close();
			bis.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}
	
	public static void zipResourceToFile(String resourceLocation, String fileLocation)
	{
		InputStream stream = Prefab.class.getClassLoader().getResourceAsStream(resourceLocation);
		String temp;
		
		try
		{
			temp = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8.name()));
			stream.close();
			ZipUtil.zipStringToFile(temp, fileLocation);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void zipStringToFile(String value, String fileLocation)
	{
		try
		{
			byte[] compressed = ZipUtil.compressString(value);
			Files.write(compressed, new File(fileLocation));
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String decompressResource(String resourceLocation)
	{
		InputStream stream = Prefab.class.getClassLoader().getResourceAsStream(resourceLocation);
		byte[] buf;
		String returnValue = "";
		
		try
		{
			buf = ByteStreams.toByteArray(stream);
			returnValue = ZipUtil.decompressString(buf);
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return returnValue;
	}
}
