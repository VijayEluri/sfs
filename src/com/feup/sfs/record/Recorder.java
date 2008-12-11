package com.feup.sfs.record;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;

import com.feup.sfs.facility.Facility;
import com.feup.sfs.factory.Factory;

public class Recorder {
	private boolean initialized = false;
	private Vector<Integer> inputs = new Vector<Integer>();;
	private Vector<Integer> outputs = new Vector<Integer>();;
	private Vector<Integer> registers = new Vector<Integer>();;
	private PrintStream ps = null;
	
	public Recorder(String file) throws FileNotFoundException {
		System.out.println("Recording to: " + file);
		ps = new PrintStream(new FileOutputStream(file));
	}
	
	private void init() {
		ArrayList<Facility> facilities = Factory.getInstance().getFacilities();
		for (Facility facility : facilities) {
			for (int d = 0; d < facility.getNumberDigitalIns(); d++)
				inputs.add(new Integer(facility.getDigitalIn(d)?1:0));

			for (int d = 0; d < facility.getNumberDigitalOuts(); d++)
				outputs.add(new Integer(facility.getDigitalOut(d)?1:0));

			for (int d = 0; d < facility.getNumberRegisters(); d++)
				registers.add(new Integer(facility.getRegister(d)));
		}
		initialized = true;
	}
	
	private void printDeltas() {
		ArrayList<Facility> facilities = Factory.getInstance().getFacilities();
		int i = 0, o = 0, r = 0;
		for (Facility facility : facilities) {
			for (int d = 0; d < facility.getNumberDigitalIns(); d++) {
				int v = facility.getDigitalIn(d)?1:0;
				if (inputs.elementAt(i).intValue()!=v) {
					ps.println("IN " + i + " " + v);
					inputs.setElementAt(new Integer(v), i);
				}
				i++;
			}
			for (int d = 0; d < facility.getNumberDigitalOuts(); d++) {
				int v = facility.getDigitalOut(d)?1:0;
				if (outputs.elementAt(o).intValue()!=v) {
					ps.println("OUT " + o + " " + v);
					outputs.setElementAt(new Integer(v), o);
				}
				o++;
			}
			for (int d = 0; d < facility.getNumberRegisters(); d++) {
				int v = facility.getRegister(d);
				if (registers.elementAt(r).intValue()!=v) {
					ps.println("REG " + r + " " + v);
					registers.setElementAt(new Integer(v), r);
				}
				r++;
			}

		}		
	}
	
	public void record() {
		if (!initialized) init();
		else printDeltas();
	}
}