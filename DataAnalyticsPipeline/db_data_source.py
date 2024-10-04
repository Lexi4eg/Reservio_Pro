import matplotlib.pyplot as plt
from sqlalchemy import create_engine
import pandas as pd


engine = create_engine('postgresql+psycopg2://postgres:password@localhost/postgres')

query = 'SELECT * FROM reservations'
df = pd.read_sql(query, engine)


def generate_report():


    total_reservations = len(df)
    reservations_by_date = df['date'].value_counts().sort_index()
    average_people_count = df['peoplecount'].mean()
    reservations_by_table = df['tableid'].value_counts()
    special_requests_summary = df['specialrequests'].value_counts()
    high_chair_requests = df['highchair'].sum()
    reservations_by_customer = df.groupby(['firstname', 'lastname']).size()

    # Print the report
    print(f"Total Reservations: {total_reservations}")
    print("\nReservations by Date:")
    print(reservations_by_date)
    print(f"\nAverage People Count: {average_people_count:.2f}")
    print("\nReservations by Table:")
    print(reservations_by_table)
    print("\nSpecial Requests Summary:")
    print(special_requests_summary)
    print(f"\nHigh Chair Requests: {high_chair_requests}")
    print("\nReservations by Customer:")
    print(reservations_by_customer)

generate_report()


def showReservationsByDates(df1):
    df_grouped = df1.groupby('date').sum().reset_index()

    plt.figure(figsize=(15, 10))
    plt.plot(df_grouped['date'], df_grouped['peoplecount'])
    plt.xlabel('Date')
    plt.ylabel('People Count')
    plt.title('People Count Over Time')
    plt.show()


def shwoPeopleByReservations(df1) :
    df_grouped = df1.drop(columns=['date']).groupby(['firstname', 'lastname']).sum().reset_index()

    df_sorted = df_grouped.sort_values('peoplecount', ascending=False)

    plt.figure(figsize=(15, 10))
    plt.bar(df_sorted['firstname'] + ' ' + df_sorted['lastname'], df_sorted['peoplecount'])
    plt.xlabel('Customer (Firstname Lastname)')
    plt.ylabel('People Count')
    plt.title('People Count by Customer')
    plt.xticks(rotation=90)
    plt.show()


showReservationsByDates(df)
shwoPeopleByReservations(df)