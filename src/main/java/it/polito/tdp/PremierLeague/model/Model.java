package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	Graph<Player,DefaultWeightedEdge> grafo;
	PremierLeagueDAO dao;
	Map<Integer,Player> idMap;
	List<Player> dreamTeam;
	List<Player> tutti;
	double titolaritaMassima;
	public Model() {
		dao=new PremierLeagueDAO();
	}
	
	public List<Player> getDreamTeam(){
		List<Player> vuota=new ArrayList();
		dreamTeam=new ArrayList();
		titolaritaMassima=0;
		ricorsione(tutti,0,vuota,0);
		return dreamTeam;
	}
	
	public void ricorsione(List<Player> daEsaminare, double titolaritaAttuale, List<Player> soluzione, int livello) {
		if(livello==3) {
			if(titolaritaAttuale>titolaritaMassima) {
				titolaritaMassima=titolaritaAttuale;
				dreamTeam=new ArrayList(soluzione);
			}
			return;
		}
		
		for(Player p: daEsaminare) {
			Set<DefaultWeightedEdge> archiUscenti=grafo.outgoingEdgesOf(p);
			Set<DefaultWeightedEdge> archiEntranti=grafo.incomingEdgesOf(p);
			List<Player> daEliminare=new ArrayList();
			double somma=0;
			for(DefaultWeightedEdge d: archiUscenti) {
				somma+=grafo.getEdgeWeight(d);
				daEliminare.add(grafo.getEdgeTarget(d));
			}
			for(DefaultWeightedEdge d: archiEntranti) {
				somma-=grafo.getEdgeWeight(d);
			}
			List<Player> nuova=new ArrayList(daEsaminare);
			for(Player p2: daEliminare) {
				nuova.remove(p2);
			}
			soluzione.add(p);
			nuova.remove(p);
			ricorsione(nuova,titolaritaAttuale+somma,soluzione,livello+1);
			soluzione.remove(p);
			
		}
		
	}
	
	public void creaGrafo(double minimoGoals) {
		idMap=new TreeMap();
		tutti=new ArrayList();
		grafo=new SimpleDirectedWeightedGraph(DefaultWeightedEdge.class);
		List<Player> vertici=dao.getPlayers(minimoGoals);
		for(Player p: vertici) {
			grafo.addVertex(p);
		idMap.put(p.getPlayerID(),p);
		tutti.add(p);
		}
		List<Collegamento> coll=dao.getCollegamento(idMap);
		for(Collegamento c: coll) {
			if(c.differenza>0) {
				grafo.addEdge(c.p1, c.p2);
				grafo.setEdgeWeight(c.p1, c.p2, c.differenza);
				}
			else {
			   grafo.addEdge(c.p2, c.p1);
			grafo.setEdgeWeight(c.p2, c.p1, c.differenza*-1);
			}
		}
	}
	
}
