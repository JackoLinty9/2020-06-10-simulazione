package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer,Actor>idMap;
	private Map<Actor,Actor>predecessore;
	
	public Model() {
		this.dao=new ImdbDAO();
		this.idMap=new HashMap<Integer,Actor>();
	}
	
	public List<String>getAllGenres(){
		return dao.listAllGenres();
	}
	
	public void creaGrafo(String genere) {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//vertici
		Graphs.addAllVertices(this.grafo, dao.listAllVerteces(genere));
		for(Actor a: grafo.vertexSet()) {
			idMap.put(a.getId(), a);
		}
		//archi
		for(Arco a:dao.getArchi(genere, idMap)) {
			if(this.grafo.containsVertex(a.getA1())&& this.grafo.containsVertex(a.getA2()))
				Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(),a.getPeso());
		}
	}
	
	public Integer getNVertici() {
		return grafo.vertexSet().size();
	}
	
	public Integer getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public Collection<Actor>listaAttoriVertici(){
		return grafo.vertexSet();
	}
	
	public String getRaggiungibili(Actor a) {
		BreadthFirstIterator<Actor,DefaultWeightedEdge> bfv=new BreadthFirstIterator<>(this.grafo, a);
		this.predecessore= new HashMap<Actor,Actor>();
		
		bfv.addTraversalListener(new TraversalListener<Actor, DefaultWeightedEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> e) {
				DefaultWeightedEdge arco=e.getEdge();
				Actor a=grafo.getEdgeSource(arco);
				Actor b=grafo.getEdgeTarget(arco);
				if(predecessore.containsKey(b)&&!predecessore.containsKey(a))
					predecessore.put(a, b);
				else if(predecessore.containsKey(a)&&!predecessore.containsKey(b))
					predecessore.put(b, a);
				
			}
			
			@Override
			public void vertexTraversed(VertexTraversalEvent<Actor> e) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Actor> e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		List<Actor>actorSimili=new ArrayList<>();
		
		//visito il grafo
		while(bfv.hasNext()) {
			Actor ac=bfv.next();
			actorSimili.add(ac);
		}
		Collections.sort(actorSimili);
		String risultato="";
		if(actorSimili.isEmpty())
			return risultato;
		
		for(Actor act:actorSimili)
			risultato+=act+"\n";
		
		return risultato;
	}
}
