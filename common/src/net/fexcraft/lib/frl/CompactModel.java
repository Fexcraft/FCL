package net.fexcraft.lib.frl;

import java.util.*;

/**
 * All rights reserved. Only to be distributed within authorized mods.
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public class CompactModel {

	public final Map<String, CompactGroup> groups = new LinkedHashMap<>();
	public List<String> authors;
	public int tex_height;
	public int tex_width;
	public String name;

	public void addCreator(String str){
		if(authors == null) authors = new ArrayList<>();
		authors.add(str);
	}

	public static class CompactGroup {

		public ArrayList<Polyhedron> polyhedrons = new ArrayList<>();
		public String name;

		public CompactGroup(){}

		public CompactGroup(String str){
			name = str;
		}

	}

}
