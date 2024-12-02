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
            // Überprüfen, ob ein Tisch ausgewählt wurde
            if (Personenanzahl != null)
            {
                // Den ausgewählten Tisch holen (z.B. "F1", "F2", etc.)
                string personenanzahl = Personenanzahl.Text;

                // Navigiere zur Personendaten-Seite und übergebe den ausgewählten Tisch
                this.Content = new ThirdPage(personenanzahl);
            }
            else
            {
                // Fehlermeldung anzeigen, wenn kein Tisch ausgewählt wurde
                ShowErrorMessage("Bitte wählen Sie einen Tisch aus.");
            }
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