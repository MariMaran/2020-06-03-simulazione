package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Collegamento;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Collegamento> getCollegamento(Map<Integer,Player> idMap){
		String sql ="SELECT a1.MatchID, a1.PlayerID as id1, a2.PlayerID as id2, a1.TeamID, a2.TeamID, SUM(a1.TimePlayed)-SUM(a2.TimePlayed) AS differenza, COUNT(*) AS somma "+ 
				"FROM actions a1, actions a2 "+
				"WHERE a1.MatchID=a2.MatchID && a1.PlayerID>a2.PlayerID && a1.TeamID!=a2.TeamID && a1.`Starts`=1 && a2.`Starts`=1 "+
				"GROUP BY a1.PlayerID, a2.PlayerID "+
				"HAVING differenza!=0";
		List<Collegamento> result = new ArrayList<Collegamento>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player p1=idMap.get(res.getInt("id1"));
				Player p2=idMap.get(res.getInt("id2"));
				if(p1!=null&&p2!=null) {
					Collegamento c=new Collegamento(p1,p2,res.getInt("somma"), res.getInt("differenza"));
					result.add(c);
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Player> getPlayers(double minimo){
		String sql = "SELECT p.PlayerID,p.Name, AVG(a.Goals) AS media "+
				"FROM actions a, players p "+
				"WHERE a.PlayerID=p.PlayerID "+
				"GROUP BY p.PlayerID "+
				"HAVING media>?";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, minimo);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
