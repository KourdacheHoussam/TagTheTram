package com.stage.models;



public class Station {
	/**
	 * on suppose qu'une station pour l'instant est caractérisée par ces 3 attributs.
	 */
	private double id;
	private String nomstation;
	private String nomville;
	
	public Station(){}
	
	public Station(double id, String nomstation, String nomville){
		this.id=id;
		this.nomville=nomville;
		this.nomstation=nomstation;		
	}


	public CharSequence getId() {
		return new Double(id).toString();
	}


	public void setId(double id) {
		this.id = id;
	}


	public String getNomstation() {
		return nomstation;
	}


	public void setNomstation(String nomstation) {
		this.nomstation = nomstation;
	}


	public String getNomville() {
		return nomville;
	}


	public void setNomville(String nomville) {
		this.nomville = nomville;
	}
	
	
	
}
