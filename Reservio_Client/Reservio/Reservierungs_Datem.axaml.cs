using System;
using System.Numerics;
using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class SecondPage : UserControl
    {
        public SecondPage()
        {
            InitializeComponent();
        }
        
        private void OnWeiterButtonClick(object sender, RoutedEventArgs e)
        {
            string personenanzahl = Personenanzahl.Text;
            var selectedDate = datePicker.SelectedDate.HasValue ? datePicker.SelectedDate.Value.DateTime : (DateTime?)null;
            var selectedHour = hoursComboBox.SelectedItem as ComboBoxItem;
            var selectedMinute = minutesComboBox.SelectedItem as ComboBoxItem;

            // Zusammenstellen der Uhrzeit
            var time = TimeSpan.Parse($"{selectedHour.Content}:{selectedMinute.Content}");

            // Daten an die nächste Seite weitergeben
            var reservierungsDatum = selectedDate.Value.Date + time;
            
            this.Content = new ThirdPage(personenanzahl, reservierungsDatum);
        }

        private bool IsInputValid()
        {
            // Überprüfen, ob das Datum ausgewählt ist
            if (datePicker.SelectedDate == null)
                return false;

            // Überprüfen, ob eine Stunde ausgewählt ist
            if (hoursComboBox.SelectedItem == null)
                return false;

            // Überprüfen, ob eine Minute ausgewählt ist
            if (minutesComboBox.SelectedItem == null)
                return false;

            // Überprüfen, ob die Anzahl der Personen eingegeben wurde
            if (Personenanzahl.Text == null)
            {
                return false;
            }
            
            // Alle Eingaben sind gültig
            return true;
        }

        private void ShowErrorMessage(string message)
        {
            // Hier können Sie eine MessageBox oder einen anderen Mechanismus zur Anzeige der Fehlermeldung verwenden
        }
    }
}