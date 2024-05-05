import pandas as pd
import matplotlib.pyplot as plt
import json

host_throughput_df = pd.read_csv('hostThroughputBothHigh.csv', parse_dates=['date_time'])

host_throughput_limit_df = pd.read_csv('hostThroughputLimitBothHigh.csv', parse_dates=['date_time'])

host_throughput_limit_df['hosts_throughput_limit'] = host_throughput_limit_df['hosts_throughput_limit'].apply(json.loads)

host_throughput_limit_df = host_throughput_limit_df.explode('hosts_throughput_limit', ignore_index=True)

host_throughput_limit_df['host_id'] = host_throughput_limit_df['hosts_throughput_limit'].apply(lambda x: x['host']['id'])
host_throughput_limit_df['throughput_limit'] = host_throughput_limit_df['hosts_throughput_limit'].apply(lambda x: x['throughput'])

host_throughput_df = host_throughput_df.sort_values('date_time')
host_throughput_limit_df = host_throughput_limit_df.sort_values('date_time')

fig, ax = plt.subplots(figsize=(12, 6))

for host_id in [1, 2]:
    host_data = host_throughput_df[host_throughput_df['host_id'] == host_id]
    color = 'r' if host_id == 1 else 'orange'  
    ax.plot(host_data['date_time'], host_data['throughput'], color=color, label=f'Host ID {host_id}')

for host_id in [1, 2]:
    color = 'k' if host_id == 1 else 'gray' 
    host_data = host_throughput_limit_df[host_throughput_limit_df['host_id'] == host_id]
    ax.step(host_data['date_time'], host_data['throughput_limit'], where='post', color=color, label=f'Host ID {host_id} Limit')

ax.axhline(y=14000, color='r', linestyle='--', label='Required Throughput for ID 1 (Kbit)')
ax.axhline(y=5000, color='orange', linestyle='--', label='Required Throughput for ID 2 (Kbit)')

ax.set_xlabel('Time')
ax.set_ylabel('Throughput (Kbit)')
ax.set_title('Throughput Limits with Cubic and Cubic ccs')

ax.legend()

plt.savefig('cubicCubic.png', dpi=300, bbox_inches='tight')
