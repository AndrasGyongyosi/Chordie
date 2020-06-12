package com.example.ChordCalculator.Model.Chord;

import java.util.ArrayList;
import java.util.List;

public enum BaseType {
	maj("major", new ArrayList<Integer>() {
		{
			add(0);
			add(4);
			add(7);
		}
	}, new ArrayList<String>() {
		{
			add("maj");
			add("");
		}
	}), min("minor", new ArrayList<Integer>() {
		{
			add(0);
			add(3);
			add(7);
		}
	}, new ArrayList<String>() {
		{
			add("min");
			add("m");
		}
	}), dim("diminished", new ArrayList<Integer>() {
		{
			add(0);
			add(3);
			add(6);
		}
	}, new ArrayList<String>() {
		{
			add("dim");
			add("D");
		}
	}), aug("augmented", new ArrayList<Integer>() {
		{
			add(0);
			add(4);
			add(8);
		}
	}, new ArrayList<String>() {
		{
			add("aug");
			add("+");
		}
	});

	private ArrayList<Integer> structure;
	private String name;
	private List<String> aliases;

	private BaseType(String name, ArrayList<Integer> structure, List<String> aliases) {
		this.name = name;
		this.structure = structure;
		this.aliases = aliases;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Integer> getStructure() {
		return structure;
	}

	public List<String> getAliases() {
		return aliases;
	}
}
