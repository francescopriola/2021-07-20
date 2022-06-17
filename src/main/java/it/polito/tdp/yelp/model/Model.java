package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private Graph<User, DefaultWeightedEdge> graph;
	private YelpDao dao;
	private List<User> utenti;
	
	public Model() {
		dao= new YelpDao();
	}
	
	public String creaGrafo(int nRecensioni, int anno) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
//		Aggiungo i vertici
		utenti = dao.getVertici(nRecensioni);
		Graphs.addAllVertices(this.graph, utenti);
		
//		Aggiungo gli archi
		for(User u1 : utenti) {
			for(User u2 : utenti) {
				if(!u1.equals(u2) && u1.getUserId().compareTo(u2.getUserId()) < 0) {
					int sim = dao.calcolaSimilarita(u1, u2, anno);
					if(sim > 0)
						Graphs.addEdge(this.graph, u1, u2, sim);
				}
			}
		}
		
//		TODO riempire la tendina degli archi
//		System.out.println("Grafo creato!");
//		System.out.println("#Vertici: " + this.graph.vertexSet().size());
//		System.out.println("#Archi: " + this.graph.edgeSet().size());
		String s = "Grafo creato!\n" + "#Vertici: " + this.graph.vertexSet().size() + "\n" + "#Archi: " + this.graph.edgeSet().size() + "\n";
		return s;
		
	}
	
	public List<User> utentiPiuSimili(User u){
		int max = 0;
		for(DefaultWeightedEdge e : this.graph.edgesOf(u)) {
			if(this.graph.getEdgeWeight(e) > max) 
				max = (int)this.graph.getEdgeWeight(e);
		}
		
		List<User> result = new ArrayList<User>();	
		for(DefaultWeightedEdge e : this.graph.edgesOf(u)) {
			if((int)this.graph.getEdgeWeight(e) == max) {
				User u2 = Graphs.getOppositeVertex(this.graph, e, u);
				result.add(u2);
			}
		}
		return result;
	}
	
	public Integer calcolaSimilarita(User u1, User u2, int anno){
		return dao.calcolaSimilarita(u1, u2, anno);
	}

	public List<User> getUtente() {
		return this.utenti;
	}
	
}
