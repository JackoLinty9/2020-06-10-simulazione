package it.polito.tdp.imdb.model;

public class Arco {
	
	Actor a1;
	Actor a2;
	Integer peso;
	
	public Arco(Actor a1, Actor a2, Integer peso) {
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}

	public Actor getA1() {
		return a1;
	}

	public void setA1(Actor a1) {
		this.a1 = a1;
	}

	public Actor getA2() {
		return a2;
	}

	public void setA2(Actor a2) {
		this.a2 = a2;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

}
