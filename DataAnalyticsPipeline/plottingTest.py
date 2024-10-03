import pandas as pd
from packaging.tags import platform_tags
import matplotlib.pyplot as plt

data = {
    'date': ['2024-10-03 17:30:00+00:00' , '2024-10-03 18:30:00+00:00'],
    'peopleCount': [4,4],
}

df = pd.DataFrame(data)
df['date'] = pd.to_datetime(df['date'])

plt.plot(df['date'], df['peopleCount'])
plt.xlabel('Date')
plt.ylabel('People Count')
plt.title('People Count Over Time')
plt.show()



print(df)