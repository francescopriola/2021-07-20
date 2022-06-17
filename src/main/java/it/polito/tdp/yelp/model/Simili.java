package it.polito.tdp.yelp.model;

public class Simili {
	
	private User u1;
	private User u2;
	private int gradoDiSimilarita;
	
	public Simili(User u1, User u2, int gradoDiSimilarita) {
		super();
		this.u1 = u1;
		this.u2 = u2;
		this.gradoDiSimilarita = gradoDiSimilarita;
	}

	public User getU1() {
		return u1;
	}

	public void setU1(User u1) {
		this.u1 = u1;
	}

	public User getU2() {
		return u2;
	}

	public void setU2(User u2) {
		this.u2 = u2;
	}

	public int getGradoDiSimilarita() {
		return gradoDiSimilarita;
	}

	public void setGradoDiSimilarita(int gradoDiSimilarita) {
		this.gradoDiSimilarita = gradoDiSimilarita;
	}
	
	
	
	

}
