package connect.four.ai;

public interface ArtificialIntelligenceListener {
	
	public void decisionTaken(byte rowIndex, byte columnIndex, ArtificialIntelligence artificialIntelligence);
	
}