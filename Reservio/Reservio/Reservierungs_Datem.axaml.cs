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

        // Event handler for Weiter button click
        private void OnWeiterButtonClick1(object sender, RoutedEventArgs e)
        {
            // Eingabewerte validieren
            if (IsInputValid())
            {
                // Navigieren zur nächsten Seite
                this.Content = new ThirdPage(); // Ersetzen Sie 'NextPage' durch die entsprechende Seite
            }
            else
            {
                // Fehlernachricht anzeigen
                ShowErrorMessage("Bitte füllen Sie alle erforderlichen Felder aus.");
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