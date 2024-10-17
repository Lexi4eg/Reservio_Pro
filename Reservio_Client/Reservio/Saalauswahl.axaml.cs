using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class SaalPage : UserControl
    {
        public SaalPage()
        {
            InitializeComponent();
        }
        
        // Event handler für Weiter-Button
        private void OnWeiterButtonClick(object sender, RoutedEventArgs e)
        {
            // Überprüfen, ob ein Tisch ausgewählt wurde
            if (areaComboBox.SelectedItem != null)
            {
                // Den ausgewählten Tisch holen (z.B. "F1", "F2", etc.)
                string selectedTable = (areaComboBox.SelectedItem as ComboBoxItem).Content.ToString();

                // Navigiere zur Personendaten-Seite und übergebe den ausgewählten Tisch
                this.Content = new Personendaten(selectedTable);
            }
            else
            {
                // Fehlermeldung anzeigen, wenn kein Tisch ausgewählt wurde
                ShowErrorMessage("Bitte wählen Sie einen Tisch aus.");
            }
        }

        // Methode zur Anzeige einer Fehlermeldung (optional)
        private void ShowErrorMessage(string message)
        {
            // Hier könntest du eine MessageBox oder ein anderes UI-Element zur Fehleranzeige implementieren
        }

        private void OnZurückButtonClick(object sender, RoutedEventArgs e)
        {
            this.Content = new ThirdPage();        
        }
    }
}