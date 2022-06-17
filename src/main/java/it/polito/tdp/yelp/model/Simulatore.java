package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.mariadb.jdbc.internal.util.dao.Identifier;

import it.polito.tdp.yelp.model.Event.EventType;

public class Simulatore {
	
//	Dati in ingresso
	private int x1;
	private int x2;
	
//	Dati in uscita
//	I giornalisti sono rappresentati da un numero compreso tra 0 e x1-1
	private List<Giornalista> giornalisti;
	private int numeroGiorni;
	
//	Modello del mondo
	private Set<User> intervistati;
	private Graph<User, DefaultWeightedEdge> graph;
	
//	Coda degli eventi
	private PriorityQueue<Event> queue;
	
	public Simulatore(Graph<User, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}
	
	public  void init(int x1, int x2) {
		this.x1 = x1;
		this.x2 = x2;
		this.intervistati = new HashSet<>();
		this.numeroGiorni = 0;
		this.giornalisti = new ArrayList<>();
		
		for(int id = 0; id < this.x1; id++)
			this.giornalisti.add(new Giornalista(id));
		
//		pre-carico la coda
//		per ogni giornalista carico l'intervista del giorno successivo
		for(Giornalista giornalista : giornalisti) {
			User intervistato = selezionaIntervistato(this.graph.vertexSet());
			
			this.intervistati.add(intervistato);
			giornalista.incrementaIntervistati();
			
			this.queue.add(new Event(1, EventType.DA_INTERVISTARE, intervistato, giornalista));
		}
	}
	
	public void run() {
		
		while(!this.queue.isEmpty() && this.intervistati.size() < x2) {
			Event event = this.queue.poll();
			this.numeroGiorni = event.getGiorno();
			
			processEvent(event);
		}
	}

	private void processEvent(Event e) {
		switch (e.getType()) {
		case DA_INTERVISTARE:
			double caso = Math.random();
			
			if(caso < 0.6) {
//				caso 1
				User vicino = selezionaAdiacente(e.getIntervistato());
				if(vicino == null) {
					vicino = selezionaIntervistato(this.graph.vertexSet());
				}
				
				this.queue.add(new Event(e.getGiorno()+1, EventType.DA_INTERVISTARE, vicino, e.getGiornalista()));
				this.intervistati.add(vicino);
				e.getGiornalista().incrementaIntervistati();
				
			} else if(caso < 0.8) {
//				caso 2: sceglierò domani 
				this.queue.add(new Event(e.getGiorno()+1, EventType.FERIE, e.getIntervistato(), e.getGiornalista()));
			} else {
//				caso 3: domani continuo con lo stesso
				this.queue.add(new Event(e.getGiorno()+1, EventType.DA_INTERVISTARE, e.getIntervistato(), e.getGiornalista()));
			}
			break;

		case FERIE:
			break;
		}
		
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public List<Giornalista> getGiornalisti() {
		return giornalisti;
	}

	public int getNumeroGiorni() {
		return numeroGiorni;
	}
	
	/**
	 * Seleziona un intervistato dalla lista specificata, evitando di selezionare coloro che sono già in this.intervistati
	 * @param lista
	 * @return
	 */
	private User selezionaIntervistato(Collection<User> lista) {
		List<User> candidati = new LinkedList<>(lista);
		candidati.removeAll(this.intervistati);
		
		int scelto = (int)(Math.random()*candidati.size());
		return candidati.get(scelto);
	}
	
	private User selezionaAdiacente(User u) {
		List<User> vicini = Graphs.neighborListOf(this.graph, u);
		vicini.removeAll(this.intervistati);
		
		if(vicini.size() == 0)
			return null;
		
		double max = 0;
		for(User v : vicini) {
			double peso = this.graph.getEdgeWeight(this.graph.getEdge(u, v));
			
			if(peso > max)
				max = peso;
		}
		
		List<User> migliori = new ArrayList<>();
		for(User v : vicini) {
			double peso = this.graph.getEdgeWeight(this.graph.getEdge(u, v));
			if(peso == max)
				migliori.add(v);
		}
		
		int scelto = (int)Math.random()*migliori.size();
		return migliori.get(scelto);
	}
	
	
	
	
}
