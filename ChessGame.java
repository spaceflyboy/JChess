import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ChessGame {
		private ChessController ctrl;
		private ChessPiece[][] gameboard;
		private int turnNumber;
		private boolean whiteToMove;
		private int scr_w;
		private int scr_b;
		private boolean whiteInCheck;
		private boolean blackInCheck;
		private int whiteKingPos;
		private int blackKingPos;
		public static final char[] charFromType = { 'K', 'p', 'B', 'N', 'R', 'Q' };
		public static final int[] pawnStarts = {48, 8};
		public static final String[] nameFromType = {"king", "pawn", "bishop", "knight", "rook", "queen"};
		
		public ChessGame() {
			
		}
		
		public ChessGame(ChessController ctrl) {
			this.gameboard = initializePieces();
			this.turnNumber = 0;
			this.whiteToMove = true;
			this.whiteKingPos = 60;
			this.blackKingPos = 4;
			this.ctrl = ctrl;
		}
	
		
		private ArrayList<Integer> generateMovesForPieceAt(int gridPos) {
			int[] split = ChessController.splitGridPos(gridPos);
			ChessPiece toMove = this.gameboard[split[1]][split[0]];
			ArrayList<Integer> moves = new ArrayList<>();
			if(toMove == null) return moves;
			int type = toMove.getType();

			switch(type) {
				case 0: //king
					generateKingMoves(gridPos, moves); //
				break;
				case 1: //pawn
					generatePawnMoves(gridPos, moves); //
				break;
				case 2: //bishop
					generateBishopMoves(gridPos, moves);
				break;
				case 3: //knight
					generateKnightMoves(gridPos, moves); //
				break;
				case 4: //rook
					generateRookMoves(gridPos, moves);
				break;
				case 5: //queen
					generateQueenMoves(gridPos, moves);
				break;
				
			}
			return moves;
		}
		
		private void generatePawnMoves(int pos, ArrayList<Integer> moves) {
			int[] split = ChessController.splitGridPos(pos);
			ChessPiece toMove = this.gameboard[split[1]][split[0]];
			if(toMove.getColor() == 0) {
				if(split[1] == 6) { //start spot for double potential
					if(split[0]-1 > -1) {
						this.checkPos(pos, ChessController.joinGridPos(split[0]-1, split[1]-1), moves);
					}
					if(split[0]+1 < 8) {
						this.checkPos(pos, ChessController.joinGridPos(split[0]+1, split[1]-1), moves);
					}
					this.checkPos(pos, ChessController.joinGridPos(split[0], split[1]-1), moves);
					this.checkPos(pos, ChessController.joinGridPos(split[0], split[1]-2), moves);
				} else {
					if(split[1] == 3) { //en passant potential
						this.checkPos(pos, ChessController.joinGridPos(split[0], split[1]-1), moves);
					} else {
						if(split[1]-1 > -1) {
							if(split[0]-1 > -1) {
								this.checkPos(pos, ChessController.joinGridPos(split[0]-1, split[1]-1), moves);
							}
							if(split[0]+1 < 8) {
								this.checkPos(pos, ChessController.joinGridPos(split[0]+1, split[1]-1), moves);
							}
							this.checkPos(pos, ChessController.joinGridPos(split[0], split[1]-1), moves);
						}
					}
				}
			} else {
				if(split[1] == 1) { //start spot for double potential
					if(split[0]-1 > -1) {
						this.checkPos(pos, ChessController.joinGridPos(split[0]-1, split[1]+1), moves);
					}
					if(split[0]+1 < 8) {
						this.checkPos(pos, ChessController.joinGridPos(split[0]+1, split[1]+1), moves);
					}
					this.checkPos(pos, ChessController.joinGridPos(split[0], split[1]+1), moves);
					this.checkPos(pos, ChessController.joinGridPos(split[0], split[1]+2), moves);
				} else {
					if(split[1] == 4) { //en passant potential
						this.checkPos(pos, ChessController.joinGridPos(split[0], split[1]+1), moves);
					} else {
						if(split[1]+1 < 8) {
							if(split[0]-1 > -1) {
								this.checkPos(pos, ChessController.joinGridPos(split[0]-1, split[1]+1), moves);
							}
							if(split[0]+1 < 8) {
								this.checkPos(pos, ChessController.joinGridPos(split[0]+1, split[1]+1), moves);
							}
							this.checkPos(pos, ChessController.joinGridPos(split[0], split[1]+1), moves);

						}
					}
				}
			}
		}
		
		private void generateBishopMoves(int pos, ArrayList<Integer> moves) {
			
		}
		
		private void generateKnightMoves(int pos, ArrayList<Integer> moves) {
			int[] split = ChessController.splitGridPos(pos);
			ChessPiece toMove = this.gameboard[split[1]][split[0]];
			ArrayList<Integer> potential = findKnightSquares(pos);
			for(Integer gridPos : potential) {
				this.checkPos(pos, gridPos, moves);
			}
		}
		
		private void generateRookMoves(int pos, ArrayList<Integer> moves) {
			
		}
		
		private void generateQueenMoves(int pos, ArrayList<Integer> moves) {
			
		}
		
		private void generateKingMoves(int pos, ArrayList<Integer> moves) {
			int[] split = ChessController.splitGridPos(pos);
			int startY = split[1];
			int startX = split[0];
			int newGridPos = 0;
			if(startY+1 < 8) {
				newGridPos = ChessController.joinGridPos(startX, startY+1);
				this.checkPos(pos, newGridPos, moves);
				if(startX+1 < 8) {
					newGridPos = ChessController.joinGridPos(startX+1, startY+1);
					this.checkPos(pos, newGridPos, moves);
				}
				
				if(startX-1 > -1) {
					newGridPos = ChessController.joinGridPos(startX-1, startY+1);
					this.checkPos(pos, newGridPos, moves);
				} 
			} else if(startY-1 > -1) {
				newGridPos = ChessController.joinGridPos(startX, startY-1);
				this.checkPos(pos, newGridPos, moves);
				if(startX+1 < 8) {
					newGridPos = ChessController.joinGridPos(startX+1, startY-1);
					this.checkPos(pos, newGridPos, moves);
				} 
				
				if(startX-1 > -1) {
					newGridPos = ChessController.joinGridPos(startX-1, startY-1);
					this.checkPos(pos, newGridPos, moves);
				} 
			} 

			if(startX+1 > -1) {
				newGridPos = ChessController.joinGridPos(startX+1, startY);
				this.checkPos(pos, newGridPos, moves);
			}
			
			if(startX-1 < 8) {
				newGridPos = ChessController.joinGridPos(startX-1, startY);
				this.checkPos(pos, newGridPos, moves);
			}		
		}
		
		private boolean checkPos(int _old, int _new, ArrayList<Integer> legalMoves) {
			if(this.verifyMoveLegality( _old, _new)) {
				legalMoves.add(_new);
				return true;
			} return false;
		}
		
		public boolean movePiece_unchecked(int oldGridPos, int newGridPos) {
			if(oldGridPos == newGridPos) return false;
			System.out.printf("movePiece_unchecked(%d, %d)\n", oldGridPos, newGridPos);
			int[] oldSplit = ChessController.splitGridPos(oldGridPos);
			int[] newSplit = ChessController.splitGridPos(newGridPos);
			ChessPiece toMove = this.gameboard[oldSplit[1]][oldSplit[0]];
			System.out.println(toMove);
			System.out.println(this.gameboard[oldSplit[1]][oldSplit[0]]);
			ChessPiece target = this.gameboard[newSplit[1]][newSplit[0]];
			if(toMove == null) return false;
			if(target != null && target.getColor() == toMove.getColor()) return false;
			if(toMove.getColor() == 1) {
				if(toMove.getType() == 0) this.setBlackKingPos(newGridPos);
			} else {
				if(toMove.getType() == 0) this.setWhiteKingPos(newGridPos);
			}	

			this.gameboard[newSplit[1]][newSplit[0]] = toMove;
			this.gameboard[oldSplit[1]][oldSplit[0]] = null;
			this.gameboard[newSplit[1]][newSplit[0]].setLastMove(this.turnNumber);
			this.whiteToMove = !this.whiteToMove;
			adjustScore(target, toMove.getColor());
			
			if(toMove.getType() == 1 && newSplit[0] != oldSplit[0] && target == null) { //en passant
				ChessPiece actualTarget = this.gameboard[oldSplit[1]][newSplit[0]];
				this.adjustScore(actualTarget, toMove.getColor());
				this.gameboard[oldSplit[1]][newSplit[0]] = null;
			}
			
			this.turnNumber += (this.whiteToMove) ? 1 : 0;
			return true;
		}
		
		private void adjustScore(ChessPiece target, int victorious_color) {
			if(target != null) {
				int scoreAdjust = 0;
				int targVal = target.getValue();
				if(victorious_color == 0) {
					while(this.scr_b > 0 && scoreAdjust < targVal) {
						this.scr_b--;
						scoreAdjust++;
					}
					
					while(scoreAdjust < targVal) {
						this.scr_w++;
						scoreAdjust++;
					}
				} else {
					while(this.scr_w > 0 && scoreAdjust < targVal) {
						this.scr_w--;
						scoreAdjust++;
					}
					
					while(scoreAdjust < targVal) {
						this.scr_b++;
						scoreAdjust++;
					}
				}
			}
		}
		
		public ChessPiece[][] getBoard() { return this.gameboard; }
		
		public boolean whiteToMove() { return this.whiteToMove; }
		public int getWhiteKingPos() { return this.whiteKingPos; }
		public int setWhiteKingPos(int newGridPos) { 
			int t = this.whiteKingPos;
			this.whiteKingPos = newGridPos;
			return t;
			
		}
		public int getBlackKingPos() { return this.blackKingPos; }
		public int setBlackKingPos(int newGridPos) {
			int t = this.blackKingPos;
			this.blackKingPos = newGridPos;
			return t;
		}
		public boolean whiteInCheck() { return this.whiteInCheck; }
		public boolean blackInCheck() { return this.blackInCheck; }
		public ChessPiece gameboard(int y, int x) { return this.gameboard[y][x]; }
		
		public void _printGameState() {
			System.out.println("Printing the game board");
			for(int i = 0; i < this.gameboard.length; i++) {
				System.out.printf("|");
				for(int j = 0; j < this.gameboard[i].length; j++) {
					if(gameboard[i][j] != null) {
						char toPrint = charFromType[this.gameboard[i][j].getType()];
						System.out.printf("%c %c|", toPrint, (gameboard[i][j].getColor() == 0)?'w':'b');
					} else {
						System.out.printf("\\0 |");
					}
				}
				System.out.printf("\n");
			}
			System.out.println();
			System.out.printf("It's %s's turn. Turn number %d\n", (this.whiteToMove)? "white" : "black", turnNumber);
			char advPlr = '\0';
			if(this.scr_w > this.scr_b) {
				advPlr = 'w';
			} else if(this.scr_b > this.scr_w) {
				advPlr = 'b';
			}
			
			if(advPlr != '\0') {
				System.out.printf("%c: +%d\n", advPlr, (advPlr == 'w')?this.scr_w:this.scr_b);
			}
		}
		public ChessPiece getPieceAt(int i) {
			int[] split = ChessController.splitGridPos(i);
			return gameboard(split[1], split[0]);
		}

		
		public ChessPiece[][] initializePieces() {
			int sides = 2;
			ChessPiece[][] game = new ChessPiece[8][8];
			for(int i = 0; i < sides; i++) {
				game[(i == 0) ? 7 : 0][4] = new ChessPiece(0, i); //king
				game[(i == 0) ? 7 : 0][3] = new ChessPiece(5, i); //queen
				initializeIntermediates(game, i);
				for(int k = 0; k < 8; k++) { //
					game[(i == 0) ? 6 : 1][k] = new ChessPiece(1, i);
				}
			}
			return game;
		}
		
		private void initializeIntermediates(ChessPiece[][] chessboard, int i) {
			int mLocDex = 0;
			int pairDex = 2;
			final int[][] midLocations = { {61, 58, 57, 62, 63, 56}, {2, 5, 6, 1, 0, 7}}; //default positions of intermediate pieces (where Component 0 is top left square, -->, wrap)
			final int[] midOffsets = { 5, 2, 1, 6, 7, 0 };
			for(int j = 0; j < 3; j++) { //bishop, knight, rook					
				chessboard[(i == 0) ? 7 : 0][midOffsets[mLocDex]] = new ChessPiece(pairDex, midLocations[i][mLocDex++], i);
				chessboard[(i == 0) ? 7 : 0][midOffsets[mLocDex]] = new ChessPiece(pairDex++, midLocations[i][mLocDex++],i);
			}	
		}
		
		public boolean verifyMoveLegality(int heldGridPosition, int newGridPos) {
			
			if(newGridPos == heldGridPosition) return false; //you cant pass by not moving
			int[] split = ChessController.splitGridPos(newGridPos);
			int newGridX = split[0];
			int newGridY = split[1];
			
			int[] oldSplit = ChessController.splitGridPos(heldGridPosition);
			int oldGridX = oldSplit[0];
			int oldGridY = oldSplit[1];
			
			ChessPiece lastHeld = this.gameboard[oldGridY][oldGridX];
			if(lastHeld == null) return false;
			
			int diffX = newGridX - oldGridX; 
			int diffY = newGridY - oldGridY;
			
			ChessPiece targetPiece = this.gameboard[newGridY][newGridX];
			if(targetPiece != null && targetPiece.getColor() == lastHeld.getColor()) return false;
			boolean fixed = true;
			//boolean danger = !isKingSafe(lastHeld.getColor(), (lastHeld.getColor() == 0)?this.gameState.whiteKingPos:this.gameState.blackKingPos);
			switch(lastHeld.getType()) {
				case 0: //king
					if(Math.abs(diffX) > 1 || Math.abs(diffY) > 1) return false;
					else {
						this.gameboard[newGridY][newGridX] = lastHeld;
						this.gameboard[oldGridY][oldGridX] = null;
						fixed = isKingSafe(lastHeld.getColor(), ChessController.joinGridPos(newGridX, newGridY));
						this.gameboard[newGridY][newGridX] = targetPiece;
						this.gameboard[oldGridY][oldGridX] = lastHeld;
						if(fixed) {
							return true;
						} return false;
					}
				
				case 1: //pawn
					if(Math.abs(diffX) > 1 || Math.abs(diffY) > 2) return false;
					
					if(Math.abs(diffY) == 2) {
						if(Math.abs(diffX) > 0 || targetPiece != null || (lastHeld.getColor() == 0 && oldGridY != 6) || (lastHeld.getColor() == 1 && oldGridY != 1)) return false;
					} else {
						if(Math.abs(diffX) > 0 && Math.abs(diffY) == 0) return false;
						if(Math.abs(diffX) == 0 && targetPiece != null) return false;
						if(Math.abs(diffX) == 1 && targetPiece == null) {
							if(newGridY == 2 && lastHeld.getColor() == 0) {  //en passant potential
								System.out.println("possible en passant - white\n");
								ChessPiece enp = this.gameboard[oldGridY][newGridX];
								if(enp != null) { 
									System.out.printf("en passant target present, type = %s, color = %s, turn of last move = %d, current turn number = %d\n", nameFromType[enp.getType()], (enp.getColor() == 0)? "white" : "black", enp.getLastMove(), this.turnNumber);
								}
								if(enp != null && enp.getType() == 1 && enp.getColor() == 1 && enp.getLastMove() == this.turnNumber-1) {
									System.out.println("Further en passant test");
									this.gameboard[newGridY][newGridX] = lastHeld;
									this.gameboard[oldGridY][oldGridX] = null;
									this.gameboard[oldGridY][newGridX] = null;
									fixed = isKingSafe(lastHeld.getColor(), this.whiteKingPos);
									this.gameboard[oldGridY][newGridX] = enp;
									this.gameboard[oldGridY][oldGridX] = lastHeld;
									this.gameboard[newGridY][newGridX] = null;
									if(fixed) {
										return true;
									} return false;
								} else return false;
							} else if(newGridY == 5 && lastHeld.getColor() == 1) {
								System.out.println("possible en passant - black\n");
								ChessPiece enp = this.gameboard[oldGridY][newGridX];
								if(enp != null && enp.getType() == 1 && enp.getColor() == 1 && enp.getLastMove() == this.turnNumber) {
									this.gameboard[newGridY][newGridX] = lastHeld;
									this.gameboard[oldGridY][oldGridX] = null;
									ChessPiece enp_victim = this.gameboard[oldGridY][newGridX];
									this.gameboard[oldGridY][newGridX] = null;
									fixed = isKingSafe(lastHeld.getColor(), this.blackKingPos);
									this.gameboard[oldGridY][newGridX] = enp_victim;
									this.gameboard[oldGridY][oldGridX] = lastHeld;
									this.gameboard[newGridY][newGridX] = null;
									if(fixed) {
										return true;
									} return false;
								} else return false;
							} else {
								return false;
							}
						}
					}
					this.gameboard[newGridY][newGridX] = lastHeld;
					this.gameboard[oldGridY][oldGridX] = null;
					fixed = isKingSafe(lastHeld.getColor(), (lastHeld.getColor() == 0)? this.whiteKingPos : this.blackKingPos);
					this.gameboard[newGridY][newGridX] = targetPiece;
					this.gameboard[oldGridY][oldGridX] = lastHeld;
					if(fixed) {
						return true;
					} return false;

				case 2: //bishop
					
					break;
				case 3: //knight
					ArrayList<Integer> knightSquares = findKnightSquares(heldGridPosition);
					if(!knightSquares.contains(new Integer(newGridPos))) return false;
					this.gameboard[newGridY][newGridX] = lastHeld;
					this.gameboard[oldGridY][oldGridX] = null;
					fixed = isKingSafe(lastHeld.getColor(), (lastHeld.getColor() == 0)? this.whiteKingPos : this.blackKingPos);
					this.gameboard[newGridY][newGridX] = targetPiece;
					this.gameboard[oldGridY][oldGridX] = lastHeld;
					if(fixed) {
						return true;
					} return false;

				case 4: //rook
					if(Math.abs(diffX) > 0 && Math.abs(diffY) > 0) return false;
					
					if(Math.abs(diffX) > 0) {
						int direction = diffX > 0 ? 1 : -1;
						int mkX = oldGridX + direction;
						while(mkX != newGridX) {
							ChessPiece t = this.gameboard[oldGridY][mkX];
							if(t != null) return false;
							mkX += direction;
						}
						
					} else if(Math.abs(diffY) > 0) {
						int direction = diffY > 0 ? 1 : -1;
						int bound = (direction == 1) ? 8 : -1;
						int mkY = oldGridY + direction;
						while(mkY != bound && mkY != newGridY) {
							ChessPiece t = this.gameboard[mkY][oldGridX];
							if(t != null) return false;
							mkY += direction;
						}	
						
					} else return false; //theoretically not possible anyway (non move)
					this.gameboard[newGridY][newGridX] = lastHeld;
					this.gameboard[oldGridY][oldGridX] = null;
					fixed = isKingSafe(lastHeld.getColor(), (lastHeld.getColor() == 0)? this.whiteKingPos : this.blackKingPos);
					this.gameboard[newGridY][newGridX] = targetPiece;
					this.gameboard[oldGridY][oldGridX] = lastHeld;
					if(fixed) return true;
					return false;
					
				case 5: //queen
					
					if(Math.abs(diffX) > Math.abs(diffY)) {
						
					} else if(Math.abs(diffY) > Math.abs(diffX)) {
						
					} else { //Math.abs(diffX) == Math.abs(diffY)
						
					}
					
				default:
					break;
					
				
			}
			
			
			return true;
		}
		
		private boolean isKingSafe(int color, int kingGridPos) {
			ArrayList<Integer> knightSquares = findKnightSquares(kingGridPos);
			for(Integer i : knightSquares) {
				int[] split = ChessController.splitGridPos(i);
				ChessPiece t = this.gameboard[split[1]][split[0]];
				if(t != null) {
					if(t.getColor() != color && t.getType() == 3) {
						return false;
					}
				}
			}
			
			return safe(color, kingGridPos);
		}
		
		private boolean safe(int color, int kingGridPos) { 
			int[] split = ChessController.splitGridPos(kingGridPos);
			int startX = split[0];
			int startY = split[1];
			
			int mkY = startY+1;//UP
			while(mkY < 8 && mkY > -1) {
				ChessPiece t = this.gameboard[mkY][startX];
				if(t != null) {
					if(t.getColor() == color) break;
					if(mkY == startY+1 && t.getType() == 0) return false;
					else if(t.getType() == 4 || t.getType() == 5) return false;	
					else break;
				}
				mkY--;
			}
			mkY = startY-1;//DOWN
			while(mkY > -1 && mkY < 8) {
				ChessPiece t = this.gameboard[mkY][startX];
				if(t != null) {
					if(t.getColor() == color) break;
					if(mkY == startY-1 && t.getType() == 0) return false;
					else if(t.getType() == 4 || t.getType() == 5) return false;
					else break;
				}
				mkY++;
			}
			int mkX = startX-1; //LEFT
			while(mkX < 8 && mkX > -1) {
				ChessPiece t = this.gameboard[startY][mkX];
				if(t != null) {
					if(t.getColor() == color) break;
					if(mkX == startX-1 && t.getType() == 0) return false;
					else if(t.getType() == 4 || t.getType() == 5) return false;
					else break;
				}
				mkX--;
			}
			mkX = startX+1; //RIGHT
			while(mkX > -1 && mkX < 8) {
				ChessPiece t = this.gameboard[startY][mkX];
				if(t != null) {
					if(t.getColor() == color) break;
					if(mkX == startX+1 && t.getType() == 0) return false;
					else if(t.getType() == 4 || t.getType() == 5) return false;
					else break;
				}
				mkX++;
			}
			mkX = startX-1; //UP LEFT
			mkY = startY-1;
			if(mkX > -1 && mkX < 8 && mkY < 8 && mkY > -1 && color == 0) {
				ChessPiece t = this.gameboard[mkY][mkX];
				if(t != null) {
					if(t.getColor() != color && t.getType() == 1) return false;
				}
			}
			while(mkX > -1 && mkX < 8 && mkY < 8 && mkY > -1) {
				ChessPiece t = this.gameboard[mkY][mkX];
				if(t != null) {
					if(t.getColor() == color) break;
					if(mkX == startX-1 && mkY == startY-1 && t.getType() == 0) return false;
					else if(t.getType() == 2 || t.getType() == 5) return false;
					else break;
				}
				mkX--;
				mkY--;
			}
			mkX = startX+1; //UP RIGHT
			mkY = startY-1;
			if(mkX > -1 && mkX < 8 && mkY < 8 && mkY > -1 && color == 0) {
				ChessPiece t = this.gameboard[mkY][mkX];
				if(t != null) {
					if(t.getColor() != color && t.getType() == 1) return false;
				}
			}
			while(mkX < 8 && mkX > -1 && mkY < 8 && mkY > -1) {
				ChessPiece t = this.gameboard[mkY][mkX];
				if(t != null) {
					if(t.getColor() == color) break;
					if(mkX == startX+1 && mkY == startY-1 && t.getType() == 0) return false;
					else if(t.getType() == 2 || t.getType() == 5) return false;
					else break;
				}
				mkX++;
				mkY--;
			}
			mkX = startX-1; //DOWN LEFT
			mkY = startY+1;
			if(mkX > -1 && mkX < 8 && mkY < 8 && mkY > -1 && color == 1) {
				ChessPiece t = this.gameboard[mkY][mkX];
				if(t != null) {
					if(t.getColor() != color && t.getType() == 1) return false;
				}
			}
			while(mkX > -1 && mkX < 8 && mkY > -1 && mkY < 8) {
				ChessPiece t = this.gameboard[mkY][mkX];
				if(t != null) {
					if(t.getColor() == color) break;
					if(mkX == startX-1 && mkY == startY+1 && t.getType() == 0) return false;
					else if(t.getType() == 2 || t.getType() == 5) return false;
					else break;
				}
				mkX--;
				mkY++;
			}
			mkX = startX+1; //DOWN RIGHT
			mkY = startY+1;
			if(mkX > -1 && mkX < 8 && mkY < 8 && mkY > -1 && color == 1) {
				ChessPiece t = this.gameboard[mkY][mkX];
				if(t != null) {
					if(t.getColor() != color && t.getType() == 1) return false;
				}
			}
			while(mkX > -1 && mkX < 8 && mkY > -1 && mkY < 8) {
				ChessPiece t = this.gameboard[mkY][mkX];
				if(t != null) {
					if(t.getColor() == color) break;
					if(mkX == startX+1 && mkY == startY+1 && t.getType() == 0) return false;
					else if(t.getType() == 2 || t.getType() == 5) return false;
					else break;
				}
				mkX++;
				mkY++;
			}
			return true;
			
		}
		
		private ArrayList<Integer> findKnightSquares(int basePosition) {
			ArrayList<Integer> knightSquares = new ArrayList<>();
			int[] split = ChessController.splitGridPos(basePosition);
			int baseX = split[0];
			int baseY = split[1];
			//baseX + 2: baseY + 1, baseY - 1
			//baseX - 2: baseY + 1, baseY - 1
			//baseY + 2: baseX + 1, baseX - 1
			//baseY - 2: baseX + 1, baseX - 1
			if(baseX + 2 < 8) {
				if(baseY + 1 < 8) {
					knightSquares.add(ChessController.joinGridPos(baseX + 2, baseY + 1));
				}  if(baseY - 1 > -1) {
					knightSquares.add(ChessController.joinGridPos(baseX + 2,  baseY - 1));
				}
			} if(baseX - 2 > -1) {
				if(baseY + 1 < 8) {
					knightSquares.add(ChessController.joinGridPos(baseX - 2, baseY + 1));
				} else if(baseY - 1 > -1) {
					knightSquares.add(ChessController.joinGridPos(baseX - 2, baseY - 1));
				}
			}
			
			if(baseY + 2 < 8) {
				if(baseX + 1 < 8) {
					knightSquares.add(ChessController.joinGridPos(baseX + 1, baseY + 2));
				} if(baseX - 1 > -1) {
					knightSquares.add(ChessController.joinGridPos(baseX - 1, baseY + 2));
				}
			} else if(baseY - 2 > -1) {
				if(baseX + 1 < 8) {
					knightSquares.add(ChessController.joinGridPos(baseX + 1, baseY - 2));
				} if(baseX - 1 > -1) {
					knightSquares.add(ChessController.joinGridPos(baseX - 1, baseY - 2));
				}
			}
			//System.out.printf("findKnightSquares: basePosition = %d\n", basePosition);
			for(int i = 0; i < knightSquares.size(); i++) {
				split = ChessController.splitGridPos(knightSquares.get(i));
				//System.out.printf("knightSquares.get(%d) = (%d, %d)\n", i, split[0], split[1]);
			}
			return knightSquares;
		}
	}