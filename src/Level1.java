import java.util.List;

public class Level1 implements Level {

    // here solve problems! lol

    @Override
    public String solve(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        int n = Integer.parseInt(lines.get(0).trim());

        for(int i = 1; i <= n; i++){
            String[] parts = lines.get(i).trim().split("\\s+");
            long x = Long.parseLong(parts[0]);
            long y = Long.parseLong(parts[1]);
            sb.append(room(x, y)).append('\n');
        }
        return sb.toString();
    }

    static long room(long x, long y){
        return (x / 3) * y;
    }

}
