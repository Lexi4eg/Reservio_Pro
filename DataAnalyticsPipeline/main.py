import threading
from confluent_kafka import Consumer, KafkaException
import logging
import pandas as pd
import json
import matplotlib.pyplot as plt

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class DataCleaning:

    def __init__(self):
        self.df = pd.DataFrame()

    def clean_data(self, data):
        data_dict = json.loads(data)
        new_df = pd.DataFrame([data_dict])
        new_df['date'] = pd.to_datetime(new_df['date'])
        self.df = pd.concat([self.df, new_df], ignore_index=True)
        return self.df

    def plot_data(self):
        print("Plotting data")
        print(self.df[['date', 'peopleCount']])

        plt.plot(self.df['date'], self.df['peopleCount'])
        plt.xlabel('Date')
        plt.ylabel('People Count')
        plt.title('People Count Over Time')
        plt.show()

class KafkaConsumer:

    def __init__(self):
        self.c = Consumer({
            'bootstrap.servers': 'localhost:9092',
            'group.id': 'mygroup',
            'auto.offset.reset': 'earliest',
            'enable.auto.commit': False  # Disable auto commit
        })
        self.topics = []
        self.raw_data = []
        self.lock = threading.Lock()

    def subscribe(self, topics):
        self.topics = topics
        self.c.subscribe(topics)

    def consume(self):
        try:
            while True:
                msg = self.c.poll(1.0)

                if msg is None:
                    continue
                if msg.error():
                    raise KafkaException(msg.error())

                with self.lock:
                    self.raw_data.append(msg.value().decode('utf-8'))
                    print(f"Consumed message: {msg.value().decode('utf-8')}")

                self.c.commit(msg)
        except KeyboardInterrupt:
            pass
        finally:
            self.c.close()
            self.process_data()  # Process all data after consuming

    def process_data(self):
        with self.lock:
            data_to_process = self.raw_data
            self.raw_data = []

        data_cleaning = DataCleaning()
        for data in data_to_process:
            data_cleaning.clean_data(data)
        data_cleaning.plot_data()

if __name__ == '__main__':
    consumer = KafkaConsumer()
    consumer.subscribe(['reservations'])
    consumer.consume()