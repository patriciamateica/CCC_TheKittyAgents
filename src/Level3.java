import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Level3 implements Level {
    private static Map<Integer, List<Integer>> offsets = Map.of(
            1, List.of(0, 1),
            2, List.of(1, 1),
            3, List.of(1, -1),
            4, List.of(0, -1),
            5, List.of(-1, -1),
            6, List.of(-1, 1)
    );

    @Override
    public String solve(List<String> lines) {
        List<char[][]> boards = parseGrids(lines);
        StringBuilder sb = new StringBuilder();

        for(char[][] board : boards){
            List<Pair> emitters = getEmitter(board);
            int[][] frequency = new int[board.length][board[0].length];
            for(int f = 0; f < board.length; f++){
                for(int c = 0; c < board[0].length; c++){
                    frequency[f][c] = 0;
                }
            }

            for(Pair emitter: emitters){
                MutateMatrix(emitter, board, frequency);
            }

            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                    sb.append(board[r][c]);
                }
                sb.append('\n');
            }

            sb.append('\n');
        }
        return sb.toString();
    }

    public static List<char[][]> parseGrids(List<String> lines) {
        List<char[][]> grids = new ArrayList<>();
        List<String> block = new ArrayList<>();

        int nrBlocks = Integer.parseInt(lines.get(0));
        boolean lastEmpty = true;

        for (int i = 1; i < lines.size(); i++) {
            if (lastEmpty) {
                lastEmpty = false;
                continue;
            }

            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                if (!block.isEmpty()) {
                    grids.add(toMatrix(block));
                    block.clear();
                }

                lastEmpty = true;
            } else {
                block.add(line);
                lastEmpty = false;
            }
        }
        if (!block.isEmpty()) {
            grids.add(toMatrix(block));
        }
        return grids;
    }

    private static char[][] toMatrix(List<String> block) {
        char[][] m = new char[block.size()][];
        for (int r = 0; r < block.size(); r++) {
            m[r] = block.get(r).toCharArray();
        }
        return m;
    }

    private static List<Pair> getEmitter(char[][] board) {
        List<Pair> pairs = new ArrayList<>();
        List<Character> characters = List.of('-', 'O', '*', 'M');

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (characters.contains(board[i][j])) continue;

                pairs.add(new Pair(i, j));
            }
        }

        return pairs;
    }

    private static void MutateMatrix(Pair pair, char[][] board, int[][] frequency){
        Pair emitter = pair;
        int emitterRow = emitter.left();
        int emitterColumn = emitter.right();
        int emitterValue = Integer.parseInt(String.valueOf(board[emitterRow][emitterColumn]));

        List<Integer> offset = offsets.get(emitterValue);
        int offsetRow = offset.get(0);
        int offsetColumn = offset.get(1);

        while (emitterColumn >= 0 && emitterColumn < board[0].length && emitterRow >= 0 && emitterRow < board.length) {
            if (board[emitterRow][emitterColumn] == 'O' || board[emitterRow][emitterColumn] == 'M') {
                frequency[emitterRow][emitterColumn] ++;
            }

            if(frequency[emitterRow][emitterColumn] == 2) {
                board[emitterRow][emitterColumn] = 'M';
            }
            else if(frequency[emitterRow][emitterColumn] > 2) {
                board[emitterRow][emitterColumn] = 'O';
            }

            emitterRow += offsetRow;
            emitterColumn += offsetColumn;
        }
    }
}
