package com.wuest.prefab.StructureGen;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.wuest.prefab.Prefab;

/**
 * Each structure represents a building which is pre-defined in a JSON file.
 * @author WuestMan
 */
public class Structure
{
	private String name;
	private BuildClear clearSpace;
	private ArrayList<BuildFloor> floors;
	private ArrayList<BuildWall> walls;
	private ArrayList<BuildBlock> blocks;
	
	public Structure()
	{
		this.Initialize();
	}
	
	/**
	 * Creates an instance of the structure after reading from a resource location and converting it from JSON.
	 * @param resourceLocation The location of the JSON file to load. Example: "assets/prefab/structures/warehouse.json"
	 * @return Null if the resource wasn't found or the JSON could not be parsed, otherwise the de-serialized object.
	 */
	public static Structure CreateInstance(String resourceLocation)
	{
		Structure structure = null;
		
		try
		{
			Gson file = new Gson();
			InputStreamReader reader = new InputStreamReader(Prefab.class.getClassLoader().getResourceAsStream(resourceLocation), "UTF-8");
			
			if (reader != null)
			{
				 structure = file.fromJson(reader, Structure.class);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return structure;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String value)
	{
		this.name = value;
	}
	
	public void Initialize()
	{
		this.name = "";
        this.clearSpace = new BuildClear();
        this.floors = new ArrayList<BuildFloor>();
        this.walls = new ArrayList<BuildWall>();
        this.blocks = new ArrayList<BuildBlock>();
	}
}