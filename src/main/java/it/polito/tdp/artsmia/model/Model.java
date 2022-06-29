package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject,DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer,ArtObject> idMap;

	public Model() {
		//grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); // non conviene creare il grafo alla costruzione del model perche' si rende più articolato modificare
		dao=new ArtsmiaDAO();
		idMap=new HashMap<>();
	}
	// più opportuno creare un metodo per la creazione del grafo, da richiamare all'occorrenza
	public void creaGrafo() { // si possono aggiungere parametri per filtrare i vertici
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		// aggiunta vertici
		// 1. recupero tutti gli ArtObject dal DB
		// 2. li inserisco come vertici (quindi ho bisogno di un DAO nel model)
		//List<ArtObject> vertici = dao.listObjects();
		//Graphs.addAllVertices(grafo,idMap.values());
		// è utile avere una identityMAp. delego al dao di popolare gli oggetti secondo il formato specificato.
		dao.listObjects(idMap);
		Graphs.addAllVertices(grafo,idMap.values());
		// aggiungere gli archi
		// approccio 1
		// doppio ciclo for sui vertici, dati due vertici controllo se devono essere collegati
		/*for(ArtObject a1:this.grafo.vertexSet()) {
			for(ArtObject a2:this.grafo.vertexSet()) {
				if(!a1.equals(a2)&&!this.grafo.containsEdge(a1,a2)) {
					// devo collegare a1 ad a2?
					int peso=dao.getPeso(a1,a2);
					if(peso>0) {
						Graphs.addEdge(this.grafo,a1,a2,peso);
					}
				}
			}
		}*/
		// non funziona, non giugne al termine perchè ci sono troppi vertici
		// approccio 3
		for(Adiacenza a:dao.getAdiacenze()) {
			Graphs.addEdge(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
		}
		System.out.println("GRAFO CREATO");
		System.out.println("# VERTICI "+grafo.vertexSet().size());
		System.out.println("# ARCHI "+grafo.edgeSet().size());
	}
}
