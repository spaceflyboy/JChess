# JChess
Java Chess program

Model: ChessGame.java, ChessPiece.java: internal board data, move verification, checkmate finding.
View: Swing based, relatively simple for now. I hope to upgrade it to JavaFX once I have a more complete build of the model, including planned feature upgrades.
Controller: ChessController.java: has main(), some utility functions and serves essentially as a middleman between the view and the model. Hopefully this design choice will make implementing a turned based networked gamemode easier, even if it involves tearing some stuff down to build it back in a more logical way.
