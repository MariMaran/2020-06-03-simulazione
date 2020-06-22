package it.polito.tdp.PremierLeague.model;

import java.util.List;
import java.util.Set;

public class TestModel {

	public static void main(String[] args) {
		Model m=new Model();
		m.creaGrafo(0.5);
		System.out.println(m.grafo.vertexSet().size());
		System.out.println(m.grafo.edgeSet().size());
		
		
		System.out.println(m.grafo.outDegreeOf(m.idMap.get(12297)));
		List<Player> soluzione=m.getDreamTeam();
		System.out.println(soluzione);
		System.out.println(m.titolaritaMassima);

	}

}
