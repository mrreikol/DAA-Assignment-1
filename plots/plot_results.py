import pandas as pd
import matplotlib.pyplot as plt
import os

os.makedirs('plots', exist_ok=True)

df = pd.read_csv('results/results.csv')
random_data = df[df['InputType'].isin(['Random', 'Random Points'])]

plt.figure(figsize=(10, 6))
for algo in random_data['Algorithm'].unique():
    subset = random_data[random_data['Algorithm'] == algo]
    plt.plot(subset['Size'], subset['Time_ns'], marker='o', label=algo)

plt.title('Execution Time vs Input Size (Random Input)')
plt.xlabel('Input Size (n)')
plt.ylabel('Execution Time (ns)')
plt.legend()
plt.grid(True)
plt.savefig('plots/time_vs_n.png')
plt.close()

plt.figure(figsize=(10, 6))
for algo in random_data['Algorithm'].unique():
    subset = random_data[random_data['Algorithm'] == algo]
    plt.plot(subset['Size'], subset['MaxDepth'], marker='s', label=algo)

plt.title('Max Recursion Depth vs Input Size (Random Input)')
plt.xlabel('Input Size (n)')
plt.ylabel('Max Recursion Depth')
plt.legend()
plt.grid(True)
plt.savefig('plots/depth_vs_n.png')
plt.close()