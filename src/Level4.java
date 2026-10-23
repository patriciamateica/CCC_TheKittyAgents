import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Level4 implements Level {
    private static Map<Integer, List<Integer>> offsets = Map.of(
            1, List.of(0, 1),
            2, List.of(1, 1),
            3, List.of(1, -1),
            4, List.of(0, -1),
            5, List.of(-1, -1),
            6, List.of(-1, 1)
    );

    private static Map<Character, List<Integer>> clockwiseOffsets = Map.of(
            'a', List.of(0, 1),
            'b', List.of(1, 1),
            'c', List.of(1, -1),
            'd', List.of(0, -1),
            'f', List.of(-1, -1),
            'g', List.of(-1, 1)
    );

    private static Map<Character, List<Integer>> counterclockwiseOffsets = Map.of(
            'a', List.of(0, -1),
            'b', List.of(1, -1),
            'c', List.of(1, 1),
            'd', List.of(0, 1),
            'f', List.of(-1, 1),
            'g', List.of(-1, -1)
    );

    @Override
    public String solve(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        int n = Integer.parseInt(lines.get(0).trim());
        boolean lastEmpty = true;

        List<char[][]> boards = parseGrids(lines);

        for (char[][] board : boards) {

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

            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                if (!block.isEmpty()) {
                    grids.add(ModifyMatrix(block));
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

    private static char[][] ModifyMatrix(List<String> block){
        int radius = Integer.parseInt(block.getFirst().strip());
        char[][] grid = BuildMatrix(radius);
        int[][] frequency = new int[grid.length][grid[0].length];
        for (int f = 0; f < grid.length; f++) {
            for (int c = 0; c < grid[0].length; c++) {
                frequency[f][c] = 0;
            }
        }
        int numberCommands = Integer.parseInt(block.get(1).strip());

        int columns = 2 + radius * 2 + radius * 2 - 1;
        int rows = (int)Math.ceil((double)columns / 2);

        List<EmitterDetails> positions = List.of(
                new EmitterDetails('1', new Pair((int)Math.ceil((double)rows / 2), 0), false, 'f', 'c'),
                new EmitterDetails('2', new Pair(0, radius), false, 'a', 'b'),
                new EmitterDetails('3', new Pair(0, columns - radius - 1), false, 'b', 'a'),
                new EmitterDetails('4', new Pair((int)Math.ceil((double)rows / 2), columns - 1), false, 'c', 'f'),
                new EmitterDetails('5', new Pair(rows - 1, columns - radius - 1), false, 'd', 'e'),
                new EmitterDetails('6', new Pair(rows - 1, radius), false, 'e', 'd')
        );

        for(EmitterDetails emitter: positions){
            grid[emitter.position.left()][emitter.position.right()] = emitter.val;
        }

        for(int i = 2; i < block.size(); i++){
            List<String> commands = List.of(block.get(i).split(" "));
            for(int e = 0; e <=5 ;  e++){
                AplyCommand(grid, commands.get(e), positions.get(e));
            }
            List<Pair> emitters = positions.stream()
                    .filter(emitterDetails -> emitterDetails.on)
                    .map(emitterDetails -> emitterDetails.position)
                    .toList();

            for (Pair emitter : emitters) {
                MutateMatrix(emitter, grid, frequency);
            }

        }
        return grid;
    }

    private static void AplyCommand(char[][] board, String command, EmitterDetails emitter){
        if(command.equals('F')){
            int newRow = emitter.position.left() + clockwiseOffsets.get(emitter.clockwise).get(0);
            int newColumn = emitter.position.right() + clockwiseOffsets.get(emitter.clockwise).get(1);

            if(board[newRow][newColumn] != '*'){
                char newOffset = getNextOffset(emitter.clockwise);
                emitter.clockwise = newOffset;
            }
            newRow = emitter.position.left() + clockwiseOffsets.get(emitter.clockwise).get(0);
            newColumn = emitter.position.right() + clockwiseOffsets.get(emitter.clockwise).get(1);
            board[emitter.position.left()][emitter.position.right()] = '*';
            emitter.position.left = newRow;
            emitter.position.right = newColumn;
            board[emitter.position.left()][emitter.position.right()] = emitter.val;
        }
        else if(command.equals('B')){
            int newRow = emitter.position.left() + counterclockwiseOffsets.get(emitter.counterclockwise).get(0);
            int newColumn = emitter.position.right() + counterclockwiseOffsets.get(emitter.counterclockwise).get(1);

            if(board[newRow][newColumn] != '*'){
                char newOffset = getNextOffset(emitter.counterclockwise);
                emitter.counterclockwise = newOffset;
            }
            newRow = emitter.position.left() + counterclockwiseOffsets.get(emitter.counterclockwise).get(0);
            newColumn = emitter.position.right() + counterclockwiseOffsets.get(emitter.counterclockwise).get(1);
            board[emitter.position.left()][emitter.position.right()] = '*';
            emitter.position.left = newRow;
            emitter.position.right = newColumn;
            board[emitter.position.left()][emitter.position.right()] = emitter.val;
        }
        else if(command.equals('T')){
            emitter.on = !emitter.on;
        }
    }

    private static char getNextOffset(char offset){
        if(offset == 'f'){
            return 'a';
        }
        return (char)((int) offset + 1);
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

    private static char[][] BuildMatrix(int radius) {
        int columns = 2 + radius * 2 + radius * 2 - 1;
        int rows = (int) Math.ceil((double) columns / 2);

        System.out.println(rows);
        System.out.println(columns);

        char[][] board = new char[rows][columns];

        int nrLines = 0;
        int nrZeroes = radius * 2 - 1;
        int nrCurrentLines = 0;
        boolean lastStar = false;

        for (int r = (int) Math.ceil((double) rows / 2) - 1; r >= 0; r--) {
            nrCurrentLines = 0;
            lastStar = false;
            for (int c = 0; c <= (int) Math.ceil((double) columns / 2); c++) {
                if (nrCurrentLines < nrLines) {
                    board[r][c] = '-';
                    board[r][columns - c - 1] = '-';
                    board[rows - r - 1][c] = '-';
                    board[rows - r - 1][columns - c - 1] = '-';

                    nrCurrentLines++;
                } else if (!lastStar) {
                    board[r][c] = '*';
                    board[r][columns - c - 1] = '*';
                    board[rows - r - 1][c] = '*';
                    board[rows - r - 1][columns - c - 1] = '*';

                    lastStar = true;
                } else if (r == 0) {
                    if (board[r][c - 1] != '-') {
                        board[r][c] = '-';
                        board[r][columns - c - 1] = '-';
                        board[rows - r - 1][c] = '-';
                        board[rows - r - 1][columns - c - 1] = '-';
                    } else {
                        board[r][c] = '*';
                        board[r][columns - c - 1] = '*';
                        board[rows - r - 1][c] = '*';
                        board[rows - r - 1][columns - c - 1] = '*';
                    }
                } else {
                    if (board[r][c - 1] != '-') {
                        board[r][c] = '-';
                        board[r][columns - c - 1] = '-';
                        board[rows - r - 1][c] = '-';
                        board[rows - r - 1][columns - c - 1] = '-';
                    } else {
                        board[r][c] = 'O';
                        board[r][columns - c - 1] = 'O';
                        board[rows - r - 1][c] = 'O';
                        board[rows - r - 1][columns - c - 1] = 'O';
                    }
                }
            }
            nrLines++;
        }
        return board;
    }

    private static void MutateMatrix(Pair pair, char[][] board, int[][] frequency) {
        Pair emitter = pair;
        int emitterRow = emitter.left();
        int emitterColumn = emitter.right();
        int emitterValue = Integer.parseInt(String.valueOf(board[emitterRow][emitterColumn]));

        List<Integer> offset = offsets.get(emitterValue);
        int offsetRow = offset.get(0);
        int offsetColumn = offset.get(1);

        while (emitterColumn >= 0 && emitterColumn < board[0].length && emitterRow >= 0 && emitterRow < board.length) {
            if (board[emitterRow][emitterColumn] == 'O' || board[emitterRow][emitterColumn] == 'M') {
                frequency[emitterRow][emitterColumn]++;
            }

            if (frequency[emitterRow][emitterColumn] == 2) {
                board[emitterRow][emitterColumn] = 'M';
            } else if (frequency[emitterRow][emitterColumn] > 2) {
                board[emitterRow][emitterColumn] = 'O';
            }

            emitterRow += offsetRow;
            emitterColumn += offsetColumn;
        }
    }
}
