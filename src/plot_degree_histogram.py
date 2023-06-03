import matplotlib.pyplot as plt
import sys
import os
import numpy as np

def load_degrees(filename):
    """
    Load the degree values from the specified file.

    Args:
        filename (str): The name of the file containing the degree values.

    Returns:
        list: A list of degree values.
    """
    degrees = []
    with open(filename, 'r') as file:
        for line in file:
            degrees.append(int(line))
    return degrees

def plot_degree_histogram(degrees, base_output_file):
    """
    Plot the histogram of degree distribution in both linear and logarithmic scale.

    Args:
        degrees (list): A list of degree values.
        base_output_file (str): The base name for the output plot files.
    """
    plt.style.use('dark_background')

    # Prepare bins
    max_degree = max(degrees)
    bins = 10 ** np.linspace(np.log10(1), np.log10(max_degree+1), num=75)

    # Plot the histogram in linear scale
    plt.hist(degrees, bins=bins, edgecolor='black')
    plt.gca().xaxis.set_major_formatter(plt.FuncFormatter(lambda x, _: '{:g}'.format(x)))
    plt.gca().yaxis.set_major_formatter(plt.FuncFormatter(lambda y, _: '{:g}'.format(y)))
    plt.title("Degree Distribution (Linear Scale)")
    plt.xlabel("Degree")
    plt.ylabel("Number of Nodes")
    plt.savefig(f"{base_output_file}_linear.png", format='png', dpi=400)
    plt.clf()  # Clear the current figure

    # Plot the histogram in log scale
    plt.hist(degrees, bins=bins, log=True, edgecolor='black')
    plt.gca().set_xscale("log")
    plt.gca().xaxis.set_major_formatter(plt.FuncFormatter(lambda x, _: '{:g}'.format(x)))
    plt.gca().yaxis.set_major_formatter(plt.FuncFormatter(lambda y, _: '{:g}'.format(y)))
    plt.title("Degree Distribution (Log Scale)")
    plt.xlabel("Log Degree")
    plt.ylabel("Log Number of Nodes")
    plt.savefig(f"{base_output_file}_log.png", format='png', dpi=400)
    plt.clf()  # Clear the current figure

def main():
    if len(sys.argv) != 2:
        print("Error please try again.\nUsage: python degree_distribution.py <file_path>")
        exit(1)

    input_file = sys.argv[1]
    degree_list = load_degrees(input_file)
    base_name = os.path.splitext(input_file)[0]

    plot_degree_histogram(degree_list, base_name)

if __name__ == "__main__":
    main()
