/*
 * This file is part of ShopFloorSimulator.
 * 
 * ShopFloorSimulator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ShopFloorSimulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with ShopFloorSimulator.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.feup.sfs.warehouse;

import com.feup.sfs.facility.WarehouseIn;

public class OrderIn extends Order {
	private WarehouseIn in;

	public OrderIn(int time, WarehouseIn in) {
		super(time);
		this.setIn(in);
	}

	public void setIn(WarehouseIn in) {
		this.in = in;
	}

	public WarehouseIn getIn() {
		return in;
	}
}
