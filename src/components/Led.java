package components;

public class Led {
	private String name;
	private int cell, row, column, status;
	
	public Led(String name, int cell, int row, int column, int status) {
		this.name = name;
		this.cell = cell;
		this.row = row;
		this.column = column;
		this.status = status;
	}
	
	public String getName() {
		return this.name;
	}
	public int getCell() {
		return this.cell;
	}
	public int getStatus() {
		return this.status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getRow() {
		return this.row;
	}
	public int getColumn() {
		return this.column;
	}
}
