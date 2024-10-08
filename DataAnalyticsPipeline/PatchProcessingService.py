import time
import matplotlib.pyplot as plt
import pandas as pd
from kafka import KafkaProducer
from sqlalchemy import create_engine
from kafka import KafkaProducer
engine = create_engine('postgresql+psycopg2://postgres:password@localhost/postgres')

class PatchProcessingService ():
    def __init__(self):
        query = 'SELECT * FROM reservations'
        self.df = pd.read_sql(query, engine)
        self.df['date'] = pd.to_datetime(self.df['date'])
        self.df['peopleCount'] = self.df['peopleCount'].astype(int)

    def plot_data(self):
        print("Plotting data")
        print(self.df[['date', 'peopleCount']])

        plt.plot(self.df['date'], self.df['peopleCount'])
        plt.xlabel('Date')
        plt.ylabel('People Count')
        plt.title('People Count Over Time')
        plt.show()

    def refresh_data(self):
        query = 'SELECT * FROM reservations'
        self.df = pd.read_sql(query, engine)
        self.df['date'] = pd.to_datetime(self.df['date'])
        self.df['peopleCount'] = self.df['peopleCount'].astype(int)
        return self.df


    def patchService(self):
        while True:
            print("Patch Service")
            self.refresh_data()
            self.plot_data()
            print(self.df)
            time.sleep(60)
            kafkaProducer = KafkaProducer(bootstrap_servers='localhost:9092')
            kafkaProducer.send('patchProcesses', value=self.df.to_json())
            kafkaProducer.flush()
            return self.df



if __name__ == '__main__':
    patch = PatchProcessingService()
    patch.patchService()



