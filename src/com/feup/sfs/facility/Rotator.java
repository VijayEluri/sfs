package com.feup.sfs.facility;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Properties;

import net.wimpi.modbus.procimg.SimpleDigitalIn;
import net.wimpi.modbus.procimg.SimpleDigitalOut;

import com.feup.sfs.block.Block;
import com.feup.sfs.exceptions.FactoryInitializationException;
import com.feup.sfs.modbus.ModbusSlave;

public class Rotator extends Conveyor {
	protected double currentRotation = 0;
	protected boolean rotated = false;
	
	public Rotator(Properties properties, int id) throws FactoryInitializationException {
		super(properties, id);
		
		ModbusSlave.getSimpleProcessImage().addDigitalOut(new SimpleDigitalOut(false));//R-
		ModbusSlave.getSimpleProcessImage().addDigitalOut(new SimpleDigitalOut(false));//R+
		ModbusSlave.getSimpleProcessImage().addDigitalIn(new SimpleDigitalIn(false)); // R- Sensor	
		ModbusSlave.getSimpleProcessImage().addDigitalIn(new SimpleDigitalIn(false)); // R+ Sensor	
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		if (currentRotation != 0 && currentRotation != 90) {
			g.setColor(Color.darkGray);
			Rectangle r = getBounds();
			int x1, y1, x2, y2;
			double cosine = Math.cos((double)currentRotation*Math.PI/180);
			double sine = Math.sin((double)currentRotation*Math.PI/180);
			int length = Math.max(r.width, r.height);
			if (orientation == Direction.HORIZONTAL) {
				x1 = (int) (r.getCenterX() - length / 2 * cosine) ; x2 = (int) (r.getCenterX() + length /  2* cosine);
				y1 = (int) (r.getCenterY() - length / 2 * sine); y2 = (int) (r.getCenterY() + length / 2 * sine);
			} else {
				x1 = (int) (r.getCenterX() - length / 2 * sine) ; x2 = (int) (r.getCenterX() + length /  2* sine);
				y1 = (int) (r.getCenterY() - length / 2 * cosine); y2 = (int) (r.getCenterY() + length / 2 * cosine);
				
			}
			g.drawLine(x1, y1, x2, y2);
		}
		
		paintLight(g, false, 0, getDigitalOut(2), 1);
		paintLight(g, true, 1, getDigitalIn(1), 1);
		paintLight(g, true, 2, getDigitalIn(2), 1);
		paintLight(g, false, 3, getDigitalOut(3), 1);
	}
	
	@Override
	public void doStep(boolean conveyorBlocked){
		if (facilityError) return;
		boolean forcing = false;
		super.doStep(currentRotation != 0 && currentRotation !=90);
		if (isRotatingAntiClockwise() && !isRotatingClockwise()) {
			currentRotation += getFactory().getRotationSpeed()*getFactory().getSimulationTime()/1000;
			if (currentRotation >= 90) {currentRotation = 90; setRotated(true); forcing = true;}
		}
		if (!isRotatingAntiClockwise() && isRotatingClockwise()) {
			currentRotation -= getFactory().getRotationSpeed()*getFactory().getSimulationTime()/1000;
			if (currentRotation <= 0) {currentRotation = 0; setRotated(false); forcing = true;}
		}
		if (currentRotation == 0) setDigitalIn(1, true); else setDigitalIn(1, false);
		if (currentRotation == 90) setDigitalIn(2, true); else setDigitalIn(2, false);
		isForcing(forcing);
	}

	private void setRotated(boolean rotated) {
		if (rotated != this.rotated) {
			ArrayList<Block> blocks = getFactory().getBlocks();
			for (Block block : blocks) {
				if (getBounds().contains(block.getBounds().getCenterX(), block.getBounds().getCenterY())){
					double distX = getCenterX() - block.getCenterX();
					double distY = getCenterY() - block.getCenterY();
					block.setCenterX(getCenterX() - distY);
					block.setCenterY(getCenterY() - distX);
				}
			}
		}
		this.rotated = rotated;
	}

	private boolean isRotatingAntiClockwise() {
		return getDigitalOut(3);
	}

	private boolean isRotatingClockwise() {
		return getDigitalOut(2);
	}
	
	@Override 
	public Direction getOrientation(){
		if (!rotated) return orientation;
		if (orientation == Direction.VERTICAL) return Direction.HORIZONTAL;
		else return Direction.VERTICAL;
	}
	
	@Override
	public String getName() {
		return "Rotator";
	}

	@Override
	public int getNumberDigitalIns() {return 3;}

	@Override
	public int getNumberDigitalOuts() {return 4;}

	
}