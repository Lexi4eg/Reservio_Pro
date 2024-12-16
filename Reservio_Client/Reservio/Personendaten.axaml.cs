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
        private DateTime Datum { get; }

        public Personendaten(string selectedTable, string personenanzahl, DateTime datum)
        {
            InitializeComponent();
            SelectedTable = selectedTable;
            Personenanzahl = personenanzahl;
            Datum = datum;

            DisplaySelectedTable();
        }

        private void DisplaySelectedTable()
        { 
            selectedTableTextBlock.Text = $"Ausgewählter Tisch: {SelectedTable}";
        }

        private void OnZurückButtonClick(object sender, RoutedEventArgs e)
        {
            this.Content = new Bereichsauswahl(Personenanzahl, Datum);
        }

        private async void OnWeiterButtonClick(object sender, RoutedEventArgs e)
        {
            if (!IsInputValid())
            {
                ShowErrorMessage("Bitte füllen Sie alle Felder aus, bevor Sie fortfahren.");
                return;
            }

            var reservation = new ReservationRequest
            {
                id = Guid.NewGuid().ToString(),
                firstname = Vorname.Text,
                lastname = Nachname.Text,
                date = Datum,
                peopleCount = int.Parse(Personenanzahl),
                email = Email.Text,
                phoneNumber = Telefonnummer.Text,
                specialRequests = "",
                highChair = false,
                tableID = SelectedTable,
                numberChairs = 0
            };

            string apiUrl = "http://localhost:4567/sendReservation"; 
            var client = new ApiClient();

            await client.SendPostRequestAsync(apiUrl, reservation);
            this.Content = new LastPage();
        }

        private bool IsInputValid()
        {
            return !string.IsNullOrWhiteSpace(Vorname.Text) &&
                   !string.IsNullOrWhiteSpace(Nachname.Text) &&
                   !string.IsNullOrWhiteSpace(Email.Text) &&
                   !string.IsNullOrWhiteSpace(Telefonnummer.Text);
        }

        private void ShowErrorMessage(string message)
        {
            var errorWindow = new Window
            {
                Width = 300,
                Height = 150,
                Content = new TextBlock
                {
                    Text = message,
                    HorizontalAlignment = Avalonia.Layout.HorizontalAlignment.Center,
                    VerticalAlignment = Avalonia.Layout.VerticalAlignment.Center,
                    TextWrapping = Avalonia.Media.TextWrapping.Wrap,
                    FontSize = 16
                }
            };
            errorWindow.ShowDialog((Window)this.VisualRoot);
        }
    }

    public class ReservationRequest
    {
        public string id { get; set; }
        public string firstname { get; set; }
        public string lastname { get; set; }
        public DateTime date { get; set; }
        public int peopleCount { get; set; }
        public string email { get; set; }
        public string phoneNumber { get; set; }
        public string specialRequests { get; set; }
        public bool highChair { get; set; }
        public string tableID { get; set; }
        public int numberChairs { get; set; }
    }

    public class ApiClient
    {
        public async Task SendPostRequestAsync(string url, object data)
        {
            using (HttpClient client = new HttpClient())
            {
                var content = new StringContent(JsonSerializer.Serialize(data), Encoding.UTF8, "application/json");
                var response = await client.PostAsync(url, content);

                if (!response.IsSuccessStatusCode)
                {
                    throw new Exception($"Fehler bei der API-Anfrage: {response.StatusCode}");
                }
            }
        }
    }
}
