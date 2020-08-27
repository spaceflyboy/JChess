import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//TODO: https://docs.oracle.com/javase/8/docs/api/javax/swing/Timer.html (down the road)

public class ChessController {
	public boolean CHESS_DEBUG = false;
	private ChessGame gameState;
	private ChessGUI view;
	public static final int SIDES = 2;
	private static int[][] gridCondenser;
	
	public ChessController() {
		generateGridCondenser();
	}
	
	private static void generateGridCondenser() {
		int index = 0;
		gridCondenser = new int[8][8];
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				gridCondenser[i][j] = index++;
			}
		}
	}
	
	
	

	public boolean attemptMove(int oldGridPos, int newGridPos) {
		int[] oldSplit = splitGridPos(oldGridPos);
		ChessPiece toMove = this.gameState.gameboard(oldSplit[1], oldSplit[0]);
		if(toMove == null) return false;
		System.out.printf("ChessController: attempting to move %s %s from %d to %d\n", (toMove.getColor() == 0) ? "white": "black", ChessGame.nameFromType[toMove.getType()], oldGridPos, newGridPos );
		
		ChessPiece target = this.fetchPiece(newGridPos);
		System.out.print("ChessController: attemptMove: target = ");
		System.out.println(target);

		if(!this.gameState.verifyMoveLegality(oldGridPos, newGridPos)) {
			System.out.println("ChessController: attemptMove: Error: Invalid Move");
			this.view.updatePiece(oldGridPos);
			return false;
		} else {	
			System.out.println("ChessController: attemptMove: move verified as legal");
			int[] newSplit = splitGridPos(newGridPos);
			
			if(toMove.getType() == 1 && target == null && oldSplit[0] != newSplit[0]) {// en passant
				System.out.println("ChessController: attemptMove: en passant");
				this.view.removePiece(joinGridPos(newSplit[0], oldSplit[1]));
			} else if(target != null) {
				System.out.println("ChessController: attemptMove: standard capture");
				this.view.removePiece(newGridPos);
				
			}
			
			
			boolean check = this.gameState.movePiece_unchecked(oldGridPos, newGridPos);
			if(!check) return false;
			
			System.out.printf("piece moved from grid pos %d to %d successfully\n", oldGridPos, newGridPos);
			
			boolean whiteMoveNext = this.gameState.whiteToMove();
			boolean safe = false;
			
			
			
			if(whiteMoveNext) { //inside of black's move routine
				safe = this.gameState.isKingSafe(0, this.gameState.getWhiteKingPos());
				if(!safe) this.gameState.induceCheck(0);
				
				if(this.gameState.blackInCheck()) {
					safe = this.gameState.isKingSafe(1, this.gameState.getBlackKingPos());
				}
				if(safe) this.gameState.reverseCheck(1);
				
			} else { //inside of white's move routine
				safe = this.gameState.isKingSafe(1,  this.gameState.getBlackKingPos());
				if(!safe) this.gameState.induceCheck(1);

				if(this.gameState.whiteInCheck()) {
						safe = this.gameState.isKingSafe(0,  this.gameState.getWhiteKingPos());
				}
				if(safe) this.gameState.reverseCheck(0);
			}
			
			int numMovesForOpponent = this.gameState.countLegalMoves((whiteMoveNext) ? 0 : 1, this.gameState.generateLegalMoves((this.gameState.whiteToMove()) ? 0 : 1));
			if(numMovesForOpponent == 0) { //end game! We need to determine if its stale mate or not
				int endType = -1;
				int colorWinner = 0;
				if(this.gameState.whiteInCheck() || this.gameState.blackInCheck()) {
					endType = 0;
					if(this.gameState.whiteInCheck()) colorWinner = 1;
				} else {
					endType = 1;
				}
				this.view.gameOver(endType, colorWinner);
			}
			
			this.view.updatePiece(newGridPos);
			this.gameState._printGameState();
			return true;
		}
	}
	
	public static void main(String[] args) {
		
		ChessController ctrl = new ChessController();
		ChessGame game = new ChessGame(ctrl);
		ctrl.gameState = game;
		ChessGUI view = new ChessGUI(ctrl);
		ctrl.view = view;
		
		JFrame frame = (JFrame) ctrl.view;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setTitle("Java Chess");
		
	}
	
	public ChessPiece fetchPiece(int gridPosition) {
		int[] split = splitGridPos(gridPosition);
		return this.gameState.gameboard(split[1], split[0]);
	}

	
	public static int[] splitGridPos(int gridPos) {
		//System.out.printf("splitGridPos(%d)\n", gridPos);
		int[] result = new int[2];
		result[1] = gridPos/8;
		result[0] = gridPos%8;
		//System.out.printf("\tresult = (%d, %d)\n", result[0],result[1]);
		return result;
	}
	
	public static int joinGridPos(int x, int y) {
		//System.out.printf("joinGridPos(%d, %d) = %d\n", x, y, y*8+x);
		return gridCondenser[y][x];
		
	}
	
	public boolean whiteToMove() { return this.gameState.whiteToMove(); }
}
