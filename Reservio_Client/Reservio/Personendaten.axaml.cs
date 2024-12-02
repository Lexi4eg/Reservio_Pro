using System;
using Avalonia.Controls;
using Avalonia.Interactivity;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace Reservio
{
    public partial class Personendaten : UserControl
    {
        private string SelectedTable { get; }
        private string Personenanzahl { get; }
        
        

        public Personendaten(string selectedTable, string personenanzahl)
        {
            InitializeComponent();
            SelectedTable = selectedTable;
            Personenanzahl = personenanzahl;

            // Den ausgewählten Tisch in einem TextBlock anzeigen
            DisplaySelectedTable();
        }

        private void DisplaySelectedTable()
        { 
            selectedTableTextBlock.Text = $"Ausgewählter Tisch: {SelectedTable}";
        }

        private void OnZurückButtonClick(object sender, RoutedEventArgs e)
        {
            this.Content = new ThirdPage(Personenanzahl);        
        }
        
        private async void OnWeiterButtonClick(object sender, RoutedEventArgs e)
        {
            var reservation = new ReservationRequest
            {
                id = "74678468",
                firstname = Vorname.Text,
                lastname = Nachname.Text,
                date = DateTime.UtcNow,
                peopleCount = int.Parse(Personenanzahl),
                email = Email.Text,
                phoneNumber = Telefonnummer.Text,
                specialRequests = "",
                highChair = false,
                tableID = SelectedTable,
                numberChairs = 0
            };

            string apiUrl = "http://localhost:4567/sendReservation"; // Replace with your actual API endpoint
            var client = new ApiClient();

            await client.SendPostRequestAsync(apiUrl, reservation);
            
        }
        
    }
}