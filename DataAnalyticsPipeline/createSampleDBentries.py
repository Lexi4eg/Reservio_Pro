from sqlalchemy import create_engine
import pandas as pd
import numpy as np
from datetime import datetime, timedelta
import uuid

def createSampleData():
    # Create the SQLAlchemy engine
    engine = create_engine('postgresql+psycopg2://postgres:password@localhost/postgres')

    # Generate sample data
    np.random.seed(0)
    num_samples = 100
    data = {
        'id': [str(uuid.uuid4()) for _ in range(num_samples)],
        'firstname': np.random.choice(['John', 'Jane', 'Sue', 'Fred', 'Alice', 'Bob', 'Carol', 'Dave'], num_samples),
        'lastname': np.random.choice(['Smith', 'Doe', 'Brown', 'Johnson', 'Williams', 'Jones', 'Miller', 'Davis'], num_samples),
        'date': [datetime.now() - timedelta(days=np.random.randint(0, 365)) for _ in range(num_samples)],
        'peoplecount': np.random.randint(1, 10, num_samples),
        'email': [f'user{i}@example.com' for i in range(num_samples)],
        'phonenumber': [f'555-01{i:03d}' for i in range(num_samples)],
        'specialrequests': np.random.choice(['None', 'Vegan', 'Window seat', 'Birthday'], num_samples),
        'highchair': np.random.choice([True, False], num_samples),
        'tableid': np.random.choice([f'T{i}' for i in range(1, 9)], num_samples),
        'numberchairs': np.random.randint(1, 10, num_samples)
    }

    df = pd.DataFrame(data)

    df.to_sql('reservations', engine, if_exists='append', index=False)

createSampleData()