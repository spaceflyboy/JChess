import java.util.ArrayList;

class MoveInfoNode {
			private ChessPiece piece;
			private ArrayList<Integer> moves;
			public MoveInfoNode(ChessPiece piece, ArrayList<Integer> moves) {
				this.piece = piece;
				this.moves = moves;
			}
			
			public MoveInfoNode() {
				this.piece = null;
				this.moves = new ArrayList<>();
			}
			
			public ChessPiece getPiece() {
				return this.piece;
			}
			
			public ArrayList<Integer> getMoves() {
				return this.moves;
			}
			
			public void setMoves(ArrayList<Integer> moves) {
				this.moves = moves;
			}
}
