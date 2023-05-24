import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DegreeDistribution {
    public static void main(String[] args) {
        HashMap<String, Integer> vertexToId = new HashMap<>();
        HashMap<Integer, Integer> degreeDistribution = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line, vertexToId, degreeDistribution);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Output the degree distribution
        System.out.println("The representation is like n^(number of vertices with degree n), we have: ");
        printDegreeDistribution(degreeDistribution);
    }

    private static void processLine(String line, HashMap<String, Integer> vertexToId,
                                    HashMap<Integer, Integer> degreeDistribution) {
        String[] splitLine = line.split("\\s+");
        String vertex1 = splitLine[0];
        String vertex2 = splitLine[1];

        int id1 = vertexToId.computeIfAbsent(vertex1, k -> vertexToId.size());
        int id2 = vertexToId.computeIfAbsent(vertex2, k -> vertexToId.size());

        incrementDegree(degreeDistribution, id1);
        incrementDegree(degreeDistribution, id2);
    }

    private static void incrementDegree(HashMap<Integer, Integer> degreeDistribution, int vertex) {
        degreeDistribution.put(vertex, degreeDistribution.getOrDefault(vertex, 0) + 1);
    }

    private static void printDegreeDistribution(HashMap<Integer, Integer> degreeDistribution) {
        StringBuilder sb = new StringBuilder();
        Map<Integer, Integer> degreeCounts = new HashMap<>();

        // Count the occurrences of each degree
        for (int degree : degreeDistribution.values()) {
            degreeCounts.put(degree, degreeCounts.getOrDefault(degree, 0) + 1);
        }

        // Generate the degree distribution representation
        for (int degree : degreeCounts.keySet()) {
            int count = degreeCounts.get(degree);
            sb.append(degree).append('^').append(count).append(' ');
        }
        String degreeDistributionStr = sb.toString().trim();
        System.out.println(degreeDistributionStr);
    }
}