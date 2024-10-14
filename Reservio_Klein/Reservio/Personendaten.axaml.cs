using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class Personendaten : UserControl
    {
        private string SelectedTable { get; }

        public Personendaten(string selectedTable)
        {
            InitializeComponent();
            SelectedTable = selectedTable;

            // Den ausgewählten Tisch in einem TextBlock anzeigen
            DisplaySelectedTable();
        }

        private void DisplaySelectedTable()
        {
            // Den TextBlock mit der Tischnummer befüllen (in der XAML-Datei muss ein TextBlock mit x:Name="selectedTableTextBlock" vorhanden sein)
            selectedTableTextBlock.Text = $"Ausgewählter Tisch: {SelectedTable}";
        }

        private void OnZurückButtonClick(object sender, RoutedEventArgs e)
        {
            this.Content = new ThirdPage();        
        }
    }
}