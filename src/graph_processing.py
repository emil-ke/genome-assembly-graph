import matplotlib.pyplot as plt
import sys
from collections import defaultdict

def parse_file(filename):
    graph =  defaultdict(list)
    identifier_to_int = {}
    int_identifier = 0
    with open(filename, 'r') as file:
        for line in file:
            row = line.strip().split()
            v1_str, v2_str = row[0], row[1]

            if v1_str not in identifier_to_int:
                identifier_to_int[v1_str] = int_identifier
                int_identifier += 1
            if v2_str not in identifier_to_int:
                identifier_to_int[v2_str] = int_identifier
                int_identifier += 1

            v1 = identifier_to_int[v1_str]
            v2 = identifier_to_int[v2_str]

            graph[v1].append(v2)
            graph[v2].append(v1) 

    return graph

def calculate_degrees(graph: dict) -> dict:
    degree_dict = {}
    for v in graph:
        degree_dict[v] = len(graph[v])
    return degree_dict


def analyze_degree_distribution(degree_dict: dict):
    degrees = list(degree_dict.values())
    plt.hist(degrees, bins='auto')
    plt.title("Degree Distribution")
    plt.xlabel("Degree")
    plt.ylabel("Number of Nodes")
    plt.show()

def main(filename: str) -> None:
    graph = parse_file(filename)
    degree_dict = calculate_degrees(graph)
    analyze_degree_distribution(degree_dict)


if __name__ == "__main__":
    try:
        main(sys.argv[1])
    except Exception as e:
        raise e
