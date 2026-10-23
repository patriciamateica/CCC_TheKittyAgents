import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Runner {
    public static void run(int level, Level solver) throws IOException {
        Path dir = Paths.get("src", "data");
        String glob = "in_level-" + level + "_*.txt";
        try (DirectoryStream<Path> files = Files.newDirectoryStream(dir, glob)) {
            for (Path in : files) {
                List<String> lines = Files.readAllLines(in);
                String outName = in.getFileName().toString().replaceFirst("^in_", "out_");
                Files.writeString(in.resolveSibling(outName), solver.solve(lines));
                System.out.println("Done: " + in.getFileName());
            }
        }
    }
}