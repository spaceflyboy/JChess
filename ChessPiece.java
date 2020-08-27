import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ChessPiece {
	//sprite?
	
	private int value; //for the +2 type beat display thing when u yoink a piece and op no trade
	private int type; // 0- king 1- pawn 2- bishop 3- knight 4- rook 5-queen
	private int pos;
	private int color; //0- white 1- black
	private int lastMove;

	private final int[] valFromType = {0, 1, 3, 3, 5, 9};
	
	public ChessPiece(int type, int color) {
		this.value = valFromType[type];
		this.type = type;
		this.color = color;
		this.pos = 0;
		this.lastMove = -1;
	}
	
	public ChessPiece(int type, int pos, int color) {
		this.value = valFromType[type];
		this.type = type;
		this.pos = pos;
		this.color = color;
		this.lastMove = -1;
	}
	
	public int updatePos(int pos) {
		int temp = this.pos;
		this.pos = pos;
		return temp;
	}
	
	
	public int getColor() {
		return this.color;
	}
	
	public int getType() {
		return this.type;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setLastMove(int turn) {
		this.lastMove = turn;
	}
	
	public int getLastMove() {
		return this.lastMove;
	}
}
