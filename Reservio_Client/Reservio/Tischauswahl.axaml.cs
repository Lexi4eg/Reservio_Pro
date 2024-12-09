using System;
using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class ThirdPage : UserControl
    {
        private string Personenanzahl { get; }
        private DateTime Datum { get; }

        public ThirdPage(string personenanzahl, DateTime datum)
        {
            InitializeComponent();
            Personenanzahl = personenanzahl;
            Datum = datum;
        }

        // Event handler for Weiter button click
        private void OnWeiterButtonClick(object sender, RoutedEventArgs e)
        {
            // Überprüfen, ob ein Bereich ausgewählt ist
            var comboBox = this.FindControl<ComboBox>("areaComboBox");
            var selectedItem = comboBox.SelectedItem as ComboBoxItem;

            if (selectedItem == null)
            {
                ShowErrorMessage("Bitte wählen Sie einen Bereich aus, bevor Sie fortfahren!");
                return;
            }

            string selectedArea = selectedItem.Content.ToString();

            // Navigation basierend auf dem ausgewählten Bereich
            switch (selectedArea)
            {
                case "Lounge":
                    this.Content = new LoungePage(Personenanzahl, Datum);
                    break;
                case "Gang":
                    this.Content = new GangPage(Personenanzahl, Datum);
                    break;
                case "Saal":
                    this.Content = new SaalPage(Personenanzahl, Datum);
                    break;
                case "Terrasse":
                    this.Content = new TerrassenPage(Personenanzahl, Datum);
                    break;
                case "Freibereich":
                    this.Content = new FreibereichPage(Personenanzahl, Datum);
                    break;
                default:
                    ShowErrorMessage("Ungültiger Bereich ausgewählt.");
                    break;
            }
        }

        private void OnZurückButtonClick(object sender, RoutedEventArgs e)
        {
            // Zurück zur SecondPage
            this.Content = new SecondPage();
        }

        private void ShowErrorMessage(string message)
        {
            // Beispiel für eine einfache Fehlermeldung
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
}
